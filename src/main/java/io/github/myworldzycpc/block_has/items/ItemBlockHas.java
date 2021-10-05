package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

public class ItemBlockHas extends Item implements IHasModel {

    public ItemBlockHas() {
        setTranslationKey("block_has");
        setRegistryName("block_has");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }


    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        GameRules gamerules = worldIn.getGameRules();

        if (!worldIn.isRemote) {
            SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
            PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
            Vec3d hallPosition = BlockHasSettingsGlobal.getHallPosition();
            for (EntityPlayer player : worldIn.playerEntities) {
                player.setGameType(SettingsWorldSavedData.getGlobal(worldIn).getPlayingGameMode());
                player.inventory.clear();
                player.setPosition(hallPosition.x, hallPosition.y, hallPosition.z);
            }
            blockHasPlayingGlobal.setPlaying("ready");
            blockHasPlayingGlobal.setHunterWaitingTime(-1);

            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    FuncOperation.debugInfo(worldIn, "Thread Timer ran.");

                    if (!blockHasPlayingGlobal.getPlaying().equals("endGame")) {
                        for (EntityPlayer player : worldIn.playerEntities) {
                            player.inventory.addItemStackToInventory(new ItemStack(ModItems.READY_OFF));
                            player.inventory.setInventorySlotContents(8, new ItemStack(ModItems.FORCE_END));
//                            player.inventory.addItemStackToInventory(new ItemStack(ModItems.FORCE_END));
                        }
                        if (!Reference.DEBUG_MODE) {
                            gamerules.setOrCreateGameRule("sendCommandFeedback", "false");
                        } else {
                            gamerules.setOrCreateGameRule("sendCommandFeedback", "true");
                        }
                    }
                }
            }, 1000);

        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
