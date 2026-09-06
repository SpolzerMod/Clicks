package me.spolzer.clicks.mixin;

import me.spolzer.clicks.MouseInput;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
	@Inject(method = "onPress", at = @At("HEAD"))
	private void clicks$count(long window, int button, int action, int modifiers, CallbackInfo info) {
		MouseInput.handle(button, action);
	}
}
