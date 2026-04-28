package net.nimbu.nimbus_pyromancy;

import net.fabricmc.api.ModInitializer;

import net.nimbu.nimbus_pyromancy.entity.ModEntities;
import net.nimbu.nimbus_pyromancy.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NimbusPyromancy implements ModInitializer {
	public static final String MOD_ID = "nimbuspyromancy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntities.registerModEntities();
	}
}