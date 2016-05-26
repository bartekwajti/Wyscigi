package com.mygdx.rozproszone.network.packets;

/**
 * Created by Admin on 2016-05-23.
 */
public class LobbyPacket extends Packet {

    public int lapsCount;

    public LobbyPacket(int lapsCount) {
        this.lapsCount = lapsCount;
    }

    @Override
    public String getPacketName() {
        return PacketsConstants.LOBBY_PACKET;
    }
}
