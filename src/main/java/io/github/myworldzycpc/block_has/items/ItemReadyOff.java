package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemReadyOff extends Item implements IHasModel {

    public ItemReadyOff() {

        setTranslationKey("ready_off");
        setRegistryName("ready_off");
        if (Reference.SHOW_DEBUG_ITEM) {
            setCreativeTab(Main.ITEM_TAB);
        }

        ModItems.ITEMS.add(this);

    }


    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        playerIn.setHeldItem(handIn, new ItemStack(ModItems.READY_ON));
        if (!worldIn.isRemote) {
            PlayingWorldSavedData.getGlobal(worldIn).getPlayer(playerIn.getUniqueID()).setReady(true);
            PlayingWorldSavedData.getGlobal(worldIn).markDirty();
            FuncFragment.detectForReady(worldIn);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
