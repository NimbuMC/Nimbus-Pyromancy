package net.nimbu.nimbus_pyromancy.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nimbu.nimbus_pyromancy.NimbusPyromancy;

public class ModParticleTypes {

    public static final SimpleParticleType PYROFLAME =
            registerParticle("pyroflame_particle", FabricParticleTypes.simple(true)); //will always spawn, even if particles are turned off in settings

    private static SimpleParticleType registerParticle(String name, SimpleParticleType simpleParticleType){
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(NimbusPyromancy.MOD_ID, name), simpleParticleType);
    }
    public static void registerParticles(){
        NimbusPyromancy.LOGGER.info("Registering particles for "+ NimbusPyromancy.MOD_ID);
    }
}
