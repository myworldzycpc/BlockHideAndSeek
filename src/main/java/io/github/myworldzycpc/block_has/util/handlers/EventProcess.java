package io.github.myworldzycpc.block_has.util.handlers;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.PlayingType;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.util.Template;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.time.Duration;
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
                if (blockHasPlayingGlobal.getPlaying() == PlayingType.PLAYING) {
                    if (blockHasPlayingGlobal.getPlayer(attacker).getStatus() == BlockHasPlayer.Status.HUNTER) {
                        if (blockHasPlayingGlobal.getPlayer(attacked).getStatus() == BlockHasPlayer.Status.HIDER) {
                            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.was_killed", attacked.getDisplayNameString(), attacker.getDisplayNameString());
                            blockHasPlayingGlobal.getPlayer(attacked).setStatus(BlockHasPlayer.Status.VISITOR);
                            FuncOperation.updatePlayingData(worldIn);

                            attacked.setGameType(GameType.SPECTATOR);
                            boolean isEndGame = true;
                            for (EntityPlayer player : worldIn.playerEntities) {
                                if (blockHasPlayingGlobal.getPlayer(player).getStatus() == BlockHasPlayer.Status.HIDER) {
                                    isEndGame = false;
                                }
                            }
                            if (isEndGame) {
                                FuncOperation.messageAllTranslation(worldIn, "block_has.chat.all_hider_found");

                                Duration duration = Duration.ofMillis(new Date().getTime() - blockHasPlayingGlobal.getStartTime());

                                ITextComponent ComponentSet = ITextComponent.Serializer.jsonToComponent(String.format(Template.END_GAME_INFO, attacked.getDisplayNameString(), attacker.getDisplayNameString(), FuncAlgorithms.formatDuration(duration)));

                                FuncOperation.messageAllCompound(worldIn, ComponentSet);

                                FuncFragment.endGame(worldIn);
                            }
                        } else {
                            //FuncOperation.messageTranslation(attacker, "block_has.chat.target_is_not_hider");
                        }
                    } else {
                        //FuncOperation.messageTranslation(attacker, "block_has.chat.you_are_not_hunter");
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
                if (blockHasPlayingGlobal.getPlaying() != PlayingType.END_GAME) {
                    EntityPlayer playerIn = (EntityPlayer) entityIn;
                    BlockHasPlayer blockHasPlayerIn = blockHasPlayingGlobal.getPlayer(playerIn);
                    blockHasPlayerIn.addOldPosition(playerIn.getPositionVector());
//                    FuncOperation.updatePlayingData(worldIn);
                    if (blockHasPlayerIn.getStatus() == BlockHasPlayer.Status.HUNTER) {
                        List<Double> distanceList = new ArrayList<>();
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
                        blockHasPlayingGlobal.markDirty();

                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void livingFall(LivingFallEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer eventPlayer = (EntityPlayer) event.getEntityLiving();
        //Check if the damage should be removed
        event.setDistance(0F);
    }

    @SubscribeEvent
    public static void gameOverlayText(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getMinecraft();
        World worldIn = mc.world;
        FontRenderer fontRenderer = mc.fontRenderer;
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(worldIn);
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(worldIn);

        if (mc.getRenderManager().isDebugBoundingBox() && blockHasPlayingGlobal.getPlaying() == PlayingType.PLAYING && blockHasSettingsGlobal.isAntiCheating()) {
            mc.getRenderManager().setDebugBoundingBox(false);
            FuncOperation.kickByCheat();
        }

        if (blockHasSettingsGlobal.isShowHUD()) {

            if (blockHasPlayingGlobal.getPlaying() == PlayingType.PLAYING) {


                String blockHasTitleShow = I18n.format("block_has.chat.block_has");

                Duration duration = Duration.ofMillis(new Date().getTime() - blockHasPlayingGlobal.getStartTime());


                String playingTimeShow = I18n.format("block_has.chat.game_time") + FuncAlgorithms.formatDuration(duration);

                int huntersNumber = 0;
                int hidersNumber = 0;

                for (EntityPlayer player : worldIn.playerEntities) {
                    BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                    if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HUNTER) {
                        huntersNumber++;
                    } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HIDER) {
                        hidersNumber++;
                    }
                }

                String huntersNumberShow = I18n.format("block_has.generic.colon", I18n.format("block_has.chat.number_of_hunters"), huntersNumber);
                String hidersNumberShow = I18n.format("block_has.generic.colon", I18n.format("block_has.chat.number_of_hiders"), hidersNumber);
                String mapNameShow = I18n.format("block_has.generic.colon", I18n.format("block_has.chat.name_of_map"), blockHasPlayingGlobal.getBlockHasMap().mapName);

                List<Integer> widthList = new ArrayList<>();

                widthList.add(fontRenderer.getStringWidth(blockHasTitleShow));
                widthList.add(fontRenderer.getStringWidth(playingTimeShow));
                widthList.add(fontRenderer.getStringWidth(huntersNumberShow));
                widthList.add(fontRenderer.getStringWidth(hidersNumberShow));
                widthList.add(fontRenderer.getStringWidth(mapNameShow));

                int maxWidth = Collections.max(widthList);

                ScaledResolution scaledResolution = new ScaledResolution(mc);

                int offsetX = scaledResolution.getScaledWidth() - 10 - maxWidth;
                int offsetY = 10;

                float y = offsetY;

                // draw the background
                Gui.drawRect(offsetX - 5, offsetY - 5, offsetX + maxWidth + 5, offsetY + fontRenderer.FONT_HEIGHT * 7 + 5, 0x50000000);

                // draw the text
                fontRenderer.drawStringWithShadow(blockHasTitleShow, offsetX + (maxWidth / 2.0f - fontRenderer.getStringWidth(blockHasTitleShow) / 2.0f), y, 0xFFFFFFFF);
                fontRenderer.drawStringWithShadow(playingTimeShow, offsetX, y += fontRenderer.FONT_HEIGHT * 2, 0xFFFFFFFF);
                fontRenderer.drawStringWithShadow(huntersNumberShow, offsetX, y += fontRenderer.FONT_HEIGHT, 0xFFFF8080);
                fontRenderer.drawStringWithShadow(hidersNumberShow, offsetX, y += fontRenderer.FONT_HEIGHT, 0xFF00FF00);
                fontRenderer.drawStringWithShadow(mapNameShow, offsetX, y += fontRenderer.FONT_HEIGHT * 2, 0xFFFFFFFF);
            }
        }
    }

    @SubscribeEvent
    public static void loadHandler(WorldEvent.Load event) {
        World worldIn = event.getWorld();
        if (!worldIn.isRemote) {
            if (worldIn instanceof WorldServer && !(worldIn instanceof WorldServerMulti)) {
                FuncOperation.updatePlayingData(worldIn);

            }
        }
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (player.isServerWorld()) {
                PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(player.world);
                FuncOperation.updatePlayingData(player.world);
                FuncOperation.updateSettingsData(player.world);
//                EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
                BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(player);
                if (blockHasPlayingGlobal.getPlaying() == PlayingType.PLAYING) {
                    if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.NULL) {
                        blockHasPlayer.setStatus(BlockHasPlayer.Status.VISITOR);
                        player.setGameType(GameType.SPECTATOR);
                        FuncOperation.teleportPlayer(player, blockHasPlayingGlobal.getBlockHasMap().spawnPoint);
                        FuncOperation.title(player, blockHasPlayingGlobal.getBlockHasMap().mapName);
                    } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.VISITOR) {
                        player.setGameType(GameType.SPECTATOR);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        EntityPlayer player = event.player;
        World worldIn = player.world;
        if (worldIn.isRemote) {
            System.exit(233);
        }
        PlayingWorldSavedData blockHasPlayingGlobal = PlayingWorldSavedData.getGlobal(player.world);
        FuncOperation.debugInfo(player.world, player.getDisplayNameString() + " logged out.");
        if (worldIn.playerEntities.get(0) == player && blockHasPlayingGlobal.getPlaying() != PlayingType.END_GAME) {
            FuncFragment.endGame(worldIn);
        }
        boolean hasHunters = false;
        boolean hasHiders = false;
        for (EntityPlayer checkingPlayer : worldIn.playerEntities) {
            BlockHasPlayer blockHasPlayer = blockHasPlayingGlobal.getPlayer(checkingPlayer);
            if (checkingPlayer != player) {
                if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HUNTER) {
                    hasHunters = true;
                } else if (blockHasPlayer.getStatus() == BlockHasPlayer.Status.HIDER) {
                    hasHiders = true;
                }
            }
        }

        if (!hasHunters) {
            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.hunters_left");
        }
        if (!hasHiders) {
            FuncOperation.messageAllTranslation(worldIn, "block_has.chat.hiders_left");
        }
    }

}