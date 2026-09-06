package me.spolzer.clicks.port;

import me.spolzer.clicks.Hud;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Overlay implements HudElement {
	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker tracker) {
		Hud.place(new Brush(graphics), Minecraft.getInstance().font);
	}
}
