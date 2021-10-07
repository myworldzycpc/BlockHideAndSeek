package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemHicaSensor extends Item implements IHasModel {

    public ItemHicaSensor() {
        setTranslationKey("hica_sensor");
        setRegistryName("hica_sensor");
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote) {
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer playerIn = (EntityPlayer) entityIn;
                PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
                SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
                BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(playerIn);
                if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HUNTER) {
                    double differenceMinDistance = blockHasPlayer.getDifferenceMinDistance();
                    double hicaSensorSensitivity = blockHasSettingsGlobal.getHicaSensorSensitivity();
                    int meta;
                    if (differenceMinDistance > hicaSensorSensitivity) {
                        meta = 5;
                    } else if (differenceMinDistance > 0) {
                        meta = (int) (5 * (differenceMinDistance / hicaSensorSensitivity));
                    } else {
                        meta = 0;
                    }
                    stack.setItemDamage(meta);
                }
            }
        }

    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 6; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < 6; i++) {
            Main.proxy.registerItemRenderer(this, i, "inventory");
        }
    }

}
