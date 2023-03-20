package de.teawork.teeutils.mixins;
import de.teawork.teeutils.tools.ToolBucketProtect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class MixinBucketItem {

    @Shadow @Final private Fluid fluid;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(
            World world,
            PlayerEntity user,
            Hand hand,
            CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        if (ToolBucketProtect.INSTANCE.isEnabled) {
            ItemStack itemStack = user.getStackInHand(hand);
            // is stack of empty buckets && has more than 1 item
            if (this.fluid == Fluids.EMPTY && itemStack.getCount() > 1) {
                // check for free spots in inventory
                if (user.getInventory().getEmptySlot() == -1) {
                    cir.setReturnValue(TypedActionResult.fail(itemStack));
                }
            }
        }
    }
}
