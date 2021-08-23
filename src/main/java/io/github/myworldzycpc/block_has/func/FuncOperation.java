package io.github.myworldzycpc.block_has.func;

import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class FuncOperation {


    public static void message(EntityPlayer player, String text) {
        player.sendStatusMessage(new TextComponentString(text), false);
    }

    public static int executeCommand(EntityPlayer playerIn, String command) {
        MinecraftServer server = Minecraft.getMinecraft().getIntegratedServer();
        int status_code;
        if (server == null) {
            debugInfo(playerIn.getEntityWorld(), "executeCommand(): You cannot run vanilla commands on a server!");
            status_code = -2;
        } else {
            status_code = server.getCommandManager().executeCommand(playerIn, command);
        }
        debugInfo(playerIn.getEntityWorld(), String.format("executeCommand(%d): <%s> %s", status_code, playerIn.getDisplayNameString(), command));
        return status_code;
    }

    public static void messageAll(World worldIn, String text) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            message(playerIn, text);
        }
    }

    public static void debugInfo(World worldIn, String text) {
        System.out.println(text);
        if (Reference.DEBUG_MODE) {
            messageAll(worldIn, String.format("\u00a7a[DEBUG] %s", text));
        }
    }

}
