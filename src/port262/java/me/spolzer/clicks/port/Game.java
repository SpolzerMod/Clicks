package me.spolzer.clicks.port;

import me.spolzer.clicks.Clicks;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

public final class Game {
	private Game() {
	}

	public static void hud() {
		HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Clicks.MODID, "counter"), new Overlay());
	}

	public static Screen screen() {
		return Minecraft.getInstance().gui.screen();
	}

	public static void setScreen(Screen screen) {
		Minecraft.getInstance().gui.setScreen(screen);
	}

	public static boolean chatOpen() {
		return screen() instanceof ChatScreen;
	}

	public static boolean hideGui() {
		return Minecraft.getInstance().gui.hud.isHidden();
	}
}
