package me.spolzer.clicks;

import me.spolzer.clicks.port.Game;
import me.spolzer.clicks.port.Ui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public final class Hud {
	private static final int PAD = 5;
	private static final int STRIPE = 2;
	private static final int STRIPE_GAP = 5;
	private static final int COLUMN_GAP = 12;
	private static final int BAR = 2;
	private static final int BAR_GAP = 3;
	private static final int ROW_GAP = 4;

	private Hud() {
	}

	public static int width(Font font) {
		int labels = Math.max(font.width(label(true)), font.width(label(false)));
		int values = Math.max(measure(font, true), measure(font, false));
		return PAD * 2 + STRIPE + STRIPE_GAP + labels + COLUMN_GAP + values;
	}

	public static int height(Font font) {
		return PAD * 2 + row(font) * 2 + ROW_GAP;
	}

	public static void place(Paint paint, Font font) {
		Drag.follow();

		if (!Config.shown() || Minecraft.getInstance().player == null || Game.hideGui()) {
			return;
		}

		draw(paint, font, anchorX(font, paint.width()), anchorY(font, paint.height()));
	}

	public static int anchorX(Font font, int room) {
		int width = width(font);
		int x = Config.x();

		if (x < 0) {
			x = room - width - 4;
		}

		return Math.max(0, Math.min(x, room - width));
	}

	public static int anchorY(Font font, int room) {
		return Math.max(0, Math.min(Config.y(), room - height(font)));
	}

	public static void draw(Paint paint, Font font, int x, int y) {
		int width = width(font);
		int height = height(font);
		int row = row(font);

		paint.fill(x, y, x + width, y + height, Tone.PANEL);
		paint.fill(x, y, x + width, y + 1, Tone.EDGE);
		paint.fill(x, y + height - 1, x + width, y + height, Tone.EDGE);
		paint.fill(x, y + 1, x + 1, y + height - 1, Tone.EDGE);
		paint.fill(x + width - 1, y + 1, x + width, y + height - 1, Tone.EDGE);

		line(paint, font, x, y + PAD, width, true);
		line(paint, font, x, y + PAD + row + ROW_GAP, width, false);
	}

	private static void line(Paint paint, Font font, int x, int y, int width, boolean primary) {
		long count = primary ? Config.left() : Config.right();
		long goal = goal(primary);
		boolean reached = goal > 0 && count >= goal;
		boolean idle = Config.paused();
		int tone = idle ? Tone.IDLE : reached ? Tone.REACHED : primary ? Tone.LEFT : Tone.RIGHT;

		int textX = x + PAD + STRIPE + STRIPE_GAP;
		int edge = x + width - PAD;

		paint.fill(x + PAD, y, x + PAD + STRIPE, y + font.lineHeight, tone);
		paint.text(font, label(primary), textX, y, idle ? Tone.MUTED : Tone.LABEL);

		String head = Numbers.format(count);
		String tail = goal > 0 ? "/" + Numbers.format(goal) : "";
		int span = font.width(head) + font.width(tail);

		paint.text(font, head, edge - span, y, idle ? Tone.MUTED : reached ? Tone.REACHED : Tone.VALUE);

		if (!tail.isEmpty()) {
			paint.text(font, tail, edge - font.width(tail), y, Tone.MUTED);
		}

		if (!Config.goalsOn()) {
			return;
		}

		int barY = y + font.lineHeight + BAR_GAP;
		paint.fill(textX, barY, edge, barY + BAR, Tone.TRACK);

		if (goal > 0) {
			int room = edge - textX;
			int filled = count >= goal ? room : (int) (room * ((double) count / goal));
			paint.fill(textX, barY, textX + filled, barY + BAR, tone);
		}
	}

	private static int row(Font font) {
		return font.lineHeight + (Config.goalsOn() ? BAR_GAP + BAR : 0);
	}

	private static long goal(boolean primary) {
		if (!Config.goalsOn()) {
			return 0;
		}

		return primary ? Config.leftGoal() : Config.rightGoal();
	}

	private static int measure(Font font, boolean primary) {
		long goal = goal(primary);
		String text = Numbers.format(primary ? Config.left() : Config.right());
		return font.width(goal > 0 ? text + "/" + Numbers.format(goal) : text);
	}

	public static String label(boolean primary) {
		return Ui.text(primary ? "clicks.lmb" : "clicks.rmb").getString();
	}
}
