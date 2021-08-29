package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemBecomeSelectedBlock extends Item implements IHasModel {

    public ItemBecomeSelectedBlock() {
        setTranslationKey("become_selected_block");
        setRegistryName("become_selected_block");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }


    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote){
            UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
            Block facingBlock = worldIn.getBlockState(pos).getBlock();
            IBlockState facingBlockState = worldIn.getBlockState(pos);
            int facingBlockStateMeta = facingBlock.getMetaFromState(facingBlockState);
            String facingBlockName = facingBlock.getRegistryName().toString();
            Item facingBlockItem = Item.getItemFromBlock(facingBlock);
            String facingBlockItemName = facingBlockItem.getRegistryName().toString();
            ItemStack facingBlockItemStack = new ItemStack(facingBlockItem, 1, facingBlockStateMeta);

            String blockNameShow;

            FuncOperation.executeCommand(player, String.format("morph %s block {Meta:%db, Block:\"%s\", Name:\"block\"}", uuid.toString(), facingBlockStateMeta, facingBlockName));

            if (facingBlockItemName.equals("minecraft:air")){
                blockNameShow = facingBlock.getLocalizedName();
            } else {
                blockNameShow = facingBlockItem.getItemStackDisplayName(facingBlockItemStack);
            }

            FuncOperation.message(player, I18n.format("item.become_selected_block.become", blockNameShow));
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }


}
