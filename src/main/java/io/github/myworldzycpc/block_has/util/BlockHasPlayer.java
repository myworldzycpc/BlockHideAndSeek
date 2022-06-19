package io.github.myworldzycpc.block_has.util;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BlockHasPlayer {
    private UUID playerUUID;
    private Status status;
    private int toolCD;
    private boolean isReady;
    private List<Vec3d> oldPositions = new ArrayList<Vec3d>();
    private double lastMinDistance;
    private double differenceMinDistance;

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayer(UUID player) {
        this.playerUUID = player;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getToolCD() {
        return toolCD;
    }

    public void setToolCD(int toolCD) {
        this.toolCD = toolCD;
    }

    public List<Vec3d> getOldPositions() {
        return this.oldPositions;
    }

    public void setOldPositions(List<Vec3d> oldPositions) {
        this.oldPositions = oldPositions;
    }

    public void addOldPosition(Vec3d position) {
        oldPositions.add(position);
        if (oldPositions.size() > 10) {
            oldPositions.remove(0);
        }
    }

    public Vec3d getAverageOldPosition() {
        List<Double> listX = this.oldPositions.stream().map(i -> i.x).collect(Collectors.toList());
        double averageX = FuncAlgorithms.average(listX);
        List<Double> listY = this.oldPositions.stream().map(i -> i.y).collect(Collectors.toList());
        double averageY = FuncAlgorithms.average(listY);
        List<Double> listZ = this.oldPositions.stream().map(i -> i.z).collect(Collectors.toList());
        double averageZ = FuncAlgorithms.average(listZ);
        return new Vec3d(averageX, averageY, averageZ);
    }

    public double getLastMinDistance() {
        return lastMinDistance;
    }

    public void setLastMinDistance(double lastMinDistance) {
        this.lastMinDistance = lastMinDistance;
    }

    public void setNewMinDistance(double newMinDistance) {
        this.differenceMinDistance = this.lastMinDistance - newMinDistance;
        this.lastMinDistance = newMinDistance;
    }

    public double getDifferenceMinDistance() {
        return this.differenceMinDistance;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public BlockHasPlayer(UUID player) {
        this.playerUUID = player;
        this.status = Status.NULL;
        this.toolCD = -1;
        this.isReady = false;
        this.lastMinDistance = 0;
        this.oldPositions = new ArrayList<Vec3d>();
        this.differenceMinDistance = 0;
    }

    public BlockHasPlayer(NBTTagCompound fromNBT) {
        this.playerUUID = UUID.fromString(fromNBT.getString("player"));
        this.status = Status.fromId(fromNBT.getInteger("status"));
        this.toolCD = fromNBT.getInteger("toolCD");
        this.isReady = fromNBT.getBoolean("isReady");
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound rootCompound = new NBTTagCompound();
        rootCompound.setString("player", playerUUID.toString());
        rootCompound.setInteger("status", status.id);
        rootCompound.setInteger("toolCD", toolCD);
        rootCompound.setBoolean("isReady", isReady);
        return rootCompound;
    }


    public enum Status {
        NULL(-1, "block_has.status.null"),
        HUNTER(0, "block_has.status.hunter"),
        HIDER(1, "block_has.status.hider"),
        VISITOR(2, "block_has.status.visitor");

        public int id;
        public String translationKey;

        Status(int id, String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public static Status fromId(int id) {
            for (Status status : Status.values()) {
                if (status.id == id) {
                    return status;
                }
            }
            return null;
        }

        public String getDisplayName() {
            return I18n.format(this.translationKey);
        }
    }
}
