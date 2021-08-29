package io.github.myworldzycpc.block_has.inventory;

import io.github.myworldzycpc.block_has.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerAddMap extends Container {

    public EntityPlayer player;

    public ContainerAddMap(EntityPlayer player) {
        super();
        this.player = player;
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param playerIn
     */
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return new ItemStack(ModItems.SETTINGS).isItemEqual(playerIn.getHeldItemMainhand());
    }


    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
    }

}
