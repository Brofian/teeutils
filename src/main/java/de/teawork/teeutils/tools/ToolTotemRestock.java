package de.teawork.teeutils.tools;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import de.teawork.teeutils.gui.part.PartMenu;
import de.teawork.teeutils.util.Tool;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class ToolTotemRestock extends Tool {

    public static ToolTotemRestock INSTANCE = new ToolTotemRestock();

    @Override
    public void addConfigPart(PartMenu menu) {
        enabledMenu = menu.addEntry("Totem Restock", () -> {
            toggleTool(MinecraftClient.getInstance());
        });
    }

    @Override
    public String getName() {
        return "Totem Restock";
    }

    @Override
    public void register() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    public static void registerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("ttotem-restock").executes(ctx -> toggleCommand()));
    }

    private static int toggleCommand() {
        INSTANCE.toggleTool(MinecraftClient.getInstance());
        return Command.SINGLE_SUCCESS;
    }

    private int findTotem(ScreenHandler container) {
        for (Slot slot: container.slots) {
            ItemStack stackSlot = slot.getStack();
            // is normal inventory slot adn is totem
            if (slot.id > 8 && slot.id < 45 && stackSlot.getItem() == Items.TOTEM_OF_UNDYING)
                return slot.id;
        }
        return -1;
    }

    private void onTick(MinecraftClient client) {
        if (!this.isEnabled) return;
        if (client.player == null || client.interactionManager == null) return;
        ItemStack offhand = client.player.getOffHandStack();
        // noting to do if already a totem in offhand
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) return;
        // no totem -> search for totem in inventory
        int totemSlot = this.findTotem(client.player.playerScreenHandler);
        // TODO: add warning that we ran out of totems to restock
        if (totemSlot == -1) return;
        // swap totem to offhand
        client.player.setCurrentHand(Hand.OFF_HAND);
        ScreenHandler container = client.player.currentScreenHandler;
        if (container == client.player.playerScreenHandler) {
            client.interactionManager.clickSlot(container.syncId, totemSlot, 40, SlotActionType.SWAP, client.player);
        }
        client.player.sendMessage(Text.literal(getName() + ": §5Restocked§r"), true);
    }
}
