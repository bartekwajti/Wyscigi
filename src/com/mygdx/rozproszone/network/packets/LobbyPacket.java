package com.mygdx.rozproszone.network.packets;

/**
 * Created by Admin on 2016-05-23.
 */
public class LobbyPacket extends Packet {

    public int lapsCount;
    public int livesCount;

    public LobbyPacket(int lapsCount, int livesCount) {
        this.lapsCount = lapsCount;
        this.livesCount = livesCount;
    }

    @Override
    public String getPacketName() {
        return PacketsConstants.LOBBY_PACKET;
    }
}
