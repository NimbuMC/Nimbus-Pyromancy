package net.nimbu.nimbus_pyromancy.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.nimbu.nimbus_pyromancy.NimbusPyromancy;
import net.nimbu.nimbus_pyromancy.item.custom.FlameControllerItem;

public class ModItems {

    public static final Item FLAME_CONTROLLER = registerItem("flame_controller",
            new FlameControllerItem(new Item.Settings()
                    .rarity(Rarity.EPIC)
                    .component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(NimbusPyromancy.MOD_ID, name), item);
    }
    public static void registerModItems(){
        NimbusPyromancy.LOGGER.info("Registering mod items for "+ NimbusPyromancy.MOD_ID);
    }
}
