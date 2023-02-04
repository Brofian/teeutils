package de.teawork.teeutils.mixins;

import de.teawork.teeutils.tools.ToolDimensionalVolume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {


    @Inject(method = "setWorld", at = @At("TAIL"))
    private void setWorld(ClientWorld world, CallbackInfo ci) {
        ToolDimensionalVolume.INSTANCE.handleDimensionSwitch(world);
    }


}
