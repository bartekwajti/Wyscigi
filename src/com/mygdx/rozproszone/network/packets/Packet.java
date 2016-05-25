package com.mygdx.rozproszone.network.packets;

import java.io.Serializable;

/**
 * Created by Admin on 2016-05-23.
 */
public abstract class Packet implements Serializable {
    public   int playerID;
    public abstract String getPacketName();
}
