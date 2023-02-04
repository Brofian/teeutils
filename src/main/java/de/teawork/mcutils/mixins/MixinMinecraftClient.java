package de.teawork.mcutils.mixins;

import de.teawork.mcutils.tools.ToolDimensionalVolume;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
