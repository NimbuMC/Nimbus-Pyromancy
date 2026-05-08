package net.nimbu.nimbus_pyromancy.particle.custom;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;

public class BluePyroflameParticle extends PyroflameParticle{
    protected BluePyroflameParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider, double xSpeed, double ySpeed, double zSpeed) {
        super(clientWorld, x, y, z, spriteProvider, xSpeed, ySpeed, zSpeed);
    }
}
