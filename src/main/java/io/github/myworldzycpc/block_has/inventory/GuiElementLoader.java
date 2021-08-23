package io.github.myworldzycpc.block_has.inventory;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerDemo;
import io.github.myworldzycpc.block_has.client.gui.GuiContainerSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

public class GuiElementLoader implements IGuiHandler {

    public static final int GUI_DEMO = 1;
    public static final int GUI_SETTINGS = 2;

    public GuiElementLoader() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, this);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case GUI_DEMO:
                return new ContainerDemo(player);
            case GUI_SETTINGS:
                return new ContainerSettings(player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_DEMO:
                return new GuiContainerDemo(new ContainerDemo(player));
            case GUI_SETTINGS:
                return new GuiContainerSettings(new ContainerSettings(player));
        }
        return null;
    }
}