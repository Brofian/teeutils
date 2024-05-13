package de.brofian.bropplication.mixins;

import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(method = "onRenameItem", at = @At(value = "TAIL"))
    public void onRenameItem(RenameItemC2SPacket packet, CallbackInfo ci) {
        System.out.println("Hello world: " + packet.getName());
    }

}