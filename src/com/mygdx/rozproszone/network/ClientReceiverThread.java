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
            while(running){
                Packet packet = null;

                    packet = (Packet)ois.readObject();

                if(packet == null)
                    running = false;
                else
                    client.updateState(packet);
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
