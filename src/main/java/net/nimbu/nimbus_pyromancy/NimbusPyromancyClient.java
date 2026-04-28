package net.nimbu.nimbus_pyromancy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.nimbu.nimbus_pyromancy.entity.ModEntities;

public class NimbusPyromancyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {


        EntityRendererRegistry.register(ModEntities.FLAME, EmptyEntityRenderer::new);
    }
}
