package io.github.myworldzycpc.block_has.network;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerAddMap;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerSettings;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class Handler implements IMessageHandler<BlockHasMessage, IMessage> {

    @Override
    public IMessage onMessage(BlockHasMessage message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {

            EntityPlayerMP player = ctx.getServerHandler().player;

            if (player.isServerWorld()) {
                String operation = message.nbt.getString("operation");
                WorldServer worldServer = (WorldServer) player.world;

                if (operation.equals("update_settings_data")) {
                    NBTTagCompound settingsCompound = (NBTTagCompound) message.nbt.getTag("settings");
                    SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldServer);
                    NBTTagCompound oldSettingsCompound = (NBTTagCompound) BlockHasSettingsGlobal.writeToNBT(new NBTTagCompound()).getTag("settings");

                    if (!(oldSettingsCompound.toString().equals(settingsCompound.toString()))) {
//                    FuncOperation.debugInfo(worldServer, String.valueOf(settingsCompound.getInteger("timeForHunterToWait")));
                        BlockHasSettingsGlobal.readFromNBT(message.nbt);
                        NetworkLoader.instance.sendToAll(message);
                    }

                } else if (operation.equals("open_gui")) {
                    BlockHasMessage messageBack = new BlockHasMessage();
                    messageBack.nbt = new NBTTagCompound();
                    SettingsWorldSavedData.getGlobal(worldServer).writeToNBT(messageBack.nbt);
                    messageBack.nbt.setString("operation", "open_gui");
                    messageBack.nbt.setInteger("guiId", message.nbt.getInteger("guiId"));
                    NetworkLoader.instance.sendTo(messageBack, player);
                } else if (operation.equals("teleport")) {
                    NBTTagCompound messageCompound = message.nbt;
                    SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(player.world);
                    int selectingMapIndex = messageCompound.getInteger("selectingMapIndex");
                    Vec3d target = BlockHasSettingsGlobal.getBlockHasMaps().get(selectingMapIndex).spawnPoint;
                    FuncOperation.teleportPlayer(player, target);
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
//                        player.closeScreen();
                        BlockPos pos = player.getPosition();
                        int id = message.nbt.getInteger("guiId");
                        player.openGui(Main.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
                    } else if (operation.equals("update_settings_data")) {
                        EntityPlayer player = Minecraft.getMinecraft().player;
                        SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                        GuiContainerSettings.needUpdate = true;
                        GuiContainerAddMap.needUpdate = true;
                    }
                }
            });

        }

        return null;
    }

}
