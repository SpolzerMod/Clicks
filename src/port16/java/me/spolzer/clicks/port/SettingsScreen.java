package me.spolzer.clicks.port;

import com.mojang.blaze3d.vertex.PoseStack;

import me.spolzer.clicks.Settings;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;

public class SettingsScreen extends Settings {
	public SettingsScreen(Screen parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float delta) {
		this.renderBackground(pose);
		super.render(pose, mouseX, mouseY, delta);
		this.paint(new Brush(pose));
	}

	@Override
	protected <T extends AbstractWidget> T add(T widget) {
		return this.addButton(widget);
	}
}
