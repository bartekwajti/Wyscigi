package com.mygdx.rozproszone.network;

/**
 * Created by Admin on 2016-05-23.
 */
public class LobbyPacket extends Packet {

    public int lapsCount;
    public boolean isStarting;

    public LobbyPacket(int lapsCount, boolean isStarting) {
        this.lapsCount = lapsCount;
        this.isStarting = isStarting;
    }

    @Override
    public String getPacketName() {
        return PacketsConstants.LOBBY_PACKET;
    }
}
