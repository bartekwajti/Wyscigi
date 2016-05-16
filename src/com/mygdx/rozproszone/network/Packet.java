/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

import com.badlogic.gdx.math.Vector2;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Packet implements Serializable {
    public Vector2 position;
    public float angle;
    public int playerID;
    
    public Packet(Vector2 position, float angle, int id) {
        this.position = position;
        this.angle = angle;
        this.playerID = id;
    }
    
    
}
