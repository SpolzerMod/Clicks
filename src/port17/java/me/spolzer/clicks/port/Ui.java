package me.spolzer.clicks.port;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public final class Ui {
	private Ui() {
	}

	public static Component text(String key) {
		return new TranslatableComponent(key);
	}

	public static Button button(Runnable action, int x, int y, int width, int height) {
		return new Button(x, y, width, height, TextComponent.EMPTY, press -> action.run());
	}

	public static Button named(String key, Runnable action, int x, int y, int width, int height) {
		return new Button(x, y, width, height, text(key), press -> action.run());
	}

	public static EditBox box(Font font, int x, int y, int width, int height, Component label) {
		return new EditBox(font, x, y, width, height, label);
	}
}
