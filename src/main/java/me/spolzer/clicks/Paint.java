package me.spolzer.clicks;

import net.minecraft.client.gui.Font;

public interface Paint {
	int width();

	int height();

	void fill(int x1, int y1, int x2, int y2, int color);

	void text(Font font, String value, int x, int y, int color);
}
