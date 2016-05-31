package com.mygdx.rozproszone.network;

import com.mygdx.rozproszone.network.packets.*;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
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
                    client.getLobbyListener().onLapsCountChanged(lobbyPacket.lapsCount);
                    client.getLobbyListener().onLivesCountChanged(lobbyPacket.livesCount);
                }
                else if(packetType.equals(PacketsConstants.COMMAND_PACKET)) {
                    CommandPacket commandPacket = (CommandPacket)receivedPacket;
                    if(commandPacket.command.equals("serverClosing")) {
                        client.disconnect();
                    }
                    switch (commandPacket.command) {
                        case PacketsConstants.CMD_PLAYER_DISCONNECTED:
                            client.removePlayerState(commandPacket.playerID);
                            break;
                        case PacketsConstants.INFO_GAME_STARTED:
                            client.getLobbyListener().onGameStart();
                            break;
                    }
                }
                else if (packetType.equals(PacketsConstants.INFO_PACKET)) {
                    InfoPacket infoPacket = (InfoPacket)receivedPacket;
                    switch (infoPacket.info) {
                        case PacketsConstants.INFO_SERVER_IS_READY:
                            client.getServerListener().onServerIsReady();
                            break;
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

    public synchronized void stop() {
        running = false;
    }
}
