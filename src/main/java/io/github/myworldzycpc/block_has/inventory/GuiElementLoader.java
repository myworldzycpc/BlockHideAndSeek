package io.github.myworldzycpc.block_has.inventory;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerDemo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class GuiElementLoader implements IGuiHandler {

    public static final int GUI_SETTINGS = 1;

    public GuiElementLoader() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, this);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (ID == GUI_SETTINGS) {
            return new ContainerDemo(player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_SETTINGS) {
            return new GuiContainerDemo(new ContainerDemo(player));
        }
        return null;
    }
}