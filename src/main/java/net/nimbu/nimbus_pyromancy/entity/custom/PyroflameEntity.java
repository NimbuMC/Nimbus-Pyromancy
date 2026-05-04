package net.nimbu.nimbus_pyromancy.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PyroflameEntity extends Entity implements Ownable {

    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    private Vec3d gatherLocation=null;
    private int heat;
    private boolean controlled;
    @Nullable
    private PyroflameEntity mergeTarget;
    private boolean absorbing;

    public PyroflameEntity(EntityType<?> type, World world) {
        super(type, world);
        this.heat=100;
        this.controlled=false;
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient) {

            if (this.mergeTarget != null) {
                this.mergeTarget.absorbFlame(this.heat);
                this.mergeTarget=null;
                this.discard();
                return;
            }

            if (this.gatherLocation != null) {

                this.controlled = true; //set check for if flames are being controlled
                Vec3d toTarget = this.gatherLocation.subtract(this.getPos());
                double distance = toTarget.length();

                Vec3d direction = toTarget.normalize();
                double speed;
                if (distance < 0.7) {
                    speed = 0.6 * (Math.pow(3000, -Math.pow((distance - 0.7), 2))) - 0.03;
                } else {
                    speed = -0.2036 * distance + 0.7125;
                }

                speed = Math.max(0, speed); //stops negative velocities

                this.setVelocity(direction.multiply(speed));
                this.velocityModified = true;
                this.gatherLocation = null; //immediately reset - if input ever cancels, movement should stop changing
            }else { //fire burns out if no player is firebending
                this.heat--;
                if (this.heat <= 0) {
                    this.discard();
                    return;
                }
            }
        }
        this.absorbing=false;
        this.move(MovementType.SELF, this.getVelocity());
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
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
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
    public boolean isPushable() {
        return true;
    }

    public void setGatherLocation(Vec3d target){
        this.gatherLocation=target;
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
