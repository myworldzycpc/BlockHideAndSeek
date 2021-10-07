package io.github.myworldzycpc.block_has.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
        if (meta == 0) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
        } else {
            String location = String.format("%s/%d", item.getRegistryName().toString(), meta);
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, id));
        }

    }


}
