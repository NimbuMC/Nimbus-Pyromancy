package net.nimbu.nimbus_pyromancy.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlameEntity extends Entity implements Ownable {

    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    private Vec3d gatherLocation=null;

    public FlameEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (gatherLocation!=null){
            this.setPos(gatherLocation.x,gatherLocation.y,gatherLocation.z);
            gatherLocation=null;
        }

        super.tick();
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
