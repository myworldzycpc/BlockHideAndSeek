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
    public static final Item FORCE_END = new ItemForceEnd();
    public static final Item ALIGN_TO_GRID = new ItemAlignToGrid();
    public static final Item GET_THE_NEAREST_HIDER_DISTANCE = new ItemGetTheNearestHiderDistance();
    public static final Item HICA_SENSOR = new ItemHicaSensor();
    public static final Item BACK_TO_PLAYER = new ItemBackToPlayer();
    public static final Item BE_HUNTER = new ItemBeHunter();
    public static final Item BE_BLOCK = new ItemBeBlock();
    public static final Item BE_SPECTATOR = new ItemBeSpectator();


}