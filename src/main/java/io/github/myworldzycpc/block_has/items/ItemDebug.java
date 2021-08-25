package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.MessageSettings;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDebug extends Item implements IHasModel {

    public ItemDebug() {
        setTranslationKey("debug");
        setRegistryName("debug");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote) {
            MessageSettings message = new MessageSettings();
            message.nbt = new NBTTagCompound();
            SettingsWorldSavedData.getGlobal(worldIn).writeToNBT(message.nbt);
            NetworkLoader.instance.sendTo(message, (EntityPlayerMP) playerIn);
            BlockPos pos = playerIn.getPosition();
            int id = GuiElementLoader.GUI_SETTINGS;
            playerIn.openGui(Main.instance, id, playerIn.world, pos.getX(), pos.getY(), pos.getZ());
        }


//        if (worldIn.isRemote) {
//            MessageSettings message = new MessageSettings();
//            message.nbt = new NBTTagCompound();
//            message.nbt.setTag("settings", new NBTTagCompound());
//            NetworkLoader.instance.sendToServer(message);
//        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
