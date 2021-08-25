package io.github.myworldzycpc.block_has.worldstorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class SettingsWorldSavedData extends WorldSavedData {

    private Vec3d hallPosition = new Vec3d(0, 0, 0);
    private int timeForHunterToWait = 30;

    public SettingsWorldSavedData(String name) {
        super(name);
    }

    public Vec3d getHallPosition() {
        return hallPosition;
    }

    public int getTimeForHunterToWait() {
        return timeForHunterToWait;
    }

    public void add(Vec3d hallPosition, int timeForHunterToWait) {
        this.hallPosition = hallPosition;
        this.timeForHunterToWait = timeForHunterToWait;
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = (NBTTagCompound) nbt.getTag("settings");
        if (settingsCompound == null) {
            settingsCompound = new NBTTagCompound();
        }
        NBTTagCompound hallPositionCompound = (NBTTagCompound) settingsCompound.getTag("hallPosition");
        hallPosition = new Vec3d(hallPositionCompound.getDouble("x"), hallPositionCompound.getDouble("y"), hallPositionCompound.getDouble("z"));
        timeForHunterToWait = settingsCompound.getInteger("timeForHunterToWait");
        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound settingsCompound = new NBTTagCompound();
        settingsCompound.setInteger("timeForHunterToWait", timeForHunterToWait);

        NBTTagCompound hallPositionCompound = new NBTTagCompound();
        hallPositionCompound.setDouble("x", hallPosition.x);
        hallPositionCompound.setDouble("y", hallPosition.y);
        hallPositionCompound.setDouble("z", hallPosition.z);
        settingsCompound.setTag("hallPosition", hallPositionCompound);
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