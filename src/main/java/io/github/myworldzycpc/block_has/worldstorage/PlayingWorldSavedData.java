package io.github.myworldzycpc.block_has.worldstorage;

import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayingWorldSavedData extends WorldSavedData {

    private List<BlockHasPlayer> players = new ArrayList<BlockHasPlayer>();
    private String playing;
    private int hunterWaitingTime;
    private BlockHasMap blockHasMap;

    public List<BlockHasPlayer> getPlayers() {
        return players;
    }

    public String getPlaying() {
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

    public void setPlaying(String playing) {
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

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.players.clear();
        NBTTagCompound playingCompound = (NBTTagCompound) nbt.getTag("playing");
        if (playingCompound == null) {
            playingCompound = new NBTTagCompound();
        }
        NBTTagList playersList = (NBTTagList) playingCompound.getTag("players");
        for (int i = 0; i < playersList.tagCount(); i++) {
            players.add(new BlockHasPlayer((NBTTagCompound) playersList.get(i)));
        }
        this.playing = playingCompound.getString("playing");
        this.hunterWaitingTime = playingCompound.getInteger("hunterWaitingTime");
        this.blockHasMap = new BlockHasMap((NBTTagCompound) playingCompound.getTag("blockHasMap"));
        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound playingCompound = new NBTTagCompound();

        NBTTagList playersList = new NBTTagList();
        for (BlockHasPlayer blockHasPlayer : this.players) {
            playersList.appendTag(blockHasPlayer.toNBT());
        }
        playingCompound.setTag("players", playersList);

        playingCompound.setString("playing", this.playing);
        playingCompound.setInteger("hunterWaitingTime", this.hunterWaitingTime);
        playingCompound.setTag("blockHasMap", this.blockHasMap.toNBT());

        nbt.setTag("playing", playingCompound);
        return nbt;
    }


    public static PlayingWorldSavedData getGlobal(World world) {
        WorldSavedData data = world.getMapStorage().getOrLoadData(PlayingWorldSavedData.class, "BlockHasPlayingGlobal");
        if (data == null) {
            data = new PlayingWorldSavedData("BlockHasPlayingGlobal");
            world.getMapStorage().setData("BlockHasPlayingGlobal", data);
        }
        return (PlayingWorldSavedData) data;
    }

}
