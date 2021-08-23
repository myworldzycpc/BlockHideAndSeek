package io.github.myworldzycpc.block_has.tabs;

import io.github.myworldzycpc.block_has.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ItemTab extends CreativeTabs {
    public ItemTab() {

        super("item_tab");

    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.BLOCK_HAS);
    }

}

