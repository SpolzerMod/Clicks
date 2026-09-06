package me.spolzer.clicks.port;

import me.spolzer.clicks.Settings;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class SettingsScreen extends Settings {
	public SettingsScreen(Screen parent) {
		super(parent);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, delta);
		this.paint(new Brush(graphics));
	}

	@Override
	protected <T extends AbstractWidget> T add(T widget) {
		return this.addRenderableWidget(widget);
	}
}
