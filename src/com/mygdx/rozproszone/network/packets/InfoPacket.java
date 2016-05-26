package com.mygdx.rozproszone.network.packets;

/**
 * Created by Admin on 2016-05-26.
 */
public class InfoPacket extends Packet {

    public String info;

    public InfoPacket(String info) {
        this.info = info;
    }

    @Override
    public String getPacketName() {
        return PacketsConstants.INFO_PACKET;
    }
}
