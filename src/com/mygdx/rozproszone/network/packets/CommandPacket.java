package com.mygdx.rozproszone.network.packets;

import java.io.Serializable;

/**
 * Created by Admin on 2016-05-23.
 */
public class CommandPacket extends Packet implements Serializable {
    public String command;
    public CommandPacket(String command) {
        this.command = command;
    }

    @Override
    public String getPacketName() {
        return PacketsConstants.COMMAND_PACKET;
    }
}
