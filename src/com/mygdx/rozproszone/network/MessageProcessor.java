/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.rozproszone.network.packets.*;

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
    
    private ArrayList<Packet> gamePackets = new ArrayList<>();

    private ArrayList<Socket> clients = new ArrayList<>();
    private ArrayList<ObjectOutputStream> streams = new ArrayList<>();

    private int lapsCount = 3;

    public MessageProcessor() {
        
    }

    public synchronized int getClientsCount() {
        synchronized (clients) {
            return clients.size();
        }
    }

    public synchronized void addClient(Socket socket, int id) {
        clients.add(socket);
        synchronized(streams)
        {
        try {
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            streams.add(oos);
            //send configuration packet to client
            GamePacket setupGamePacket = new GamePacket(new Vector2(100,100),
                                            0.0f,
                                            id,
                                            lapsCount);

            oos.writeObject(setupGamePacket);
        } catch (IOException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }

    public synchronized void removeClient(Socket socket, int id) {
        synchronized (gamePackets) {
            try {

                socket.close();
                int index = clients.indexOf(socket);
                clients.set(index, null);
                streams.set(index, null);

                CommandPacket disconnectedPacket = new CommandPacket(PacketsConstants.CMD_PLAYER_DISCONNECTED);
                disconnectedPacket.playerID = index;
                addPacket(disconnectedPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void addPacket(Packet packet) {
        synchronized(gamePackets) {
            gamePackets.add(packet);
        }
    }
    
    @Override
    public void run() {
        String packetType;
        while (running) {
            synchronized(gamePackets) {
                for(Packet packet : gamePackets) {
                    packetType = packet.getPacketName();
                    for(int i = 0; i < clients.size(); ++i) {
                        if(clients.get(i) != null && i != packet.playerID) {
                            try {

                                if(packet.getPacketName().equals(PacketsConstants.GAME_PACKET)) {
                                    GamePacket gamePacket = (GamePacket)packet;
                                    streams.get(i).writeObject(gamePacket);
                                    System.out.println(gamePacket.playerID + " [" + gamePacket.position.x + ", " + gamePacket.position.y + "] " + gamePacket.angle  + " " + gamePacket.lapsCount);
                                }
                                else if (packet.getPacketName().equals(PacketsConstants.COMMAND_PACKET)) {
                                    CommandPacket commandPacket = (CommandPacket) packet;

                                    streams.get(i).writeObject(commandPacket);
                                    //streams.get(i).writeObject(packet);
                                }
                                else if (packet.getPacketName().equals(PacketsConstants.LOBBY_PACKET)) {
                                    LobbyPacket lobbyPacket = (LobbyPacket) packet;
                                    streams.get(i).writeObject(lobbyPacket);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else if(clients.get(i) != null && i == packet.playerID) {
                            try {
                                switch (packetType) {
                                    case PacketsConstants.INFO_PACKET: {
                                        InfoPacket infoPacket = (InfoPacket) packet;
                                        switch (infoPacket.info) {
                                            case PacketsConstants.INFO_SERVER_IS_READY:
                                                streams.get(i).writeObject(infoPacket);
                                                break;
                                        }
                                    }
                                    break;
                                }
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

    public void setLapsCount(int lapsCount) {
        this.lapsCount = lapsCount;
    }
    
}
