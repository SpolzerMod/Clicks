package me.spolzer.clicks;

import me.spolzer.clicks.port.Game;
import me.spolzer.clicks.port.Ui;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;

public abstract class Settings extends Screen {
	private static final int WIDE = 300;
	private static final int CONTROL = 110;
	private static final int STEP = 22;
	private static final int PREVIEW_TOP = 14;
	private static final int PREVIEW_HEIGHT = 52;
	private static final int ROWS_TOP = 76;
	private static final int TAIL = 192;
	private static final int RESET_CONFIRM_TICKS = 80;

	private final Screen parent;

	private Button counting;
	private Button visible;
	private Button goals;
	private Button reset;
	private EditBox leftGoal;
	private EditBox rightGoal;

	private int confirm;
	private int left;
	private int top;

	protected Settings(Screen parent) {
		super(Ui.text("clicks.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.left = this.width / 2 - WIDE / 2;
		this.top = this.height / 2 - 106;

		int control = this.left + WIDE - CONTROL;
		int rows = this.top + ROWS_TOP;

		this.counting = this.add(Ui.button(this::pause, control, rows, CONTROL, 20));
		this.visible = this.add(Ui.button(this::hide, control, rows + STEP, CONTROL, 20));
		this.goals = this.add(Ui.button(this::aim, control, rows + STEP * 2, CONTROL, 20));

		this.leftGoal = this.box(control, rows + STEP * 3, "clicks.goal.lmb", Config.leftGoal(), true);
		this.rightGoal = this.box(control, rows + STEP * 4, "clicks.goal.rmb", Config.rightGoal(), false);

		this.reset = this.add(Ui.button(this::wipe, this.left, this.top + TAIL, 145, 20));
		this.add(Ui.named("clicks.done", this::onClose, this.left + WIDE - 145, this.top + TAIL, 145, 20));

		this.confirm = 0;
		this.refresh();
	}

	protected abstract <T extends AbstractWidget> T add(T widget);

	@Override
	public void tick() {
		super.tick();

		if (this.confirm > 0 && --this.confirm == 0) {
			this.refresh();
		}
	}

	@Override
	public void onClose() {
		Config.save();
		Game.setScreen(this.parent);
	}

	protected void paint(Paint paint) {
		int well = this.top + PREVIEW_TOP;
		int rows = this.top + ROWS_TOP;
		String title = this.title.getString();

		paint.text(this.font, title, this.width / 2 - this.font.width(title) / 2, this.top, Tone.VALUE);

		paint.fill(this.left, well, this.left + WIDE, well + PREVIEW_HEIGHT, Tone.WELL);
		paint.fill(this.left, well, this.left + WIDE, well + 1, Tone.EDGE);
		paint.fill(this.left, well + PREVIEW_HEIGHT - 1, this.left + WIDE, well + PREVIEW_HEIGHT, Tone.EDGE);
		paint.fill(this.left, well + 1, this.left + 1, well + PREVIEW_HEIGHT - 1, Tone.EDGE);
		paint.fill(this.left + WIDE - 1, well + 1, this.left + WIDE, well + PREVIEW_HEIGHT - 1, Tone.EDGE);

		Hud.draw(paint, this.font, this.left + WIDE / 2 - Hud.width(this.font) / 2,
				well + PREVIEW_HEIGHT / 2 - Hud.height(this.font) / 2);

		this.name(paint, "clicks.counting", rows, Tone.LABEL);
		this.name(paint, "clicks.hud", rows + STEP, Tone.LABEL);
		this.name(paint, "clicks.goals", rows + STEP * 2, Tone.LABEL);
		this.name(paint, "clicks.goal.lmb", rows + STEP * 3, Config.goalsOn() ? Tone.LABEL : Tone.MUTED);
		this.name(paint, "clicks.goal.rmb", rows + STEP * 4, Config.goalsOn() ? Tone.LABEL : Tone.MUTED);
	}

	private void name(Paint paint, String key, int y, int color) {
		paint.text(this.font, Ui.text(key).getString(), this.left + 2, y + 6, color);
	}

	private EditBox box(int x, int y, String key, long value, boolean primary) {
		EditBox box = Ui.box(this.font, x, y, CONTROL, 20, Ui.text(key));
		box.setMaxLength(Config.GOAL_DIGITS);
		box.setValue(value > 0 ? Long.toString(value) : "");
		box.setResponder(text -> this.typed(box, text, primary));
		return this.add(box);
	}

	private void typed(EditBox box, String text, boolean primary) {
		String clean = Numbers.digits(text, Config.GOAL_DIGITS);

		if (!clean.equals(text)) {
			box.setValue(clean);
			return;
		}

		long value = clean.isEmpty() ? 0 : Long.parseLong(clean);

		if (primary) {
			Config.leftGoal(value);
		} else {
			Config.rightGoal(value);
		}
	}

	private void pause() {
		Config.paused(!Config.paused());
		this.refresh();
	}

	private void hide() {
		Config.shown(!Config.shown());
		this.refresh();
	}

	private void aim() {
		Config.goalsOn(!Config.goalsOn());
		this.refresh();
	}

	private void wipe() {
		if (this.confirm > 0) {
			this.confirm = 0;
			Config.wipe();
			Config.save();
		} else {
			this.confirm = RESET_CONFIRM_TICKS;
		}

		this.refresh();
	}

	private void refresh() {
		this.counting.setMessage(Ui.text(Config.paused() ? "clicks.counting.off" : "clicks.counting.on"));
		this.visible.setMessage(Ui.text(Config.shown() ? "clicks.hud.on" : "clicks.hud.off"));
		this.goals.setMessage(Ui.text(Config.goalsOn() ? "clicks.on" : "clicks.off"));
		this.reset.setMessage(Ui.text(this.confirm > 0 ? "clicks.reset.again" : "clicks.reset"));
		this.leftGoal.setEditable(Config.goalsOn());
		this.rightGoal.setEditable(Config.goalsOn());
	}
}
