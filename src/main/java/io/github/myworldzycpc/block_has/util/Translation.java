package io.github.myworldzycpc.block_has.util;

import net.minecraft.client.resources.I18n;

public class Translation {

    public static String titleGui;
    public static String titleTimeForHunterToWait;
    public static String titleNumberOfHunters;
    public static String titleToolCoolingDownTime;
    public static String titleHallPosition;

    public static String defaultGameMode;
    public static String playingGameMode;

    public static String addMap;
    public static String removeMap;
    public static String mapName;
    public static String mapSpawnPoint;
    public static String selectMapFirst;
    public static String getToThere;
    public static String unnamed;

    public static String survivalMode;
    public static String creativeMode;
    public static String adventureMode;
    public static String spectatorMode;

    public static String playerReady;
    public static String playerAllReady;

    public static void update() {

        titleGui = I18n.format("block_has.container.settings");
        titleTimeForHunterToWait = I18n.format("block_has.container.settings.time_for_hunter_to_wait");
        titleNumberOfHunters = I18n.format("block_has.container.settings.number_of_hunters");
        titleToolCoolingDownTime = I18n.format("block_has.container.settings.tool_cooling_down_time");
        titleHallPosition = I18n.format("block_has.container.settings.hall_position");

        defaultGameMode = I18n.format("block_has.container.settings.default_game_mode");
        playingGameMode = I18n.format("block_has.container.settings.playing_game_mode");

        addMap = I18n.format("block_has.container.settings.add_map");
        removeMap = I18n.format("block_has.container.settings.remove_map");
        mapName = I18n.format("block_has.container.settings.map_name");
        mapSpawnPoint = I18n.format("block_has.container.settings.map_spawn_point");
        selectMapFirst = I18n.format("block_has.container.settings.select_map_first");
        getToThere = I18n.format("block_has.container.settings.get_to_there");
        unnamed = I18n.format("block_has.container.settings.unnamed");

        survivalMode = I18n.format("gameMode.survival");
        creativeMode = I18n.format("gameMode.creative");
        adventureMode = I18n.format("gameMode.adventure");
        spectatorMode = I18n.format("gameMode.spectator");

        playerReady = I18n.format("block_has.chat.player_ready");
        playerAllReady = I18n.format("block_has.chat.player_all_ready");
    }

}
