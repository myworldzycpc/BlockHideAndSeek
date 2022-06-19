package io.github.myworldzycpc.block_has.network;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerAddMap;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerSettings;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
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
                OperationType operation = OperationType.fromId(message.nbt.getInteger("operation"));
                WorldServer worldServer = (WorldServer) player.world;
                if (operation == OperationType.CLIENT_RECEIVED_PACK) {
                    FuncOperation.debugInfo(worldServer, String.format("CLIENT %s received pack!", player.getDisplayNameString()));
                } else {
                    FuncOperation.debugInfo(worldServer, "SERVER received pack!");
                }

                if (operation == OperationType.UPDATE_SETTINGS_DATA) {
                    NBTTagCompound settingsCompound = (NBTTagCompound) message.nbt.getTag(SettingsWorldSavedData.KEY);
                    SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldServer);
                    NBTTagCompound oldSettingsCompound = (NBTTagCompound) BlockHasSettingsGlobal.writeToNBT(new NBTTagCompound()).getTag(SettingsWorldSavedData.KEY);

                    if (!(oldSettingsCompound.toString().equals(settingsCompound.toString()))) {
//                    FuncOperation.debugInfo(worldServer, String.valueOf(settingsCompound.getInteger("timeForHunterToWait")));
                        BlockHasSettingsGlobal.readFromNBT(message.nbt);
                        NetworkLoader.instance.sendToAll(message);
                    }

                } else if (operation == OperationType.OPEN_GUI) {
                    BlockHasMessage messageBack = new BlockHasMessage();
                    messageBack.nbt = new NBTTagCompound();
                    SettingsWorldSavedData.getGlobal(worldServer).writeToNBT(messageBack.nbt);
                    messageBack.nbt.setInteger("operation", OperationType.OPEN_GUI.id);
                    messageBack.nbt.setInteger("guiId", message.nbt.getInteger("guiId"));
                    NetworkLoader.instance.sendTo(messageBack, player);
                } else if (operation == OperationType.TELEPORT) {
                    NBTTagCompound messageCompound = message.nbt;
                    SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(player.world);
                    int selectingMapIndex = messageCompound.getInteger("selectingMapIndex");
                    Vec3d target = blockHasSettingsGlobal.getBlockHasMaps().get(selectingMapIndex).spawnPoint;
                    FuncOperation.teleportPlayer(player, target);
                } else if (operation == OperationType.KICK_BY_CHEAT) {
                    if (worldServer.playerEntities.get(0) != player) {
                        FuncOperation.messageAllTranslation(worldServer, "block_has.chat.kick_somebody_by_cheat.colored", player.getDisplayNameString());
                        Minecraft.getMinecraft().addScheduledTask(() -> {
                            player.connection.disconnect(new TextComponentTranslation("block_has.chat.kick_by_cheat"));
                        });
                    }
                }
            }

        } else if (ctx.side == Side.CLIENT) {

            Minecraft.getMinecraft().addScheduledTask(() -> {
                OperationType operation = OperationType.fromId(message.nbt.getInteger("operation"));
                EntityPlayer player = Minecraft.getMinecraft().player;
                FuncOperation.reportReceivedPack();
                if (operation == OperationType.OPEN_GUI) {
                    SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);
//                        player.closeScreen();
                    BlockPos pos = player.getPosition();
                    int id = message.nbt.getInteger("guiId");
                    player.openGui(Main.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
                } else if (operation == OperationType.UPDATE_SETTINGS_DATA) {
                    SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                    GuiContainerSettings.needUpdate = true;
                    GuiContainerAddMap.needUpdate = true;
                } else if (operation == OperationType.UPDATE_PLAYING_DATA) {
                    PlayingWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                } else if (operation == OperationType.CLOSE_BOUNDING_BOX) {
                    // todo: close bb before game start.
                    Minecraft.getMinecraft().getRenderManager().setDebugBoundingBox(false);
                }
            });

        }

        return null;
    }

}
