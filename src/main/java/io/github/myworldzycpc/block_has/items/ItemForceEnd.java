package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemForceEnd extends Item implements IHasModel {

    public ItemForceEnd() {
        setTranslationKey("force_end");
        setRegistryName("force_end");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote) {
            FuncFragment.endGame(worldIn);
        }

        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
