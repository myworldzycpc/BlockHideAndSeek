package io.github.myworldzycpc.block_has.func;

import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.BlockHasPlayingMode;
import io.github.myworldzycpc.block_has.util.PlayingType;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

import java.util.*;

public class FuncFragment {

    public static void endGame(World worldIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);
        blockHasPlayingGlobal.setPlaying(PlayingType.END_GAME);
        FuncOperation.updatePlayingData(worldIn);

        for (EntityPlayer player : worldIn.playerEntities) {
            player.setGameType(blockHasSettingsGlobal.getDefaultGameMode());
            Vec3d hallPosition = blockHasSettingsGlobal.getHallPosition();
            player.inventory.clear();
            FuncOperation.teleportPlayer(player, hallPosition);
            UUID uuid = EntityPlayer.getUUID(player.getGameProfile());
            FuncOperation.executeCommand(player, String.format("morph %s", uuid.toString()));
            player.setSpawnPoint(new BlockPos(hallPosition), true);
        }
        blockHasPlayingGlobal.setPlayers(new ArrayList<>());
        FuncOperation.updatePlayingData(worldIn);
        GameRules gamerules = worldIn.getGameRules();
        gamerules.setOrCreateGameRule("sendCommandFeedback", "true");

    }

    public static void startGame(World worldIn) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

        if (worldIn.playerEntities.size() >= 2) {
            if (worldIn.playerEntities.size() > blockHasSettingsGlobal.getNumberOfHunters()) {
                blockHasPlayingGlobal.setPlaying(PlayingType.PLAYING);
                blockHasPlayingGlobal.setStartTime(new Date().getTime());
                FuncOperation.updatePlayingData(worldIn);

                List<BlockHasMap> blockHasMaps = blockHasSettingsGlobal.getEnabledBlockHasMaps();
                Random rand = new Random();
                int randomIndex = rand.nextInt(blockHasMaps.size());
                BlockHasMap blockHasMap = blockHasMaps.get(randomIndex);
                blockHasPlayingGlobal.setBlockHasMap(blockHasMap);
                FuncOperation.updatePlayingData(worldIn);
                List<EntityPlayer> hunters = new ArrayList<>();
                if (blockHasSettingsGlobal.getPlayingMode() == BlockHasPlayingMode.RANDOM) {
                    for (EntityPlayer player : worldIn.playerEntities) {
                        blockHasPlayingGlobal.getPlayer(player).setStatus(BlockHasPlayer.Status.HIDER);
                        FuncOperation.updatePlayingData(worldIn);
                        player.setSpawnPoint(new BlockPos(blockHasMap.spawnPoint), true);

                    }
                    hunters = FuncAlgorithms.extract(blockHasSettingsGlobal.getNumberOfHunters(), worldIn.playerEntities);
                } else if (blockHasSettingsGlobal.getPlayingMode() == BlockHasPlayingMode.FREE) {
                    int playerCountOfHunter = 0;
                    int playerCountOfHider = 0;
                    for (EntityPlayer player : worldIn.playerEntities) {
                        BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                        if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HUNTER) {
                            playerCountOfHunter++;
                        } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HIDER) {
                            playerCountOfHider++;
                        }
                    }
                    if (playerCountOfHunter == 0 && playerCountOfHider == 0) {
                        FuncOperation.messageAllTranslation(worldIn, "block_has.chat.no_hunter");
                        endGame(worldIn);
                        return;
                    } else if (playerCountOfHunter == 0) {
                        FuncOperation.messageAllTranslation(worldIn, "block_has.chat.no_hunter");
                        endGame(worldIn);
                        return;
                    } else if (playerCountOfHider == 0) {
                        FuncOperation.messageAllTranslation(worldIn, "block_has.chat.no_hider");
                        endGame(worldIn);
                        return;
                    }
                    for (EntityPlayer player : worldIn.playerEntities) {
                        player.setSpawnPoint(new BlockPos(blockHasMap.spawnPoint), true);
                        if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HUNTER) {
                            hunters.add(player);
                        }
                    }
                }

                for (EntityPlayer player : hunters) {
                    blockHasPlayingGlobal.getPlayer(player).setStatus(BlockHasPlayer.Status.HUNTER);
                    FuncOperation.updatePlayingData(worldIn);
                    FuncOperation.messageTranslation(player, "block_has.chat.you_are_hunter", blockHasSettingsGlobal.getTimeForHunterToWait());
                }
                for (EntityPlayer player : worldIn.playerEntities) {
                    player.inventory.clear();
                    BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                    if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HIDER) {
                        FuncOperation.teleportPlayer(player, blockHasMap.spawnPoint);
                        FuncOperation.title(player, blockHasMap.mapName);
                        FuncOperation.messageTranslation(player, "block_has.chat.you_are_block");
                        for (Item item : Arrays.asList(ModItems.BECOME_SELECTED_BLOCK, ModItems.ALIGN_TO_GRID, ModItems.BACK_TO_PLAYER)) {
                            player.inventory.addItemStackToInventory(new ItemStack(item));
                        }
                    } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.VISITOR) {
                        FuncOperation.teleportPlayer(player, blockHasMap.spawnPoint);
                        FuncOperation.title(player, blockHasMap.mapName);
                        player.setGameType(GameType.SPECTATOR);
                    }
                    player.inventory.setInventorySlotContents(8, new ItemStack(ModItems.FORCE_END));
