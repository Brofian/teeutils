package de.brofian.bropplication;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.impl.networking.NetworkHandlerExtensions;
import net.fabricmc.fabric.mixin.event.interaction.ServerPlayNetworkHandlerMixin;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Bropplication implements ModInitializer {

    public static final String MOD_ID = "bropplication";

    public static final Logger LOGGER = LoggerFactory.getLogger(Bropplication.MOD_ID);

    ArrayList<String> messageQueue = new ArrayList<>();

    @Override
    public void onInitialize() {
        LOGGER.info("initializing server!");


        ServerTickEvents.END_SERVER_TICK.register(this::onTick);
        ServerMessageEvents.CHAT_MESSAGE.register(this::onMessage);
    }

    private void onMessage(SignedMessage signedMessage, ServerPlayerEntity serverPlayerEntity, MessageType.Parameters parameters) {
        LOGGER.info(signedMessage.getSignedContent());
        this.messageQueue.add("You said: " + signedMessage.getSignedContent());
    }

    private void onTick(MinecraftServer minecraftServer) {
        while (!this.messageQueue.isEmpty()) {
            String message = this.messageQueue.remove(0);
            minecraftServer.getPlayerManager().broadcast(Text.literal(message), false);
        }
    }
}