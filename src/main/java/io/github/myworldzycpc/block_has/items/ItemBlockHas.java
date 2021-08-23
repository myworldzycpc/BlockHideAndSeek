package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.Main;
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

public class ItemBlockHas extends Item implements IHasModel {

    public ItemBlockHas(){
        setTranslationKey("block_has");
        setRegistryName("block_has");
        setCreativeTab(Main.ITEM_TAB);

        ModItems.ITEMS.add(this);
    }


    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        GameRules gamerules = worldIn.getGameRules();

        if (!worldIn.isRemote){

            playerIn.setGameType(GameType.ADVENTURE);
            playerIn.inventory.clear();

            class Timer implements Runnable {

                private Thread t;

                public Timer() {
                    FuncOperation.debugInfo(worldIn, "Thread Timer created.");
                }

                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    FuncOperation.debugInfo(worldIn, "Thread Timer ran.");
                    playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.READY_OFF));
                    if (!Reference.DEBUG_MODE){
                        gamerules.setOrCreateGameRule("sendCommandFeedback","false");
                    } else {
                        gamerules.setOrCreateGameRule("sendCommandFeedback","true");
                    }
                }

                public void start () {
                    FuncOperation.debugInfo(worldIn, "Thread Timer start.");
                    if (t == null) {
                        t = new Thread (this);
                        t.start ();
                    }
                }

            }
            Timer timer = new Timer();
            timer.start();
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
