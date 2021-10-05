package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.BlockHasMessage;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemSettings extends Item implements IHasModel {

    public ItemSettings() {
        setTranslationKey("settings");
        setRegistryName("settings");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote) {
            BlockHasMessage message = new BlockHasMessage();
            message.nbt = new NBTTagCompound();
            SettingsWorldSavedData.getGlobal(worldIn).writeToNBT(message.nbt);
            message.nbt.setString("operation", "open_gui");
            message.nbt.setInteger("guiId", GuiElementLoader.GUI_SETTINGS);
            NetworkLoader.instance.sendTo(message, (EntityPlayerMP) playerIn);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
