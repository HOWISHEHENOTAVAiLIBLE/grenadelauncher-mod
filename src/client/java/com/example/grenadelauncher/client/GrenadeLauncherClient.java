package com.example.grenadelauncher.client;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import com.example.grenadelauncher.GrenadeLauncherMod;

public class GrenadeLauncherClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		// For now, we'll keep it simple as we're focusing on getting the item working first.
		// Later we can add custom rendering, animations, etc.
	}
}