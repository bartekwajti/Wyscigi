package com.mygdx.rozproszone.network;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Admin on 2016-05-15.
 */
public class ClientReceiverThread implements Runnable {

    ObjectInputStream ois;
    Client client;
    boolean running = true;
    ClientReceiverThread(ObjectInputStream ois, Client client) {
        this.ois = ois;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Packet receivedPacket = null;
            while(running){

                receivedPacket = (Packet)ois.readObject();
                String packetType = receivedPacket.getPacketName();

                if(packetType.equals(PacketsConstants.GAME_PACKET)) {
                    GamePacket gamePacket = (GamePacket)receivedPacket;
                    client.updateState(gamePacket);
                }
                else if(packetType.equals(PacketsConstants.LOBBY_PACKET)) {
                    LobbyPacket lobbyPacket = (LobbyPacket)receivedPacket;

                }
                else if(packetType.equals(PacketsConstants.COMMAND_PACKET)) {
                    CommandPacket commandPacket = (CommandPacket)receivedPacket;
                    if(commandPacket.command.equals("serverClosing")) {
                        client.diconnect();
                    }
                }

//                if(gamePacket == null)
//                    running = false;
//                else
//                    client.updateState(gamePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
