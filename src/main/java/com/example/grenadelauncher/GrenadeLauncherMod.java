package com.example.grenadelauncher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.grenadelauncher.entity.GrenadeEntity;
import com.example.grenadelauncher.item.GrenadeLauncherItem;

public class GrenadeLauncherMod implements ModInitializer {
    public static final String MOD_ID = "grenadelauncher";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Grenade launcher item
    public static final Item GRENADE_LAUNCHER = registerItem("grenade_launcher",
            new GrenadeLauncherItem(new ItemSettings().maxCount(1).maxDamage(100)));

    // Grenade entity type
    public static final EntityType<GrenadeEntity> GRENADE_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "grenade"),
            FabricEntityTypeBuilder.<GrenadeEntity>create()
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .build(GrenadeEntity::new)
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world! Grenade Launcher mod is loading...");

        // Register the grenade launcher item to the combat tab
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "grenade_launcher"), GRENADE_LAUNCHER);
        Registry.register(Registries.ITEM_GROUP_ENTRY, Identifier.of(MOD_ID, "grenade_launcher_entry"),
                ItemGroups.COMBAT.addEntries((entries) -> entries.add(GRENADE_LAUNCHER)));

        LOGGER.info("Grenade Launcher mod has loaded successfully!");
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}