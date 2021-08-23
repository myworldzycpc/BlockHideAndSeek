package io.github.myworldzycpc.block_has;

import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.proxy.CommonProxy;
import io.github.myworldzycpc.block_has.tabs.ItemTab;
import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs ITEM_TAB = new ItemTab();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        new GuiElementLoader();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
//什么时候来帮我，wcs，我已哭了，不是快哭了，ca*25123
