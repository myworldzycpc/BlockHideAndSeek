package io.github.myworldzycpc.block_has.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class BlockHasMap {

    public Vec3d spawnPoint;

    public String mapName;

    public BlockHasMap(Vec3d spawnPoint, String mapName) {
        this.spawnPoint = spawnPoint;
        this.mapName = mapName;
    }

    public BlockHasMap(NBTTagCompound fromNBT) {
        this(new Vec3d(
                fromNBT.getCompoundTag("spawnPoint").getDouble("x"),
                fromNBT.getCompoundTag("spawnPoint").getDouble("y"),
                fromNBT.getCompoundTag("spawnPoint").getDouble("z")
        ), fromNBT.getString("mapName"));
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound rootCompound = new NBTTagCompound();

        NBTTagCompound spawnPointCompound = new NBTTagCompound();
        spawnPointCompound.setDouble("x", this.spawnPoint.x);
        spawnPointCompound.setDouble("y", this.spawnPoint.y);
        spawnPointCompound.setDouble("z", this.spawnPoint.z);
        rootCompound.setTag("spawnPoint", spawnPointCompound);

        rootCompound.setString("mapName", this.mapName);

        return rootCompound;
    }

}

