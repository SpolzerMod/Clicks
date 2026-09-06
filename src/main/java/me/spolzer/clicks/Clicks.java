package me.spolzer.clicks;

import me.spolzer.clicks.port.Game;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Clicks implements ClientModInitializer {
	public static final String MODID = "clicks";
	private static final int SAVE_INTERVAL_TICKS = 1200;

	private static int sinceSave;

	@Override
	public void onInitializeClient() {
		Config.load();
		Game.hud();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (++sinceSave >= SAVE_INTERVAL_TICKS) {
				sinceSave = 0;
				Config.save();
			}
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());
	}
}
