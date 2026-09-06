package me.spolzer.clicks.port;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public final class Ui {
	private Ui() {
	}

	public static Component text(String key) {
		return Component.translatable(key);
	}

	public static Button button(Runnable action, int x, int y, int width, int height) {
		return Button.builder(Component.empty(), press -> action.run()).bounds(x, y, width, height).build();
	}

	public static Button named(String key, Runnable action, int x, int y, int width, int height) {
		return Button.builder(text(key), press -> action.run()).bounds(x, y, width, height).build();
	}

	public static EditBox box(Font font, int x, int y, int width, int height, Component label) {
		return new EditBox(font, x, y, width, height, label);
	}
}
