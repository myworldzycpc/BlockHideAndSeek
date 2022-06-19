package io.github.myworldzycpc.block_has.network;

public enum OperationType {
    NULL(-1, "null"),

    // common
    UPDATE_SETTINGS_DATA(0, "update_settings_data"),
    OPEN_GUI(1, "open_gui"),

    // server
    TELEPORT(2, "teleport"),
    CLIENT_RECEIVED_PACK(3, "client_received_pack"),
    KICK_BY_CHEAT(4, "kick_by_cheat"),

    // client
    UPDATE_PLAYING_DATA(5, "update_playing_data"),
    CLOSE_BOUNDING_BOX(6, "close_bounding_box");

    public int id;
    public String name;

    OperationType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OperationType fromId(int id) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.id == id) {
                return operationType;
            }
        }
        return NULL;
    }

    public String getName() {
        return name;
    }
}
