package net.nimbu.nimbus_pyromancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.nimbu.nimbus_pyromancy.entity.ModEntities;
import net.nimbu.nimbus_pyromancy.particle.ModParticleTypes;
import net.nimbu.nimbus_pyromancy.particle.custom.PyroflameParticle;

public class NimbusPyromancyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {


        EntityRendererRegistry.register(ModEntities.PYROFLAME, EmptyEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.PYROFLAME, PyroflameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.BLUE_PYROFLAME, PyroflameParticle.Factory::new);

    }
}
