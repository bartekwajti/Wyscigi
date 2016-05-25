/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network.packets;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class GamePacket extends Packet {
    public Vector2 position;
    public float angle;
    public int lapsCount;
    
    public GamePacket(Vector2 position, float angle, int id, int lapsCount) {
        this.position = position;
        this.angle = angle;
        this.playerID = id;
        this.lapsCount = lapsCount;
    }


    @Override
    public String getPacketName() {
        return PacketsConstants.GAME_PACKET;
    }
}
