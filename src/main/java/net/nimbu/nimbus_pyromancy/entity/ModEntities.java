package net.nimbu.nimbus_pyromancy.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nimbu.nimbus_pyromancy.NimbusPyromancy;
import net.nimbu.nimbus_pyromancy.entity.custom.FlameEntity;

public class ModEntities {

    public static final EntityType<FlameEntity> FLAME = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(NimbusPyromancy.MOD_ID, "flame"),
            EntityType.Builder.<FlameEntity>create(FlameEntity::new, SpawnGroup.MISC)
                    .dimensions(0.25f,0.25f).maxTrackingRange(128).build());

    public static void registerModEntities(){
        NimbusPyromancy.LOGGER.info("Registering mod entities for "+ NimbusPyromancy.MOD_ID);
    }
}
