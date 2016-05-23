/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

import com.badlogic.gdx.math.Vector2;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class MessageProcessor implements Runnable {

    
    boolean running = true;
    
    private ArrayList<GamePacket> gamePackets = new ArrayList<>();
    private ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<ObjectOutputStream> streams = new ArrayList<>();
    
    public MessageProcessor() {
        
    }
    
    public synchronized void addClient(Socket socket) {
        clients.add(socket);
        synchronized(streams)
        {
        try {
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            streams.add(oos);
            //send configuration packet to client
            GamePacket setupGamePacket = new GamePacket(new Vector2(100,100),
                                            0.0f,
                                            streams.size()-1, 0);
            oos.writeObject(setupGamePacket);
        } catch (IOException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }
    
    public void addPacket(GamePacket gamePacket) {
        synchronized(gamePackets) {
            gamePackets.add(gamePacket);
        }
    }
    
    @Override
    public void run() {
        
        while (running) {
            synchronized(gamePackets) {
                for(GamePacket gamePacket : gamePackets) {
                    for(int i = 0; i < clients.size(); ++i) {
                        System.out.println(gamePacket.playerID + " [" + gamePacket.position.x + ", " + gamePacket.position.y + "] " + gamePacket.angle  + " " + gamePacket.lapsCount);
                        if(i != gamePacket.playerID) {
                            try {
                                streams.get(i).writeObject(gamePacket);
                            } catch (IOException ex) {
                                Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                            
                    }
                }
                gamePackets.clear();
            }
        }
    }
    
}
