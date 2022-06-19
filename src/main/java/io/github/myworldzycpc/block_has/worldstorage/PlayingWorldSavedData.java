package io.github.myworldzycpc.block_has.worldstorage;

import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.PlayingType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayingWorldSavedData extends WorldSavedData {

    public static final String KEY = "playing";

    private List<BlockHasPlayer> players = new ArrayList<>();
    private PlayingType playing = PlayingType.END_GAME;
    private int hunterWaitingTime;
    private BlockHasMap blockHasMap;
    private long startTime;

    public List<BlockHasPlayer> getPlayers() {
        return players;
    }

    public PlayingType getPlaying() {
        return this.playing;
    }

    public int getHunterWaitingTime() {
        return hunterWaitingTime;
    }

    public BlockHasMap getBlockHasMap() {
        return blockHasMap;
    }

    public void setPlayers(List<BlockHasPlayer> players) {
        this.players = players;
        this.markDirty();
    }

    public void setPlaying(PlayingType playing) {
        this.playing = playing;
        this.markDirty();
    }

    public void setHunterWaitingTime(int hunterWaitingTime) {
        this.hunterWaitingTime = hunterWaitingTime;
        this.markDirty();
    }

    public void setBlockHasMap(BlockHasMap blockHasMap) {
        this.blockHasMap = blockHasMap;
        this.markDirty();
    }

    public PlayingWorldSavedData(String name) {
        super(name);
    }

    public int size() {
        return players.size();
    }

    public BlockHasPlayer getPlayer(UUID uuid) {
        for (BlockHasPlayer blockHasPlayer : players) {
            if (blockHasPlayer.getPlayerUUID().toString().equals(uuid.toString())) {
                return blockHasPlayer;
            }
        }
        BlockHasPlayer blockHasPlayer = new BlockHasPlayer(uuid);
        players.add(blockHasPlayer);
        this.markDirty();
        return blockHasPlayer;
    }

    public BlockHasPlayer getPlayer(EntityPlayer thePlayer) {
        for (BlockHasPlayer blockHasPlayer : players) {
            if (blockHasPlayer.getPlayerUUID().toString().equals(thePlayer.getUniqueID().toString())) {
                return blockHasPlayer;
            }
        }
        BlockHasPlayer blockHasPlayer = new BlockHasPlayer(thePlayer.getUniqueID());
        players.add(blockHasPlayer);
        this.markDirty();
        return blockHasPlayer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.players.clear();
        NBTTagCompound playingCompound = (NBTTagCompound) nbt.getTag(KEY);
        if (playingCompound == null) {
            playingCompound = new NBTTagCompound();
        }
        NBTTagList playersList = (NBTTagList) playingCompound.getTag("players");
        for (int i = 0; i < playersList.tagCount(); i++) {
            players.add(new BlockHasPlayer((NBTTagCompound) playersList.get(i)));
        }
        this.playing = PlayingType.fromId(playingCompound.getInteger("playing"));
        this.hunterWaitingTime = playingCompound.getInteger("hunterWaitingTime");
        this.startTime = playingCompound.getLong("startTime");
        if (playingCompound.hasKey("blockHasMap")) {
            this.blockHasMap = new BlockHasMap((NBTTagCompound) playingCompound.getTag("blockHasMap"));
        }

        this.markDirty();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagCompound playingCompound = new NBTTagCompound();

        NBTTagList playersList = new NBTTagList();
        for (BlockHasPlayer blockHasPlayer : this.players) {
            playersList.appendTag(blockHasPlayer.toNBT());
        }
        playingCompound.setTag("players", playersList);

        playingCompound.setInteger("playing", this.playing.id);
        playingCompound.setInteger("hunterWaitingTime", this.hunterWaitingTime);
        playingCompound.setLong("startTime", this.startTime);
        if (this.blockHasMap != null) {
            playingCompound.setTag("blockHasMap", this.blockHasMap.toNBT());
        }

        nbt.setTag(KEY, playingCompound);
        return nbt;
    }

    public static PlayingWorldSavedData getGlobal(World world) {
        WorldSavedData data;
        if (world.getMapStorage() == null) {
            data = new PlayingWorldSavedData("BlockHasPlayingGlobal");
            world.getMapStorage().setData("BlockHasPlayingGlobal", data);
        } else {
            data = world.getMapStorage().getOrLoadData(PlayingWorldSavedData.class, "BlockHasPlayingGlobal");
            if (data == null) {
                data = new PlayingWorldSavedData("BlockHasPlayingGlobal");
                world.getMapStorage().setData("BlockHasPlayingGlobal", data);
            }
        }

        return (PlayingWorldSavedData) data;
    }

}
