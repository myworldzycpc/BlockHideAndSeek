package io.github.myworldzycpc.block_has.util;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class BlockHasPlayer {
    private UUID playerUUID;
    private Status status;
    private int toolCD;
    private boolean isReady;

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
        NULL(-1), HUNTER(0), HIDER(1), VISITOR(2);

        public int id;

        Status(int id) {
            this.id = id;
        }

        public static Status fromId(int id) {
            for (Status status : Status.values()) {
                if (status.id == id) {
                    return status;
                }
            }
            return null;
        }
    }
}
