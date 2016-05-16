/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.network;

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
        Packet getPacket();
    }
    
    PacketProvider packetProvider;
    public static final String SEPARATOR = ":";
    private static final Logger log = Logger.getLogger(Client.class.getName());
    
    private String host = "";
    private int port;

    private Socket socket;
    
    private int clientID;
    
    private Packet setupPacket;

    private HashMap<Integer, Packet> playersStates = new HashMap();

    public boolean running = true;
    
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    InputStream is;
    public Client(String host, int port, PacketProvider provider) {
        this.host = host;
        this.port = port;
        this.packetProvider = provider;
        
        
        
        try { 
            socket = new Socket(host, port);
//            OutputStream os = socket.getOutputStream();
//            oos = new ObjectOutputStream(os); 
            
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            setupPacket = (Packet)ois.readObject();
            //ois.close();
            //socket.shutdownInput();
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

        ClientReceiverThread clientReceiver = new ClientReceiverThread(ois, this);
        Thread thread = new Thread(clientReceiver);
        thread.start();

    }
    
    
    public void update() {
        Packet packet = packetProvider.getPacket();
        try {
            if(packet == null)
                running = false;
            else {
                oos.writeObject(packet);
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
    
    public Packet getSetupPacket() {
        return setupPacket;
    }

    public void updateState(Packet packet) {
        synchronized (playersStates) {
            Integer key = packet.playerID;
            if(playersStates.containsKey(key)) {
                playersStates.replace(key, packet);
            }
            else {
                playersStates.put(key, packet);
            }
        }
    }

    public ArrayList<Packet> getStates() {
        ArrayList<Packet> states = new ArrayList<>(5);
        synchronized (playersStates) {
            Iterator it = playersStates.entrySet().iterator();
            while(it.hasNext()) {
                Packet packet = (Packet)(((Map.Entry)it.next()).getValue());
                states.add(packet);
            }
        }
        return states;
    }
}
