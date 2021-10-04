package io.github.myworldzycpc.block_has.util.handlers;

import io.github.myworldzycpc.block_has.func.FuncFragment;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.util.BlockHasPlayer;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.PlayingWorldSavedData;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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


}
