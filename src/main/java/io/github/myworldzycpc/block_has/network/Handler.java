package io.github.myworldzycpc.block_has.network;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
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
                FuncOperation.debugInfo(worldServer, String.valueOf(settingsCompound.getInteger("timeForHunterToWait")));
                SettingsWorldSavedData.getGlobal(worldServer).readFromNBT(message.nbt);
            }

        } else if (ctx.side == Side.CLIENT) {

            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    SettingsWorldSavedData.getGlobal(player.world).readFromNBT(message.nbt);

                    BlockPos pos = player.getPosition();
                    int id = GuiElementLoader.GUI_SETTINGS;
                    player.openGui(Main.instance, id, player.world, pos.getX(), pos.getY(), pos.getZ());
                }
            });

        }

        return null;
    }

}
