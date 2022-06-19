package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.network.OperationType;
import io.github.myworldzycpc.block_has.util.BlockHasPlayingMode;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.util.PlayingType;
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

import javax.annotation.Nonnull;
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
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        GameRules gamerules = worldIn.getGameRules();

        if (!worldIn.isRemote) {
            SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
            PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
            Vec3d hallPosition = blockHasSettingsGlobal.getHallPosition();
            for (EntityPlayer player : worldIn.playerEntities) {
                player.setGameType(SettingsWorldSavedData.getGlobal(worldIn).getPlayingGameMode());
                player.inventory.clear();
                FuncOperation.teleportPlayer(player, hallPosition);
            }
            blockHasPlayingGlobal.setPlaying(PlayingType.READY);
            blockHasPlayingGlobal.setHunterWaitingTime(-1);
            FuncOperation.updatePlayingData(worldIn);

            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    FuncOperation.debugInfo(worldIn, "Thread Timer ran.");

                    if (blockHasPlayingGlobal.getPlaying() != PlayingType.END_GAME) {
                        for (EntityPlayer player : worldIn.playerEntities) {
                            if (blockHasSettingsGlobal.getPlayingMode() == BlockHasPlayingMode.RANDOM) {
                                player.inventory.addItemStackToInventory(new ItemStack(ModItems.READY_OFF));
                            } else if (blockHasSettingsGlobal.getPlayingMode() == BlockHasPlayingMode.FREE) {
                                player.inventory.addItemStackToInventory(new ItemStack(ModItems.BE_HUNTER));
                                player.inventory.addItemStackToInventory(new ItemStack(ModItems.BE_BLOCK));
                                player.inventory.addItemStackToInventory(new ItemStack(ModItems.BE_SPECTATOR));

                            }
                            player.inventory.setInventorySlotContents(8, new ItemStack(ModItems.FORCE_END));
//                            player.inventory.addItemStackToInventory(new ItemStack(ModItems.FORCE_END));
                        }
                        gamerules.setOrCreateGameRule("sendCommandFeedback", blockHasSettingsGlobal.isDebugMode() ? "true" : "false");
                        FuncOperation.sendSingleOperationToClient(OperationType.CLOSE_BOUNDING_BOX);
                    }
                }
            }, 1000);

        }
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
