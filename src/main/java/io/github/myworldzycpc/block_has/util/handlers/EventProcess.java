package io.github.myworldzycpc.block_has.util.handlers;

import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventProcess {

    @SubscribeEvent
    public static void attackHandler(AttackEntityEvent event) {
        if (!event.getTarget().world.isRemote) {
            if (event.getTarget() instanceof EntityPlayer) {
                EntityPlayer attacked = (EntityPlayer) event.getTarget();
                EntityPlayer attacker = event.getEntityPlayer();
                World worldIn = attacker.world;
                PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
                SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

                FuncOperation.debugInfo(worldIn, String.format("%s -> %s", attacker.getDisplayNameString(), attacked.getDisplayNameString()));
                if (blockHasPlayingGlobal.getPlaying().equals("playing")) {
                    if (blockHasPlayingGlobal.getPlayer(attacker).getStatus() == BlockHasPlayer.Status.HUNTER) {
                        if (blockHasPlayingGlobal.getPlayer(attacked).getStatus() == BlockHasPlayer.Status.HIDER) {
                            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.was_killed", attacked.getDisplayNameString(), attacker.getDisplayNameString());
                            blockHasPlayingGlobal.getPlayer(attacked).setStatus(BlockHasPlayer.Status.VISITOR);
                            attacked.setGameType(GameType.SPECTATOR);
                            boolean isEndGame = true;
                            for (EntityPlayer player : worldIn.playerEntities) {
                                if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HIDER) {
                                    isEndGame = false;
                                }
                            }
                            if (isEndGame) {
                                FuncOperation.messageAllTranslation(worldIn, "block_has.chat.all_hider_found");

                                TextComponentString endLine = new TextComponentString("\n");

                                TextComponentString splitLine = new TextComponentString("============================");
                                Style styleGreen = new Style();
                                styleGreen.setColor(TextFormatting.GREEN);
                                splitLine.setStyle(styleGreen);

                                TextComponentTranslation blockHas = new TextComponentTranslation("block_has.chat.block_has");
                                Style styleGoldBold = new Style();
                                styleGoldBold.setColor(TextFormatting.GOLD);
                                styleGoldBold.setBold(true);
                                blockHas.setStyle(styleGoldBold);

                                TextComponentTranslation lastBlock = new TextComponentTranslation("block_has.chat.last_block");
                                Style styleYellow = new Style();
                                styleYellow.setColor(TextFormatting.YELLOW);
                                lastBlock.setStyle(styleYellow);

                                TextComponentString lastBlockName = new TextComponentString(attacked.getDisplayNameString());
                                lastBlockName.setStyle(styleGreen);

                                TextComponentTranslation lastHunter = new TextComponentTranslation("block_has.chat.last_hunter");
                                lastHunter.setStyle(styleYellow);

                                TextComponentString lastHunterName = new TextComponentString(attacker.getDisplayNameString());
                                Style styleRed = new Style();
                                styleRed.setColor(TextFormatting.RED);
                                lastHunterName.setStyle(styleRed);

                                TextComponentTranslation gameTime = new TextComponentTranslation("block_has.chat.game_time");
                                gameTime.setStyle(styleYellow);

                                // todo: change to duration
                                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                                Date duration = new Date(new Date().getTime() - blockHasPlayingGlobal.getStartTime());
                                TextComponentString gameTimeString = new TextComponentString(timeFormatter.format(duration));
                                Style styleGray = new Style();
                                styleGray.setColor(TextFormatting.GRAY);
                                gameTimeString.setStyle(styleGray);

                                // todo: simplify
                                TextComponentString ComponentSet = splitLine.createCopy();
                                ComponentSet.appendSibling(endLine.createCopy());

                                ComponentSet.appendSibling(blockHas);
                                ComponentSet.appendSibling(endLine.createCopy());

                                ComponentSet.appendSibling(lastBlock);
                                ComponentSet.appendSibling(lastBlockName);
                                ComponentSet.appendSibling(endLine.createCopy());

                                ComponentSet.appendSibling(lastHunter);
                                ComponentSet.appendSibling(lastHunterName);
                                ComponentSet.appendSibling(endLine.createCopy());

                                ComponentSet.appendSibling(gameTime);
                                ComponentSet.appendSibling(gameTimeString);
                                ComponentSet.appendSibling(endLine.createCopy());

                                ComponentSet.appendSibling(splitLine);

                                FuncOperation.messageAllCompound(worldIn, ComponentSet);

                                FuncFragment.endGame(worldIn);
                            }
                        } else {
                            FuncOperation.messageTranslation(attacker, "block_has.chat.target_is_not_hider");
                        }
                    } else {
                        FuncOperation.messageTranslation(attacker, "block_has.chat.you_are_not_hunter");
                    }
                }

            }
        }

    }

    @SubscribeEvent
    public static void tickHandler(LivingEvent.LivingUpdateEvent event) {
        Entity entityIn = event.getEntity();
        World worldIn = entityIn.world;
        if (!worldIn.isRemote) {
            if (entityIn instanceof EntityPlayer) {
                PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
                if (!blockHasPlayingGlobal.getPlaying().equals("endGame")) {
                    EntityPlayer playerIn = (EntityPlayer) entityIn;
                    BlockHasPlayer blockHasPlayerIn = blockHasPlayingGlobal.getPlayer(playerIn);
                    blockHasPlayerIn.addOldPosition(playerIn.getPositionVector());
                    if (blockHasPlayerIn.getStatus() == BlockHasPlayer.Status.HUNTER) {
                        List<Double> distanceList = new ArrayList<Double>();
                        Vec3d playerInPosition = blockHasPlayerIn.getAverageOldPosition();
                        for (EntityPlayer player : worldIn.playerEntities) {
                            if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HIDER) {
                                BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                                distanceList.add(playerInPosition.distanceTo(blockHasPlayer.getAverageOldPosition()));
                            }
                        }
                        double minDistance;
                        if (distanceList.size() > 0) {
                            minDistance = Collections.min(distanceList);
                        } else {
                            minDistance = 0;
                        }
                        blockHasPlayerIn.setNewMinDistance(minDistance);
                    }
                }
            }
        }

    }

}
