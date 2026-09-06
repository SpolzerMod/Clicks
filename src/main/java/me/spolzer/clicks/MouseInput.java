package me.spolzer.clicks;

import me.spolzer.clicks.port.Game;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class MouseInput {
	private MouseInput() {
	}

	public static void handle(int button, int action) {
		Minecraft client = Minecraft.getInstance();
		boolean primary = button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
		if (primary && action == GLFW.GLFW_RELEASE) {
			Drag.release();
		}

		if (action != GLFW.GLFW_PRESS || client.level == null) {
			return;
		}

		if (Game.chatOpen()) {
			if (primary) {
				Drag.press();
			}
			return;
		}

		if (Game.screen() != null) {
			return;
		}

		if (primary) {
			Config.addLeft();
		} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			Config.addRight();
		}
	}
}
