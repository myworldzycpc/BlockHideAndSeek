package io.github.myworldzycpc.block_has.network;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerSettings;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class Handler implements IMessageHandler<MessageSettings, IMessage> {

    @Override
    public IMessage onMessage(MessageSettings message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {

            EntityPlayer player = ctx.getServerHandler().player;

            if (player.isServerWorld()) {
                WorldServer worldServer = (WorldServer) player.world;
                NBTTagCompound settingsCompound = (NBTTagCompound) message.nbt.getTag("settings");
                SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldServer);
                NBTTagCompound oldSettingsCompound = (NBTTagCompound) BlockHasSettingsGlobal.writeToNBT(new NBTTagCompound()).getTag("settings");

                if (!(oldSettingsCompound.toString().equals(settingsCompound.toString()))) {
//                    FuncOperation.debugInfo(worldServer, String.valueOf(settingsCompound.getInteger("timeForHunterToWait")));
                    BlockHasSettingsGlobal.readFromNBT(message.nbt);
                    NetworkLoader.instance.sendToAll(message);
                }

            }

        } else if (ctx.side == Side.CLIENT) {

            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    String operation = message.nbt.getString("operation");
                    if (operation.equals("open_gui")) {
                        EntityPlayer player = Minecraft.getMinecraft().player;
                        SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                        BlockPos pos = player.getPosition();
                        int id = GuiElementLoader.GUI_SETTINGS;
                        player.openGui(Main.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
                    } else if (operation.equals("update_settings_data")) {
                        EntityPlayer player = Minecraft.getMinecraft().player;
                        SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                        GuiContainerSettings.needUpdate = true;
                    }
                }
            });

        }

        return null;
    }

}
