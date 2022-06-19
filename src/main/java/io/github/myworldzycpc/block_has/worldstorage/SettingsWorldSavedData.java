package io.github.myworldzycpc.block_has.worldstorage;

import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.BlockHasPlayingMode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsWorldSavedData extends WorldSavedData {

    public static final String KEY = "settings";

    private Vec3d hallPosition = new Vec3d(0, 0, 0);
    private int timeForHunterToWait = 30;
    private int numberOfHunters = 1;
    private int toolCoolingDownTime = 10;
    private GameType defaultGameMode = GameType.CREATIVE;
    private GameType playingGameMode = GameType.CREATIVE;
    private List<BlockHasMap> blockHasMaps = new ArrayList<>();
    private final List<String> bannedBlocks = new ArrayList<>(Arrays.asList(
            "minecraft:tallgrass",
            "minecraft:double_plant",
            "minecraft:fire",
            "minecraft:barrier",
            "minecraft:water",
            "minecraft:lava",
            "minecraft:redstone_wire",
            "minecraft:standing_sign",
            "minecraft:wall_sign",
            "minecraft:standing_banner",
            "minecraft:wall_banner",
            "minecraft:skull",
            "minecraft:cobblestone_wall"
    ));
    private double hicaSensorSensitivity = 1;
    private boolean debugMode = true;
    private BlockHasPlayingMode playingMode = BlockHasPlayingMode.RANDOM;
    private boolean showHUD = true;
    private boolean antiCheating = true;
    // add button step: add field (includes getter and setter)

    public SettingsWorldSavedData(String name) {
        super(name);
    }

    public Vec3d getHallPosition() {
        return hallPosition;
    }

    public int getTimeForHunterToWait() {
        return timeForHunterToWait;
    }

    public int getNumberOfHunters() {
        return numberOfHunters;
    }

    public int getToolCoolingDownTime() {
        return toolCoolingDownTime;
    }

    public GameType getDefaultGameMode() {
        return defaultGameMode;
    }

    public GameType getPlayingGameMode() {
        return playingGameMode;
    }

    public List<BlockHasMap> getBlockHasMaps() {
        return blockHasMaps;
    }

    public List<BlockHasMap> getEnabledBlockHasMaps() {
        List<BlockHasMap> enabledBlockHasMaps = new ArrayList<>();
        for (BlockHasMap blockHasMap : blockHasMaps) {
            if (!blockHasMap.forbidden) {
                enabledBlockHasMaps.add(blockHasMap);
            }
        }
        return enabledBlockHasMaps;
    }

    // todo: divide it into lines.
    public void addSettings(Vec3d hallPosition, int timeForHunterToWait, int numberOfHunters, int toolCoolingDownTime,
                            GameType defaultGameMode, GameType playingGameMode) {
        this.hallPosition = hallPosition;
        this.timeForHunterToWait = timeForHunterToWait;
        this.numberOfHunters = numberOfHunters;
        this.toolCoolingDownTime = toolCoolingDownTime;
        this.defaultGameMode = defaultGameMode;
        this.playingGameMode = playingGameMode;
        this.markDirty();
    }

    public void setBlockHasMaps(List<BlockHasMap> blockHasMaps) {
        this.blockHasMaps = blockHasMaps;
        this.markDirty();
    }

    public void setBlockHasMap(BlockHasMap blockHasMap, int index) {
        this.blockHasMaps.set(index, blockHasMap);
        this.markDirty();
    }

    public void addBlockHasMap(BlockHasMap blockHasMap) {
        this.blockHasMaps.add(blockHasMap);
        this.markDirty();
    }

    public void removeBlockHasMap(int index) {
        this.blockHasMaps.remove(index);
        this.markDirty();
    }

    public List<String> getBannedBlocks() {
        return this.bannedBlocks;

    }

    public void setBannedBlock(int index, String id) {
        this.bannedBlocks.set(index, id);
        this.markDirty();
    }

    public void addBannedBlock(String id) {
        this.bannedBlocks.add(id);
        this.markDirty();
    }

    public void removeBannedBlock(int index) {
        this.bannedBlocks.remove(index);
        this.markDirty();
    }

    public void setHicaSensorSensitivity(double hicaSensorSensitivity) {
        this.hicaSensorSensitivity = hicaSensorSensitivity;
        this.markDirty();
    }

    public double getHicaSensorSensitivity() {
        return this.hicaSensorSensitivity;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        this.markDirty();
    }

    public BlockHasPlayingMode getPlayingMode() {
        return playingMode;
    }

    public void setPlayingMode(BlockHasPlayingMode playingMode) {
        this.playingMode = playingMode;
        this.markDirty();
    }

    public boolean isShowHUD() {
        return showHUD;
    }

    public void setShowHUD(boolean showHUD) {
        this.showHUD = showHUD;
        this.markDirty();
    }

    public boolean isAntiCheating() {
        return antiCheating;
    }

    public void setAntiCheating(boolean antiCheating) {
        this.antiCheating = antiCheating;
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = (NBTTagCompound) nbt.getTag(KEY);
        if (settingsCompound == null) {
            settingsCompound = new NBTTagCompound();
        }
        NBTTagCompound hallPositionCompound = (NBTTagCompound) settingsCompound.getTag("hallPosition");
        hallPosition = new Vec3d(hallPositionCompound.getDouble("x"), hallPositionCompound.getDouble("y"), hallPositionCompound.getDouble("z"));
        this.timeForHunterToWait = settingsCompound.getInteger("timeForHunterToWait");
        this.numberOfHunters = settingsCompound.getInteger("numberOfHunters");
        this.toolCoolingDownTime = settingsCompound.getInteger("toolCoolingDownTime");
        this.defaultGameMode = GameType.getByID(settingsCompound.getInteger("defaultGameMode"));
        this.playingGameMode = GameType.getByID(settingsCompound.getInteger("playingGameMode"));
        this.debugMode = settingsCompound.getBoolean("debugMode");
        this.playingMode = BlockHasPlayingMode.fromId(settingsCompound.getInteger("playingMode"));
        this.showHUD = settingsCompound.getBoolean("showHUD");
        this.antiCheating = settingsCompound.getBoolean("antiCheating");
        // add button step: add read

        this.blockHasMaps.clear();
        NBTTagList blockHasMapsList = (NBTTagList) settingsCompound.getTag("blockHasMaps");
        for (int i = 0; i < blockHasMapsList.tagCount(); i++) {
            NBTTagCompound blockHasMapCompound = (NBTTagCompound) blockHasMapsList.get(i);
            this.blockHasMaps.add(new BlockHasMap(blockHasMapCompound));
        }

        this.bannedBlocks.clear();
        NBTTagList bannedBlocksList = (NBTTagList) settingsCompound.getTag("bannedBlocks");
        for (int i = 0; i < bannedBlocksList.tagCount(); i++) {
            NBTTagString bannedBlockTagString = (NBTTagString) bannedBlocksList.get(i);
            this.bannedBlocks.add(bannedBlockTagString.getString());
        }

        this.hicaSensorSensitivity = settingsCompound.getDouble("hicaSensorSensitivity");

        this.markDirty();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = new NBTTagCompound();
        settingsCompound.setInteger("timeForHunterToWait", timeForHunterToWait);
        settingsCompound.setInteger("numberOfHunters", numberOfHunters);
        settingsCompound.setInteger("toolCoolingDownTime", toolCoolingDownTime);
        settingsCompound.setInteger("defaultGameMode", defaultGameMode.getID());
        settingsCompound.setInteger("playingGameMode", playingGameMode.getID());
        settingsCompound.setBoolean("debugMode", debugMode);
        settingsCompound.setInteger("playingMode", playingMode.id);
        settingsCompound.setBoolean("showHUD", showHUD);
        settingsCompound.setBoolean("antiCheating", antiCheating);
        // add button step: add NBT

        NBTTagCompound hallPositionCompound = new NBTTagCompound();
        hallPositionCompound.setDouble("x", hallPosition.x);
        hallPositionCompound.setDouble("y", hallPosition.y);
        hallPositionCompound.setDouble("z", hallPosition.z);
        settingsCompound.setTag("hallPosition", hallPositionCompound);

        NBTTagList blockHasMapsList = new NBTTagList();
        for (BlockHasMap blockHasMap : this.blockHasMaps) {
            blockHasMapsList.appendTag(blockHasMap.toNBT());
        }
        settingsCompound.setTag("blockHasMaps", blockHasMapsList);

        NBTTagList bannedBlocksList = new NBTTagList();
        for (String bannedBlock : this.bannedBlocks) {
            bannedBlocksList.appendTag(new NBTTagString(bannedBlock));
        }
        settingsCompound.setTag("bannedBlocks", bannedBlocksList);

        settingsCompound.setDouble("hicaSensorSensitivity", this.hicaSensorSensitivity);

        nbt.setTag(KEY, settingsCompound);
        return nbt;
    }

    public static SettingsWorldSavedData getGlobal(World world) {
        WorldSavedData data = world.getMapStorage().getOrLoadData(SettingsWorldSavedData.class, "BlockHasSettingsGlobal");
        if (data == null) {
            data = new SettingsWorldSavedData("BlockHasSettingsGlobal");
            world.getMapStorage().setData("BlockHasSettingsGlobal", data);
            world.getMapStorage().saveAllData();
        }
        return (SettingsWorldSavedData) data;
    }


}