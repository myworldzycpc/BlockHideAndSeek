package io.github.myworldzycpc.block_has.worldstorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PositionWorldSavedData extends WorldSavedData {
    private List<Vec3d> positions = new ArrayList<Vec3d>();
    private List<UUID> players = new ArrayList<UUID>();

    public PositionWorldSavedData(String name) {
        super(name);
    }

    public int size() {
        return players.size();
    }

    public Vec3d getPosition(int index) {
        return positions.get(index);
    }

    public UUID getPlayerUUID(int index) {
        return players.get(index);
    }

    public void add(Vec3d position, UUID player) {
        positions.add(position);
        players.add(player);
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        positions.clear();
        players.clear();
        NBTTagList list = (NBTTagList) nbt.getTag("positions");
        if (list == null) {
            list = new NBTTagList();
        }
        for (int i = list.tagCount() - 1; i >= 0; --i) {
            NBTTagCompound compound = (NBTTagCompound) list.get(i);
            positions.add(new Vec3d(compound.getDouble("x"), compound.getDouble("y"), compound.getDouble("z")));
            players.add(UUID.fromString(compound.getString("player")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (int i = players.size() - 1; i >= 0; --i) {
            Vec3d position = positions.get(i);
            UUID player = players.get(i);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setDouble("x", position.x);
            compound.setDouble("y", position.y);
            compound.setDouble("z", position.z);
            compound.setString("player", player.toString());
            list.appendTag(compound);
        }
        nbt.setTag("positions", list);
        return nbt;
    }

    public static PositionWorldSavedData get(World world) {
        WorldSavedData data = world.getPerWorldStorage().getOrLoadData(PositionWorldSavedData.class, "FMLTutorPositions");
        if (data == null) {
            data = new PositionWorldSavedData("FMLTutorPositions");
            world.getPerWorldStorage().setData("FMLTutorPositions", data);
        }
        return (PositionWorldSavedData) data;
    }

    public static PositionWorldSavedData getGlobal(World world) {
        WorldSavedData data = world.getMapStorage().getOrLoadData(PositionWorldSavedData.class, "FMLTutorPositionsGlobal");
        if (data == null) {
            data = new PositionWorldSavedData("FMLTutorPositionsGlobal");
            world.getMapStorage().setData("FMLTutorPositionsGlobal", data);
        }
        return (PositionWorldSavedData) data;
    }

}