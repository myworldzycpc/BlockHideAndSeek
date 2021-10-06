package io.github.myworldzycpc.block_has.func;

import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.Translation;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.*;

public class FuncFragment {

    public static void endGame(World worldIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
        blockHasPlayingGlobal.setPlaying("endGame");

        for (EntityPlayer player : worldIn.playerEntities) {
            player.setGameType(blockHasSettingsGlobal.getDefaultGameMode());
            Vec3d hallPosition = blockHasSettingsGlobal.getHallPosition();
            player.inventory.clear();
            FuncOperation.teleportPlayer(player, hallPosition);
            UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
            FuncOperation.executeCommand(player, String.format("morph %s", uuid.toString()));
        }
        blockHasPlayingGlobal.setPlayers(new ArrayList<BlockHasPlayer>());
        GameRules gamerules = worldIn.getGameRules();
        gamerules.setOrCreateGameRule("sendCommandFeedback", "true");

    }

    public static void startGame(World worldIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

        if (worldIn.playerEntities.size() >= 2) {
            if (worldIn.playerEntities.size() > blockHasSettingsGlobal.getNumberOfHunters()) {
                blockHasPlayingGlobal.setPlaying("playing");
                List<BlockHasMap> blockHasMaps = blockHasSettingsGlobal.getBlockHasMaps();
                Random rand = new Random();
                int randomIndex = rand.nextInt(blockHasMaps.size());
                BlockHasMap blockHasMap = blockHasMaps.get(randomIndex);
                blockHasPlayingGlobal.setBlockHasMap(blockHasMap);
                for (EntityPlayer player : worldIn.playerEntities) {
                    blockHasPlayingGlobal.getPlayer(player).setStatus(BlockHasPlayer.Status.HIDER);
                }
                List<EntityPlayer> hunters = FuncAlgorithms.extract(blockHasSettingsGlobal.getNumberOfHunters(), worldIn.playerEntities);
                for (EntityPlayer player : hunters) {
                    blockHasPlayingGlobal.getPlayer(player).setStatus(BlockHasPlayer.Status.HUNTER);
                    FuncOperation.messageTranslation(player, "block_has.chat.you_are_hunter", blockHasSettingsGlobal.getTimeForHunterToWait());
                }
                for (EntityPlayer player : worldIn.playerEntities) {
                    player.inventory.clear();

                    if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HIDER) {
                        FuncOperation.teleportPlayer(player, blockHasMap.spawnPoint);
                        FuncOperation.title(player, blockHasMap.mapName);
                        FuncOperation.messageTranslation(player, "block_has.chat.you_are_block");
                        for (Item item : Arrays.asList(ModItems.BECOME_SELECTED_BLOCK, ModItems.ALIGN_TO_GRID)) {
                            player.inventory.addItemStackToInventory(new ItemStack(item));
                        }
                    }
                    player.inventory.setInventorySlotContents(8, new ItemStack(ModItems.FORCE_END));
//                    player.inventory.addItemStackToInventory(new ItemStack(ModItems.FORCE_END));
                }
                blockHasPlayingGlobal.setHunterWaitingTime(blockHasSettingsGlobal.getTimeForHunterToWait());
                startHuntersWaiting(worldIn, blockHasMap);
            } else {
                FuncOperation.messageAllTranslation(worldIn, "block_has.chat.player_only_one");
                endGame(worldIn);
            }
        } else {
            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.not_enough_hunters");
            endGame(worldIn);
        }
    }

    public static void startHuntersWaiting(World worldIn, BlockHasMap blockHasMap) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

        (new Timer()).schedule(new TimerTask() {
            public void run() {
                FuncOperation.debugInfo(worldIn, "Thread Timer in startHuntersWaiting() ran.");
                if (!blockHasPlayingGlobal.getPlaying().equals("endGame")) {
                    if (blockHasPlayingGlobal.getHunterWaitingTime() > 0) {
                        blockHasPlayingGlobal.setHunterWaitingTime(blockHasPlayingGlobal.getHunterWaitingTime() - 1);
                        FuncOperation.actionbarAll(worldIn, String.valueOf(blockHasPlayingGlobal.getHunterWaitingTime()));
                        startHuntersWaiting(worldIn, blockHasMap);
                    } else {
                        for (EntityPlayer player : worldIn.playerEntities) {
                            if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HUNTER) {
                                FuncOperation.teleportPlayer(player, blockHasMap.spawnPoint);
                                FuncOperation.title(player, blockHasMap.mapName);
                                FuncOperation.messageTranslation(player, "block_has.chat.time_out");
                            } else {
                                FuncOperation.messageTranslation(player, "block_has.chat.hunter_is_coming");
                            }
                        }
                    }
                }
            }
        }, 1000);
    }

    public static void startToolCoolingDown(World worldIn, EntityPlayer playerIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
        BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(playerIn);
        (new Timer()).schedule(new TimerTask() {
            public void run() {
                FuncOperation.debugInfo(worldIn, "Thread Timer in startToolCoolingDown() ran.");
                if (!blockHasPlayingGlobal.getPlaying().equals("endGame")) {
                    if (blockHasPlayer.getToolCD() > 0) {
                        blockHasPlayer.setToolCD(blockHasPlayer.getToolCD() - 1);
                        FuncOperation.actionbarTranslation(playerIn, "block_has.chat.get_the_nearest_hider_distance.cooling_down", blockHasPlayer.getToolCD());
                        startToolCoolingDown(worldIn, playerIn);
                    } else {
                        FuncOperation.messageTranslation(playerIn, "block_has.chat.get_the_nearest_hider_distance.regain");
                        playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.GET_THE_NEAREST_HIDER_DISTANCE));
                    }
                }
            }
        }, 1000);
    }

    public static void detectForReady(World worldIn) {
        PlayingWorldSavedData BlockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        boolean allReady = true;
        int playerCountOfReady = 0;
        for (EntityPlayer player : worldIn.playerEntities) {
            if (!BlockHasPlayingGlobal.getPlayer(player).isReady()) {
                allReady = false;
            } else {
                playerCountOfReady++;
            }
        }
        if (allReady) {
            FuncOperation.messageAll(worldIn, String.format("%s (%d/%d)", Translation.playerAllReady, playerCountOfReady, worldIn.playerEntities.size()));
            startGame(worldIn);
        } else {
            FuncOperation.messageAll(worldIn, String.format("%s (%d/%d)", Translation.playerReady, playerCountOfReady, worldIn.playerEntities.size()));
        }
    }

}