//                    player.inventory.addItemStackToInventory(new ItemStack(ModItems.FORCE_END));
                }
                blockHasPlayingGlobal.setHunterWaitingTime(blockHasSettingsGlobal.getTimeForHunterToWait());
                FuncOperation.updatePlayingData(worldIn);
                startHuntersWaiting(worldIn, blockHasMap);
            } else {
                FuncOperation.messageAllTranslation(worldIn, "block_has.chat.not_enough_hunters");
                endGame(worldIn);
            }
        } else {
            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.player_only_one");
            endGame(worldIn);
        }
    }

    public static void startHuntersWaiting(World worldIn, BlockHasMap blockHasMap) {
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

        (new Timer()).schedule(new TimerTask() {
            public void run() {
                FuncOperation.debugInfo(worldIn, "Thread Timer in startHuntersWaiting() ran.");
                if (blockHasPlayingGlobal.getPlaying() != PlayingType.END_GAME) {
                    if (blockHasPlayingGlobal.getHunterWaitingTime() > 0) {
                        blockHasPlayingGlobal.setHunterWaitingTime(blockHasPlayingGlobal.getHunterWaitingTime() - 1);
                        FuncOperation.updatePlayingData(worldIn);

                        FuncOperation.actionbarAll(worldIn, String.valueOf(blockHasPlayingGlobal.getHunterWaitingTime() + 1));
                        if (blockHasPlayingGlobal.getHunterWaitingTime() + 1 == 30) {
                            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.game_will_start.colored", I18n.format("block_has.chat.seconds.green.colored", blockHasPlayingGlobal.getHunterWaitingTime() + 1));
                        } else if (blockHasPlayingGlobal.getHunterWaitingTime() + 1 == 15) {
                            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.game_will_start.colored", I18n.format("block_has.chat.seconds.orange.colored", blockHasPlayingGlobal.getHunterWaitingTime() + 1));
                        } else if (blockHasPlayingGlobal.getHunterWaitingTime() + 1 <= 5) {
                            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.game_will_start.colored", I18n.format("block_has.chat.seconds.red.colored", blockHasPlayingGlobal.getHunterWaitingTime() + 1));
                        }
                        startHuntersWaiting(worldIn, blockHasMap);
                    } else {
                        for (EntityPlayer player : worldIn.playerEntities) {
                            if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HUNTER) {
                                FuncOperation.teleportPlayer(player, blockHasMap.spawnPoint);
                                FuncOperation.title(player, blockHasMap.mapName);
                                FuncOperation.messageTranslation(player, "block_has.chat.time_out");
                                for (Item item : Arrays.asList(ModItems.GET_THE_NEAREST_HIDER_DISTANCE, ModItems.HICA_SENSOR)) {
                                    player.inventory.addItemStackToInventory(new ItemStack(item));
                                }
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
                if (blockHasPlayingGlobal.getPlaying() != PlayingType.END_GAME) {
                    if (blockHasPlayer.getToolCD() > 0) {
                        blockHasPlayer.setToolCD(blockHasPlayer.getToolCD() - 1);
                        blockHasPlayingGlobal.markDirty();
                        FuncOperation.updatePlayingData(worldIn);

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
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData settingsWorldSavedData = SettingsWorldSavedData.getGlobal(worldIn);
        boolean allReady = true;
        int playerCountOfReady = 0;

        if (settingsWorldSavedData.getPlayingMode() == BlockHasPlayingMode.RANDOM) {
            for (EntityPlayer player : worldIn.playerEntities) {
                if (!blockHasPlayingGlobal.getPlayer(player).isReady()) {
                    allReady = false;
                } else {
                    playerCountOfReady++;
                }
            }
        } else if (settingsWorldSavedData.getPlayingMode() == BlockHasPlayingMode.FREE) {
            int playerCountOfHunter = 0;
            int playerCountOfHider = 0;
            for (EntityPlayer player : worldIn.playerEntities) {
                BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                FuncOperation.debugInfo(worldIn, String.format("detect for player: %s %s", player.getDisplayNameString(), blockHasPlayer.getStatus().getDisplayName()));
                if (blockHasPlayer.getStatus() != BlockHasPlayer.Status.NULL) {
                    playerCountOfReady++;
                    if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HUNTER) {
                        playerCountOfHunter++;
                    } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HIDER) {
                        playerCountOfHider++;
                    }
                }
            }
            for (EntityPlayer player : worldIn.playerEntities) {
                BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.NULL) {
                    allReady = false;
                    if (playerCountOfReady == worldIn.playerEntities.size() - 1) {
                        if (playerCountOfHunter == 0) {
                            blockHasPlayer.setStatus(BlockHasPlayer.Status.HUNTER);
                            FuncOperation.updatePlayingData(worldIn);
                            detectForReady(worldIn);
                        } else if (playerCountOfHider == 0) {
                            blockHasPlayer.setStatus(BlockHasPlayer.Status.HIDER);
                            FuncOperation.updatePlayingData(worldIn);
                            detectForReady(worldIn);
                        }
                    }
                }
            }
        }
        if (allReady) {
            FuncOperation.messageAll(worldIn, String.format("%s (%d/%d)", I18n.format("block_has.chat.player_all_ready"), playerCountOfReady, worldIn.playerEntities.size()));
            startGame(worldIn);
        } else {
            FuncOperation.messageAll(worldIn, String.format("%s (%d/%d)", I18n.format("block_has.chat.player_ready"), playerCountOfReady, worldIn.playerEntities.size()));
        }
    }

    public static void startUpdateDataLoop(World worldIn) {
        FuncOperation.updatePlayingData(worldIn);

        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                startUpdateDataLoop(worldIn);
            }
        }, 500);
    }

}
