package net.nimbu.nimbus_pyromancy.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.nimbu.nimbus_pyromancy.entity.ModEntities;
import net.nimbu.nimbus_pyromancy.entity.custom.PyroflameEntity;

import java.util.List;

public class FlameControllerItem extends Item {
    public FlameControllerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (world.isClient()) return;
        if (!(user instanceof PlayerEntity player)) return;

        double radius = 5.0; //size of control area based on skill?

        Vec3d eyePos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F); // direction player is looking
        double distance = 3; // 1.5 blocks in front
        Vec3d controlPos = eyePos.add(lookVec.multiply(distance)); //where the flames are dragged to

        Box controlBox = new Box(
                controlPos.x - radius, controlPos.y - radius, controlPos.z - radius,
                controlPos.x + radius, controlPos.y + radius, controlPos.z + radius
        );



        //-----------Turn fire into pyroflames-------------

        // convert box bounds to block positions
        int minX = MathHelper.floor(controlBox.minX);
        int minY = MathHelper.floor(controlBox.minY);
        int minZ = MathHelper.floor(controlBox.minZ);

        int maxX = MathHelper.floor(controlBox.maxX);
        int maxY = MathHelper.floor(controlBox.maxY);
        int maxZ = MathHelper.floor(controlBox.maxZ);

        BlockPos.Mutable mutable = new BlockPos.Mutable(); //mutable block positions can be changed without being re-instantiated

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.set(x, y, z);
                    BlockState state = world.getBlockState(mutable);
                    if (state.isOf(Blocks.FIRE)) { //if fire found
                        //check is in circle of the radius
                        Vec3d blockCentre = Vec3d.ofCenter(mutable);
                        Vec3d toTarget = controlPos.subtract(blockCentre);
                        if (toTarget.length()<=radius-0.5) { //check a little closer than radius for smoothness
                            world.setBlockState(mutable, Blocks.AIR.getDefaultState());
                            PyroflameEntity flame = ModEntities.PYROFLAME.create(world);
                            flame.setPosition(x, y, z);
                            world.spawnEntity(flame);
                        }
                    }
                }
            }
        }




        //--------------Collects all pyroflame entities within radius--------------

        List<Entity> entities = world.getOtherEntities(user, controlBox);
        for (Entity entity : entities) {
            if (entity instanceof PyroflameEntity flame) {
                Vec3d toTarget = controlPos.subtract(flame.getPos());
                double distanceToControlPoint = toTarget.length();

                if(distanceToControlPoint<=radius) { //only do anything if with the radius

                    Vec3d direction = toTarget.normalize();
                    double speed = 1;
                    if (distanceToControlPoint<speed) {
                        speed = distanceToControlPoint;
                    }

                    speed = Math.max(0, speed); //stops negative velocities

                    flame.setVelocity(direction.multiply(speed));
                    flame.velocityModified = true;
                    flame.setOwner(user);
                }
            }
        }





        //        if (world instanceof ServerWorld serverWorld)
//            drawDebugBox(serverWorld,controlBox);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    private void drawDebugBox(ServerWorld world, Box box) {

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;

        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

        Vec3d[] corners = new Vec3d[] {
                new Vec3d(minX, minY, minZ),
                new Vec3d(maxX, minY, minZ),
                new Vec3d(minX, maxY, minZ),
                new Vec3d(maxX, maxY, minZ),
                new Vec3d(minX, minY, maxZ),
                new Vec3d(maxX, minY, maxZ),
                new Vec3d(minX, maxY, maxZ),
                new Vec3d(maxX, maxY, maxZ)
        };

        for (Vec3d corner : corners){
            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    corner.x, corner.y, corner.z,
                    1,
                    0, 0, 0,
                    0
            );
        }
    }
}
