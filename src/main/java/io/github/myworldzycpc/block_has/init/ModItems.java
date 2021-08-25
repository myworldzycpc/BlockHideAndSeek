package io.github.myworldzycpc.block_has.init;

import io.github.myworldzycpc.block_has.items.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item BLOCK_HAS = new ItemBlockHas();
    public static final Item READY_ON = new ItemReadyOn();
    public static final Item READY_OFF = new ItemReadyOff();
    public static final Item DEBUG = new ItemDebug();
    public static final Item BECOME_SELECTED_BLOCK = new ItemBecomeSelectedBlock();
    public static final Item SETTINGS = new ItemSettings();

}