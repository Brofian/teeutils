package de.teawork.teeutils.tools;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.teawork.teeutils.gui.part.PartMenu;
import de.teawork.teeutils.util.Tool;
import de.teawork.teeutils.util.ToolManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;


public class ToolRepair extends Tool {

    public static ToolRepair INSTANCE = new ToolRepair();


    @Override
    public String getName() {
        return "Repair Automation";
    }

    @Override
    public String getDescription() {
        return "Automates swapping broken mending tools to offhand and auto attacks mobs.";
    }

    @Override
    public void addConfigPart(PartMenu menu) {
        enabledMenu = menu.addEntry("Repair Automation", getDescription(), () -> {
            toggleTool(MinecraftClient.getInstance());
        });
    }

    @Override
    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void attack(MinecraftClient client) {
        ItemStack mainHand = client.player.getMainHandStack();
        if (!(mainHand.getItem() instanceof SwordItem)) return;
        if (client.player.getAttackCooldownProgress(0.5F) < 1.0F) return;
        switch (client.crosshairTarget.getType()) {
            case MISS:
            case BLOCK:
                break;
            case ENTITY:
                client.interactionManager.attackEntity(client.player, ((EntityHitResult) client.crosshairTarget).getEntity());
                client.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private int findToolToSwap(MinecraftClient client, ScreenHandler container) {
        for (Slot slot: container.slots) {
            ItemStack stackSlot = slot.getStack();
            if (slot.id > 8 && slot.id < 45 &&  // is normal inventory slot
                stackSlot.isDamaged() &&  // is a damaged item
                slot.getStack() != client.player.getInventory().getMainHandStack() &&  // not in main hand
                EnchantmentHelper.getLevel(Enchantments.MENDING, stackSlot) > 0  // has mending
            )
                return slot.id;
        }
        return -1;
    }

    private void swapToolToHand(MinecraftClient client, int slotWithItem) {
        ScreenHandler container = client.player.currentScreenHandler;
        if (container == client.player.playerScreenHandler)
            client.interactionManager.clickSlot(container.syncId, slotWithItem, 40, SlotActionType.SWAP, client.player);
    }

    private void swapIfNeeded(MinecraftClient client) {
        ItemStack offHand = client.player.getOffHandStack();
        // if offhand item needs to be repaired, skip out
        if (offHand.isDamaged() && EnchantmentHelper.getLevel(Enchantments.MENDING, offHand) > 0)
            return;
        int slotWithItem = findToolToSwap(client, client.player.playerScreenHandler);
        if (slotWithItem != -1) {
            swapToolToHand(client, slotWithItem);
        } else {
            // we are done, all tools repaired -> disable
            isEnabled = false;
            syncEnabledMenu();
            ToolManager.saveConfig();
            client.player.sendMessage(Text.literal(getName() + ": §5Completed§r"), true);
        }
    }

    private void onTick(MinecraftClient client) {
        if (client.player != null && client.world != null) {
            if (isEnabled) {
                attack(client);
                swapIfNeeded(client);
            }
        }
    }
}
