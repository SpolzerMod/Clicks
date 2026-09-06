package me.spolzer.clicks.port;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;

public final class Game {
	private Game() {
	}

	public static void hud() {
		HudRenderCallback.EVENT.register(new Overlay());
	}

	public static Screen screen() {
		return Minecraft.getInstance().screen;
	}

	public static void setScreen(Screen screen) {
		Minecraft.getInstance().setScreen(screen);
	}

	public static boolean chatOpen() {
		return screen() instanceof ChatScreen;
	}

	public static boolean hideGui() {
		return Minecraft.getInstance().options.hideGui;
	}
}
