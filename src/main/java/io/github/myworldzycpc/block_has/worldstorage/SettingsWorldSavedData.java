package io.github.myworldzycpc.block_has.worldstorage;

import io.github.myworldzycpc.block_has.util.BlockHasMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsWorldSavedData extends WorldSavedData {

    private Vec3d hallPosition = new Vec3d(0, 0, 0);
    private int timeForHunterToWait = 30;
    private int numberOfHunters = 1;
    private int toolCoolingDownTime = 10;
    private GameType defaultGameMode = GameType.CREATIVE;
    private GameType playingGameMode = GameType.CREATIVE;
    private List<BlockHasMap> blockHasMaps = new ArrayList<BlockHasMap>();
    private List<String> bannedBlocks = new ArrayList<String>(Arrays.asList(
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
            "minecraft:skull"
    ));
    private double hicaSensorSensitivity = 1;

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

    public void setHicaSensorSensitivity(double hicaSensorSensitivity) {
        this.hicaSensorSensitivity = hicaSensorSensitivity;
        this.markDirty();
    }

    public double getHicaSensorSensitivity() {
        return this.hicaSensorSensitivity;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = (NBTTagCompound) nbt.getTag("settings");
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = new NBTTagCompound();
        settingsCompound.setInteger("timeForHunterToWait", timeForHunterToWait);
        settingsCompound.setInteger("numberOfHunters", numberOfHunters);
        settingsCompound.setInteger("toolCoolingDownTime", toolCoolingDownTime);
        settingsCompound.setInteger("defaultGameMode", defaultGameMode.getID());
        settingsCompound.setInteger("playingGameMode", playingGameMode.getID());

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

        nbt.setTag("settings", settingsCompound);
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