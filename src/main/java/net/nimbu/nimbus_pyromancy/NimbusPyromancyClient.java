package net.nimbu.nimbus_pyromancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.nimbu.nimbus_pyromancy.entity.ModEntities;
import net.nimbu.nimbus_pyromancy.particle.ModParticleTypes;
import net.nimbu.nimbus_pyromancy.particle.custom.PyroflameParticle;
import org.lwjgl.glfw.GLFW;

public class NimbusPyromancyClient implements ClientModInitializer {

    public static KeyBinding absorbHeat;
    public static KeyBinding ventHeat;

    @Override
    public void onInitializeClient() {


        EntityRendererRegistry.register(ModEntities.PYROFLAME, EmptyEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.PYROFLAME, PyroflameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BLUE_PYROFLAME, PyroflameParticle.Factory::new);

        absorbHeat = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nimbuspyromancy.absorb_heat", /* translation key */
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C, /* default key */
                "category.nimbuspyromancy.controls" /* category */
        ));
        ventHeat = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nimbuspyromancy.vent_heat", /* translation key */
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F, /* default key */
                "category.nimbuspyromancy.controls" /* category */
        ));
    }
}
