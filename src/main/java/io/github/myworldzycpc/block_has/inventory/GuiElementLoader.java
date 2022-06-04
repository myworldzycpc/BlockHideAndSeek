package io.github.myworldzycpc.block_has.inventory;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerAddBannedBlock;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerAddMap;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerDemo;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerSettings;
import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class GuiElementLoader implements IGuiHandler {

    public static final int GUI_DEMO = FuncAlgorithms.getNextId();
    public static final int GUI_SETTINGS = FuncAlgorithms.getNextId();
    public static final int GUI_ADD_MAP = FuncAlgorithms.getNextId();
    public static final int GUI_ADD_BANNED_BLOCK = FuncAlgorithms.getNextId();

    public GuiElementLoader() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, this);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (ID == GUI_DEMO) {
            return new ContainerDemo(player);
        } else if (ID == GUI_SETTINGS) {
            return new ContainerSettings(player);
        } else if (ID == GUI_ADD_MAP) {
            return new ContainerAddMap(player);
        } else if (ID == GUI_ADD_BANNED_BLOCK) {
            return new ContainerAddBannedBlock(player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_DEMO) {
            return new GuiContainerDemo(new ContainerDemo(player));
        } else if (ID == GUI_SETTINGS) {
            return new GuiContainerSettings(new ContainerSettings(player));
        } else if (ID == GUI_ADD_MAP) {
            return new GuiContainerAddMap(new ContainerAddMap(player));
        } else if (ID == GUI_ADD_BANNED_BLOCK) {
            return new GuiContainerAddBannedBlock(new ContainerAddBannedBlock(player));
        }
        return null;
    }
}