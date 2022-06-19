package io.github.myworldzycpc.block_has.items;

import io.github.myworldzycpc.block_has.Main;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.IHasModel;
import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemDebug extends Item implements IHasModel {

    public ItemDebug() {
        setTranslationKey("debug");
        setRegistryName("debug");

        if (Reference.SHOW_DEBUG_ITEM) {
            setCreativeTab(Main.ITEM_TAB);
        }

        ModItems.ITEMS.add(this);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {

        if (worldIn.isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.fontRenderer.drawStringWithShadow("test", 0.0f, 0.0f, 0xFFFFFFFF);
        }

        TextComponentString textComponentString = new TextComponentString("hello world");
        Style style = new Style();
        style.setColor(TextFormatting.GOLD);
        textComponentString.setStyle(style);
        FuncOperation.messageCompound(playerIn, textComponentString);
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }

}
