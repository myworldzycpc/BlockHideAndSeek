package io.github.myworldzycpc.block_has.util;

import net.minecraft.client.resources.I18n;

public enum BlockHasPlayingMode {
    RANDOM(0, "block_has.container.settings.random_mode"),
    FREE(1, "block_has.container.settings.free_mode");

    public int id;
    public String translationKey;

    BlockHasPlayingMode(int id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public static BlockHasPlayingMode fromId(int id) {
        for (BlockHasPlayingMode blockHasPlayingMode : BlockHasPlayingMode.values()) {
            if (blockHasPlayingMode.id == id) {
                return blockHasPlayingMode;
            }
        }
        return RANDOM;
    }

    public String getDisplayName() {
        return I18n.format(this.translationKey);
    }
}
