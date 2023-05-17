package de.teawork.teeutils.mixins;

import de.teawork.teeutils.tools.ToolBottleProtect;
import de.teawork.teeutils.tools.ToolBucketProtect;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Inject(method = "interactItem", at = @At("HEAD"), cancellable = true)
    public void interactItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ToolBucketProtect.INSTANCE.isEnabled) {
            ItemStack itemStack = player.getStackInHand(hand);
            // is a bucket and has more than 1 in stack
            if (itemStack.getItem() == Items.BUCKET && itemStack.getCount() > 1) {
                if (player.getInventory().getEmptySlot() == -1) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
        if (ToolBottleProtect.INSTANCE.isEnabled) {
            ItemStack itemStack = player.getStackInHand(hand);
            // is a bottle and has more than 1 in stack
            if (itemStack.getItem() == Items.GLASS_BOTTLE && itemStack.getCount() > 1) {
                if (player.getInventory().getEmptySlot() == -1) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
    }
}

