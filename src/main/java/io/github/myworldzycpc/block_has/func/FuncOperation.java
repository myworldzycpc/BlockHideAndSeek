package io.github.myworldzycpc.block_has.func;

import com.mojang.text2speech.Narrator;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class FuncOperation {

    public static void messageCompound(EntityPlayer player, ITextComponent textComponent) {
        player.sendStatusMessage(textComponent, false);
    }

    public static void messageAllCompound(World worldIn, ITextComponent textComponent) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            messageCompound(playerIn, textComponent);
        }
    }

    public static void message(EntityPlayer player, String text) {
        player.sendStatusMessage(new TextComponentString(text), false);
    }

    public static void messageTranslation(EntityPlayer player, String translationKey, Object... args) {
        player.sendStatusMessage(new TextComponentTranslation(translationKey, args), false);
    }

    public static void messageAll(World worldIn, String text) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            message(playerIn, text);
        }
    }

    public static void messageAllTranslation(World worldIn, String translationKey, Object... args) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            messageTranslation(playerIn, translationKey, args);
        }
    }

    public static void actionbar(EntityPlayer player, String text) {
        player.sendStatusMessage(new TextComponentString(text), true);
    }

    public static void actionbarTranslation(EntityPlayer player, String translationKey, Object... args) {
        player.sendStatusMessage(new TextComponentTranslation(translationKey, args), true);
    }

    public static void actionbarAll(World worldIn, String text) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            actionbar(playerIn, text);
        }
    }

    public static void actionbarAllTranslation(World worldIn, String translationKey, Object... args) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            actionbarTranslation(playerIn, translationKey, args);
        }
    }

    public static void debugInfo(World worldIn, String text) {
        System.out.println(text);
        if (Reference.DEBUG_MODE) {
            messageAll(worldIn, String.format("\u00a7a[DEBUG] %s", text));
        }
    }

    public static void title(EntityPlayer player, String text) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
            SPacketTitle.Type sPacketTitle$type = SPacketTitle.Type.TITLE;
            SPacketTitle sPacketTitle1 = new SPacketTitle(sPacketTitle$type, new TextComponentString(text));
            entityplayermp.connection.sendPacket(sPacketTitle1);
        }
    }

    public static void titleTranslation(EntityPlayer player, String translationKey, Object... args) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) player;
            SPacketTitle.Type sPacketTitle$type = SPacketTitle.Type.TITLE;
            SPacketTitle sPacketTitle1 = new SPacketTitle(sPacketTitle$type, new TextComponentTranslation(translationKey, args));
            entityplayermp.connection.sendPacket(sPacketTitle1);
        }
    }

    public static void titleAll(World worldIn, String text) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            title(playerIn, text);
        }
    }

    public static void titleAllTranslation(World worldIn, String translationKey, Object... args) {
        for (EntityPlayer playerIn : worldIn.playerEntities) {
            titleTranslation(playerIn, translationKey, args);
        }
    }

    public static void say(String content) {
        Narrator.getNarrator().say(content);
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

    public static void teleportPlayer(EntityPlayer player, Vec3d pos) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).connection.setPlayerLocation(pos.x, pos.y, pos.z, player.rotationYaw, player.rotationPitch);
        }
    }

    public static void dirtyAll(World worldIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
    }

}
