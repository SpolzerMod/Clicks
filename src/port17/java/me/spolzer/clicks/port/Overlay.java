package me.spolzer.clicks.port;

import com.mojang.blaze3d.vertex.PoseStack;

import me.spolzer.clicks.Hud;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class Overlay implements HudRenderCallback {
	@Override
	public void onHudRender(PoseStack pose, float delta) {
		Hud.place(new Brush(pose), Minecraft.getInstance().font);
	}
}
