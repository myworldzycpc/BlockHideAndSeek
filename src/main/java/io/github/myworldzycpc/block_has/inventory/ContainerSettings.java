package io.github.myworldzycpc.block_has.inventory;

import io.github.myworldzycpc.block_has.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerSettings extends Container {

    public ContainerSettings(EntityPlayer player) {
        super();

    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return new ItemStack(ModItems.DEBUG).isItemEqual(playerIn.getHeldItemMainhand());
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

    }


}
