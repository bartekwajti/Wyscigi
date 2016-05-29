package com.mygdx.rozproszone.network;

import com.mygdx.rozproszone.network.packets.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
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
                    messageProcessor.addPacket(lobbyPacket); // send new laps value to other clients
                    messageProcessor.setLapsCount(lobbyPacket.lapsCount);
                }
                else if(packetType.equals(PacketsConstants.COMMAND_PACKET)) {
                    CommandPacket commandPacket = (CommandPacket)receivedPacket;

                    switch (commandPacket.command) {
                        case PacketsConstants.CMD_DISCONNECT:
                            running = false;
                            messageProcessor.removeClient(socket, id);
                            System.out.println("Client " + id  +" disconnected");
                            break;
                        case PacketsConstants.CMD_START_GAME:
                            messageProcessor.addPacket(new CommandPacket(PacketsConstants.INFO_GAME_STARTED));
                            break;
                    }
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