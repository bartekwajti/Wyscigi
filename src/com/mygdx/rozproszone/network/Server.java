package com.mygdx.rozproszone.network;

import com.mygdx.rozproszone.network.packets.InfoPacket;
import com.mygdx.rozproszone.network.packets.PacketsConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class Server implements Runnable{
    public static final int PORT = 9877;
    
    public static final int TIME_OUT = 1000;
    
     private static final Logger log = Logger.getLogger(Server.class.getName());
     
     private boolean running = true;
     
     private int connectedPlayers = 0;
     private int playersCount;
     private ArrayList<InetAddress> clients = new ArrayList<>();
     
     
     private MessageProcessor messageProcessor = new MessageProcessor();
     
     public Server(int players) {

         this.playersCount = players;
     }
     
     @Override
     public void run() {

         Thread processorThread = new Thread(messageProcessor);
         processorThread.start();
         try (ServerSocket server = new ServerSocket(PORT)) {
             server.setSoTimeout(TIME_OUT);
             while (true) {
                 synchronized (this) {
                     if (!running) {
                         break;
                     }
                 }
                 try {
                     Socket client = server.accept();
                     if (client != null && messageProcessor.getClientsCount() < playersCount) {
                         messageProcessor.addClient(client, connectedPlayers);
                         ReceiverThread receiver = new ReceiverThread(connectedPlayers, client, messageProcessor);
                         Thread th = new Thread(receiver);
                         th.start();
                         connectedPlayers++;

                         if(connectedPlayers == playersCount) {

                             messageProcessor.addPacket(new InfoPacket(PacketsConstants.INFO_SERVER_IS_READY));
                         }
                     }
                     else {
                         client.close();
                     }
                 } catch (SocketTimeoutException ex) {
                 }
                 
             }
         } catch (IOException ex) {
             log.log(Level.WARNING, ex.getMessage(), ex);
         }
     }
     
     public synchronized void stop() {

         running = false;
     }
     
     public synchronized void addInetAddress(InetAddress addr) {

         clients.add(addr);
     }
}