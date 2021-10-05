package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        if (!worldIn.isRemote) {
            UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
            Block facingBlock = worldIn.getBlockState(pos).getBlock();
            IBlockState facingBlockState = worldIn.getBlockState(pos);
            int facingBlockStateMeta = facingBlock.getMetaFromState(facingBlockState);
            String facingBlockName = facingBlock.getRegistryName().toString();
            Item facingBlockItem = Item.getItemFromBlock(facingBlock);
            String facingBlockItemName = facingBlockItem.getRegistryName().toString();
            ItemStack facingBlockItemStack = new ItemStack(facingBlockItem, 1, facingBlockStateMeta);
            List<String> bannedBlocks = new ArrayList<String>(Arrays.asList(
                    "minecraft:tallgrass",
                    "minecraft:double_plant",
                    "minecraft:fire",
                    "minecraft:barrier",
                    "minecraft:water",
                    "minecraft:lava",
                    "minecraft:redstone_wire",
                    "minecraft:standing_sign",
                    "minecraft:wall_sign",
                    "minecraft:standing_banner",
                    "minecraft:wall_banner",
                    "minecraft:skull"
            ));
            String blockNameShow;

            if (facingBlockItemName.equals("minecraft:air")) {
                blockNameShow = facingBlock.getLocalizedName();
            } else {
                blockNameShow = facingBlockItem.getItemStackDisplayName(facingBlockItemStack);
            }

            if (!bannedBlocks.contains(facingBlockName)) {
                FuncOperation.executeCommand(player, String.format("morph %s block {Meta:%db, Block:\"%s\", Name:\"block\"}", uuid.toString(), facingBlockStateMeta, facingBlockName));
                FuncOperation.messageTranslation(player, "block_has.chat.become_selected_block.become", blockNameShow);
            } else {
                FuncOperation.messageTranslation(player, "block_has.chat.become_selected_block.cant_become", blockNameShow);
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }


}
