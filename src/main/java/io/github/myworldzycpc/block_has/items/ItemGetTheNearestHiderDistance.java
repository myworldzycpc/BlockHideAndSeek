package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemGetTheNearestHiderDistance extends Item implements IHasModel {

    public ItemGetTheNearestHiderDistance() {
        setTranslationKey("get_the_nearest_hider_distance");
        setRegistryName("get_the_nearest_hider_distance");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote) {
            PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
            SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
            playerIn.inventory.clearMatchingItems(ModItems.GET_THE_NEAREST_HIDER_DISTANCE, -1, 1, null);
            List<Double> distanceList = new ArrayList<Double>();
            Vec3d playerInPosition = playerIn.getPositionVector();
            for (EntityPlayer player : worldIn.playerEntities) {
                if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HIDER) {
                    distanceList.add(playerInPosition.distanceTo(player.getPositionVector()));
                }
            }
            double minDistance = Collections.min(distanceList);
            FuncOperation.messageTranslation(playerIn, "block_has.chat.get_the_nearest_hider_distance.get", FuncAlgorithms.roundTo(playerInPosition.x, 2), FuncAlgorithms.roundTo(playerInPosition.y, 2), FuncAlgorithms.roundTo(playerInPosition.z, 2), FuncAlgorithms.roundTo(minDistance, 2));
            blockHasPlayingGlobal.getPlayer(playerIn).setToolCD(blockHasSettingsGlobal.getToolCoolingDownTime());
            FuncFragment.startToolCoolingDown(worldIn, playerIn);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
