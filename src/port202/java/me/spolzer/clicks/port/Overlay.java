package me.spolzer.clicks.port;

import me.spolzer.clicks.Hud;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Overlay implements HudRenderCallback {
	@Override
	public void onHudRender(GuiGraphics graphics, float delta) {
		Hud.place(new Brush(graphics), Minecraft.getInstance().font);
	}
}
