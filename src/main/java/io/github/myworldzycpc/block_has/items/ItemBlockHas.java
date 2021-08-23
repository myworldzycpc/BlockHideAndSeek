package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
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

            playerIn.setGameType(GameType.ADVENTURE);
            playerIn.inventory.clear();

            (new Timer()).schedule(new TimerTask() {
                public void run() {
                    FuncOperation.debugInfo(worldIn, "Thread Timer ran.");
                    playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.READY_OFF));
                    if (!Reference.DEBUG_MODE) {
                        gamerules.setOrCreateGameRule("sendCommandFeedback", "false");
                    } else {
                        gamerules.setOrCreateGameRule("sendCommandFeedback", "true");
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
