package io.github.myworldzycpc.block_has.util;

public enum PlayingType {

    END_GAME(0, "endGame"),
    READY(1, "ready"),
    PLAYING(2, "playing");

    public int id;
    public String name;

    PlayingType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PlayingType fromId(int id) {
        for (PlayingType playingType : PlayingType.values()) {
            if (playingType.id == id) {
                return playingType;
            }
        }
        return END_GAME;
    }

    public String getName() {
        return this.name;
    }


}
