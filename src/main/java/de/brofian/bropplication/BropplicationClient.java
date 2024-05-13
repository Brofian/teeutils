package de.brofian.bropplication;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import org.slf4j.Logger;


public class BropplicationClient implements ClientModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        System.out.println("initializing client!!!");
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher,
                                        CommandRegistryAccess registryAccess) {

    }

}