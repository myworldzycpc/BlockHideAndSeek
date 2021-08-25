package io.github.myworldzycpc.block_has.network;

import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class Handler implements IMessageHandler<MessageSettings, IMessage> {

    @Override
    public IMessage onMessage(MessageSettings message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {

            WorldClient worldClient = Minecraft.getMinecraft().world;
            EntityPlayer player = worldClient.getPlayerEntityByUUID(UUID.fromString(message.nbt.getString("player")));
            World world = player.world;
            if (world instanceof WorldServer) {
                WorldServer worldServer = (WorldServer) world;
                NBTTagCompound settingsCompound = (NBTTagCompound) message.nbt.getTag("settings");
                FuncOperation.debugInfo(worldServer, String.valueOf(settingsCompound.getInteger("timeForHunterToWait")));
                SettingsWorldSavedData.getGlobal(worldServer).readFromNBT(message.nbt);
            }

        }

        return null;
    }

}
