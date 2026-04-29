package net.nimbu.nimbus_pyromancy.item.custom;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.nimbu.nimbus_pyromancy.entity.custom.FlameEntity;

import java.util.List;

public class FlameControllerItem extends Item {
    public FlameControllerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
//        ItemStack itemStack = user.getStackInHand(hand);
//
//        double radius = 5.0; //size of control area based on skill?
//        Vec3d centrePos = user.getPos().add(1,1,0); //where the flames are dragged to
//
//        Box controlBox = new Box(
//                centrePos.x - radius, centrePos.y - radius, centrePos.z - radius,
//                centrePos.x + radius, centrePos.y + radius, centrePos.z + radius
//        );
//        List<Entity> entities = world.getOtherEntities(user, controlBox);
//        for (Entity entity : entities) {
//            entity.setPos(centrePos.x,centrePos.y,centrePos.z);
//        }


        return TypedActionResult.consume(user.getStackInHand(hand));
        //return TypedActionResult.success(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (world.isClient()) return;
        if (!(user instanceof PlayerEntity player)) return;

        double radius = 5.0; //size of control area based on skill?
        Vec3d centrePos = player.getPos().add(1,1,0); //where the flames are dragged to

        Box controlBox = new Box(
                centrePos.x - radius, centrePos.y - radius, centrePos.z - radius,
                centrePos.x + radius, centrePos.y + radius, centrePos.z + radius
        );

        if (world instanceof ServerWorld serverWorld)
            drawDebugBox(serverWorld,controlBox);

        //collects all flame entities within radius
        List<Entity> entities = world.getOtherEntities(user, controlBox);
        int count=0;
        for (Entity entity : entities) {
            if (entity instanceof FlameEntity flame) {
                flame.setGatherLocation(centrePos);
                System.out.println("Found set to x:"+centrePos.x+" y:"+centrePos.y+" z:"+centrePos.z);
            }
            count++;
            System.out.println("Found entity no."+count+" of: "+entity.getName());
            entity.requestTeleport(centrePos.x,centrePos.y,centrePos.z);
        }
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
