package net.nimbu.nimbus_pyromancy.entity.custom;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.nimbu.nimbus_pyromancy.entity.ModEntities;
import net.nimbu.nimbus_pyromancy.particle.ModParticleTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PyroflameEntity extends ProjectileEntity implements Ownable {

    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    private int heat;
    @Nullable
    private PyroflameEntity mergeTarget;
    private double gravity = -0.05;

    public PyroflameEntity(EntityType<?> type, World world) {
        super(ModEntities.PYROFLAME, world);
        this.heat=100;
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        if (!world.isClient) {

            if (this.mergeTarget != null) {
                this.mergeTarget.absorbFlame(this.heat);
                this.mergeTarget=null;
                this.discard();
                return;
            }

            if(this.getOwner()!=null){ //todo: this might be really bad for multiplayer due to lag. maybe don't check every tick?
                this.setOwner(null);
                if(this.submergedInWater){
                    this.heat-= 4 + this.heat/20;; //heat still dies in water, even if controlled
                }
            }
            else{
                if(this.submergedInWater){
                    this.heat-= 7 + this.heat/20;; //heat dies much faster in water
                } else{this.heat-= 1 + this.heat/30;}

                if (this.heat <= 0) {
                    this.discard();
                    return;
                }


                this.addVelocity(0,gravity,0);
            }

            //create particle effect
            Position pos = this.getPos();
            SimpleParticleType particleType = (this.heat>1000 ? ModParticleTypes.BLUE_PYROFLAME : ModParticleTypes.PYROFLAME);
            int particleCount = (this.heat>100 ? this.heat/8 : this.heat/4);

            ((ServerWorld) world).spawnParticles(particleType,
                    pos.getX(), pos.getY(), pos.getZ(), particleCount, 0.2, 0.2, 0.2, 0.2);
        }
        this.move(MovementType.SELF, this.getVelocity());

        HitResult hit = ProjectileUtil.getCollision(this, this::canHit);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            this.onBlockHit(blockHit);
        }

        super.tick();
    }

    public int getHeat(){
        return this.heat;
    }

    public void addHeat(int heat){
        this.heat=heat;
    }

    public void absorbFlame(int heat){
        this.heat+=heat;
    }

    @Override
    public boolean collidesWith(Entity entity) {
        if (entity instanceof PyroflameEntity flame && this.mergeTarget == null
                && flame.mergeTarget == null) {
            // ONLY one side allowed to initiate
            if (this.getUuid().compareTo(flame.getUuid()) < 0) {
                this.mergeTarget = flame;
            }
        }
        return super.collidesWith(entity);
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity!=null) {
            this.ownerUuid = entity.getUuid();
        }
        else{
            this.ownerUuid = null;
        }
        this.owner = entity;
    }

    @Nullable
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else {
            if (this.ownerUuid != null) {
                World var2 = this.getWorld();
                if (var2 instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld)var2;
                    this.owner = serverWorld.getEntity(this.ownerUuid);
                    return this.owner;
                }
            }

            return null;
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);
        World world = this.getWorld();
        if(heat<150) { //only do small fire for low heat values
            BlockPos pos = hitResult.getBlockPos().offset(hitResult.getSide());
            if (world.getBlockState(pos).isAir()) {
                BlockState fireState = AbstractFireBlock.getState(world, pos);

                if (fireState.canPlaceAt(world, pos)) {
                    world.setBlockState(pos, fireState);
                }
            }
        }
        else { //explode
            world.createExplosion(this, this.getX(), this.getY(), this.getZ(), (float) this.heat / 300, true, World.ExplosionSourceType.MOB);
        }


        this.discard();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
