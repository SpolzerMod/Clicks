package me.spolzer.clicks.port;

import com.mojang.blaze3d.vertex.PoseStack;

import me.spolzer.clicks.Paint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;

public class Brush implements Paint {
	private final PoseStack pose;

	public Brush(PoseStack pose) {
		this.pose = pose;
	}

	@Override
	public int width() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth();
	}

	@Override
	public int height() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight();
	}

	@Override
	public void fill(int x1, int y1, int x2, int y2, int color) {
		GuiComponent.fill(this.pose, x1, y1, x2, y2, color);
	}

	@Override
	public void text(Font font, String value, int x, int y, int color) {
		font.drawShadow(this.pose, value, x, y, color);
	}
}
