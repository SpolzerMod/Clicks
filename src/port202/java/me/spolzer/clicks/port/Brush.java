package me.spolzer.clicks.port;

import me.spolzer.clicks.Paint;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class Brush implements Paint {
	private final GuiGraphics graphics;

	public Brush(GuiGraphics graphics) {
		this.graphics = graphics;
	}

	@Override
	public int width() {
		return this.graphics.guiWidth();
	}

	@Override
	public int height() {
		return this.graphics.guiHeight();
	}

	@Override
	public void fill(int x1, int y1, int x2, int y2, int color) {
		this.graphics.fill(x1, y1, x2, y2, color);
	}

	@Override
	public void text(Font font, String value, int x, int y, int color) {
		this.graphics.drawString(font, value, x, y, color, true);
	}
}
