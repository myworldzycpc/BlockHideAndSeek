package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
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

public class ItemBeBlock extends Item implements IHasModel {

    public ItemBeBlock() {

        setTranslationKey("be_block");
        setRegistryName("be_block");
        if (Reference.SHOW_DEBUG_ITEM) {
            setCreativeTab(Main.ITEM_TAB);
        }

        ModItems.ITEMS.add(this);

    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote) {
            BlockHasPlayer blockHasPlayer = PlayingWorldSavedData.getGlobal(worldIn).getPlayer(playerIn.getUniqueID());
            blockHasPlayer.setStatus(BlockHasPlayer.Status.HIDER);
            PlayingWorldSavedData.getGlobal(worldIn).markDirty();
            FuncOperation.updatePlayingData(worldIn);

            FuncFragment.detectForReady(worldIn);

            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.selected", playerIn.getDisplayNameString(), blockHasPlayer.getStatus().getDisplayName());
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
