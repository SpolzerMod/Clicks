package me.spolzer.clicks;

import com.mojang.blaze3d.platform.Window;

import me.spolzer.clicks.port.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public final class Drag {
	private static boolean held;
	private static int holdX;
	private static int holdY;

	private Drag() {
	}

	public static void press() {
		Minecraft client = Minecraft.getInstance();

		if (!Config.shown() || Game.hideGui()) {
			return;
		}

		Font font = client.font;
		Window window = client.getWindow();
		int x = Hud.anchorX(font, window.getGuiScaledWidth());
		int y = Hud.anchorY(font, window.getGuiScaledHeight());
		double mouseX = mouseX(client);
		double mouseY = mouseY(client);

		if (mouseX < x || mouseX >= x + Hud.width(font)) {
			return;
		}

		if (mouseY < y || mouseY >= y + Hud.height(font)) {
			return;
		}

		holdX = (int) mouseX - x;
		holdY = (int) mouseY - y;
		held = true;
	}

	public static void follow() {
		if (!held) {
			return;
		}

		Minecraft client = Minecraft.getInstance();

		if (!Game.chatOpen()) {
			release();
			return;
		}

		Window window = client.getWindow();
		int x = Math.min((int) mouseX(client) - holdX, window.getGuiScaledWidth() - Hud.width(client.font));
		int y = Math.min((int) mouseY(client) - holdY, window.getGuiScaledHeight() - Hud.height(client.font));
		Config.place(x, y);
	}

	public static void release() {
		if (!held) {
			return;
		}

		held = false;
		Config.save();
	}

	private static double mouseX(Minecraft client) {
		Window window = client.getWindow();
		return client.mouseHandler.xpos() * window.getGuiScaledWidth() / window.getScreenWidth();
	}

	private static double mouseY(Minecraft client) {
		Window window = client.getWindow();
		return client.mouseHandler.ypos() * window.getGuiScaledHeight() / window.getScreenHeight();
	}
}
