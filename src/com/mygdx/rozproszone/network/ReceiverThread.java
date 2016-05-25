/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

import com.mygdx.rozproszone.network.packets.GamePacket;
import com.mygdx.rozproszone.network.packets.LobbyPacket;
import com.mygdx.rozproszone.network.packets.Packet;
import com.mygdx.rozproszone.network.packets.PacketsConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ReceiverThread implements Runnable {
    private Socket socket;
    
    private boolean running = true;
    private static final Logger log = Logger.getLogger(ReceiverThread.class.getName());
    private int id;
    private MessageProcessor messageProcessor;
    
    public ReceiverThread(int id, Socket socket, MessageProcessor messageProcessor) {
        this.id = id;
        this.socket = socket;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void run() {

        try (InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is))
        {
            Packet receivedPacket;
            while(running){



                receivedPacket = (Packet)ois.readObject();
                String packetType = receivedPacket.getPacketName();

                if(packetType.equals(PacketsConstants.GAME_PACKET)) {
                    GamePacket gamePacket = (GamePacket)receivedPacket;
                    messageProcessor.addPacket(gamePacket);
                }
                else if(packetType.equals(PacketsConstants.LOBBY_PACKET)) {
                    LobbyPacket lobbyPacket = (LobbyPacket)receivedPacket;

                }


            }

            ois.close();

        } catch (IOException ex) {
            log.log(Level.WARNING, ex.getMessage(), ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
                
            } catch (IOException ex) {
                log.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}