package io.github.myworldzycpc.block_has.util.handlers;

import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventProcess {

    @SubscribeEvent
    public static void timerHandler(LivingEvent.LivingUpdateEvent event) {

    }



}
