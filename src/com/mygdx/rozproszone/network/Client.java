/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

import com.mygdx.rozproszone.network.packets.CommandPacket;
import com.mygdx.rozproszone.network.packets.GamePacket;
import com.mygdx.rozproszone.network.packets.LobbyPacket;
import com.mygdx.rozproszone.network.packets.PacketsConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Admin
 */
public class Client {



    public interface PacketProvider {
        GamePacket getPacket();
        void onPlayerDisconnected(Integer key);
    }

    public interface LobbyListener {
        void onGameStart();
        void onLapsCountChanged(int lapsCount);
        void onLivesCountChanged(int livesCount);
    }

    public interface ServerListener {
        void onServerIsReady();
    }

    ClientReceiverThread clientReceiver;

    PacketProvider packetProvider;
    LobbyListener lobbyListener;
    ServerListener serverListener;

    public static final String SEPARATOR = ":";
    private static final Logger log = Logger.getLogger(Client.class.getName());
    
    private String host = "";
    private int port;

    private Socket socket;
    
    private int clientID;
    
    private GamePacket setupGamePacket;

    private HashMap<Integer, GamePacket> playersStates = new HashMap();

    public boolean running = true;
    
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    InputStream is;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        //this.packetProvider = provider;

        
        try { 
            socket = new Socket(host, port);
            
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            setupGamePacket = (GamePacket)ois.readObject();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try { 
            OutputStream os = socket.getOutputStream();
            oos = new ObjectOutputStream(os); 
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        clientReceiver = new ClientReceiverThread(ois, this);
        Thread thread = new Thread(clientReceiver);
        thread.start();

    }
    
    
    public void update() {
        GamePacket gamePacket = packetProvider.getPacket();
        try {
            if(gamePacket == null)
                running = false;
            else {
                oos.writeObject(gamePacket);
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dispose() {
        try {
            oos.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public GamePacket getSetupGamePacket() {
        return setupGamePacket;
    }

    public void updateState(GamePacket gamePacket) {
        synchronized (playersStates) {
            Integer key = gamePacket.playerID;
            if(playersStates.containsKey(key)) {
                playersStates.replace(key, gamePacket);
            }
            else {
                playersStates.put(key, gamePacket);
            }
        }
    }

    public void removePlayerState(int id) {
        synchronized (playersStates) {
            Integer key = id;
            if(playersStates.containsKey(key)) {
                playersStates.remove(key);
                packetProvider.onPlayerDisconnected(key);
            }
        }
    }

    public ArrayList<GamePacket> getStates() {
        ArrayList<GamePacket> states = new ArrayList<>(5);
        synchronized (playersStates) {
            Iterator it = playersStates.entrySet().iterator();
            while(it.hasNext()) {
                GamePacket gamePacket = (GamePacket)(((Map.Entry)it.next()).getValue());
                states.add(gamePacket);
            }
        }
        return states;
    }

    private void sendDisconnectPacket() {
        try {
            CommandPacket disconnectPacket = new CommandPacket(PacketsConstants.CMD_DISCONNECT);
            oos.writeObject(disconnectPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStartGamePacket() {
        try {
            CommandPacket startGamePacket = new CommandPacket(PacketsConstants.CMD_START_GAME);
            oos.writeObject(startGamePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLapsCount(int lapsCount, int livesCount) {
        try {
            LobbyPacket lobbyPacket = new LobbyPacket(lapsCount, livesCount);
            lobbyPacket.playerID = clientID;
            oos.writeObject(lobbyPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLobbyListener(LobbyListener lobbyListener) {
        this.lobbyListener = lobbyListener;
    }

    public LobbyListener getLobbyListener() {
        return lobbyListener;
    }

    public void setPacketProvider(PacketProvider packetProvider) {
        this.packetProvider = packetProvider;
    }

    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public ServerListener getServerListener() {
        return serverListener;
    }

    public void disconnect() {
        sendDisconnectPacket();
        clientReceiver.stop();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
