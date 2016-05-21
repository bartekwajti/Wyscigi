/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.Level;
import com.mygdx.rozproszone.Player;
import com.mygdx.rozproszone.network.Client;
import com.mygdx.rozproszone.network.Client.PacketProvider;
import com.mygdx.rozproszone.network.Packet;
import com.mygdx.rozproszone.network.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class PlayState extends GameState implements PacketProvider {

    private Player player1;
    private Level level;
    
    String server;
    private int playerID = 0;
    Client client;
    
    private Float angleDelta = 5.0f;
    private Float newX;
    private Float newY;
    boolean velocityFlag = false;

    private HashMap<Integer, Player> players = new HashMap<>();
    
    public PlayState(GameStateManager gsm) {
        super(gsm);
        player1= new Player(150,450,"player1.png",360.0f,3,1,4);
        level=new Level("plansza.jpg");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;


        //test

    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            velocityFlag = true;
            newX = new Float (Math.sin(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
            newY = new Float (Math.cos(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
            checkCollision(newX,newY);
            checkSlowDust(newX,newY);
            
            player1.changePosition(newX,newY );
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                player1.getCarImage().rotate(-angleDelta);
                player1.changeAngle((angleDelta));
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                player1.getCarImage().rotate(angleDelta);
                player1.changeAngle(((angleDelta*-1.0f)));
            }
            if(player1.getVelocity() < 300) player1.changeVelocity(10);
            
            if(player1.getVelocity()> 150)angleDelta = new Float (600/player1.getVelocity());
        }
        
        
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            velocityFlag = false;
            newX = new Float (-Math.sin(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
            newY = new Float (-Math.cos(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
            checkCollision(newX,newY);
            checkSlowDust(newX,newY);
            
            player1.changePosition(newX, newY);
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                player1.getCarImage().rotate(-angleDelta);
                player1.changeAngle((angleDelta));
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                player1.getCarImage().rotate(angleDelta);
                player1.changeAngle(((angleDelta*-1.0f)));
            }
            
            if(player1.getVelocity() < 100) player1.changeVelocity(5);
            
            if(player1.getVelocity() > 50)angleDelta = new Float (600/player1.getVelocity());

        }
        else if(player1.getVelocity() > 0)
        {
            if (player1.getAbleToMove()) {
                
                if (velocityFlag) {
                    newX = new Float (Math.sin(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
                    newY = new Float (Math.cos(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt);
                    player1.changeVelocity(-10);
                    checkCollision(newX,newY);
                    
                    checkSlowDust(newX,newY);
                    
                    player1.changePosition(newX, newY);
                    
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    {
                        player1.getCarImage().rotate(-1);
                        player1.changeAngle((1));
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    {
                        player1.getCarImage().rotate(1);
                        player1.changeAngle((-1));
                    }

                }
                else{
                    player1.changeVelocity(-5);
                    newX = new Float((-Math.sin(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt));
                    newY = new Float((-Math.cos(player1.getAngle()*Math.PI/180)*player1.getVelocity()*dt));
                    checkCollision(newX,newY);
                    checkSlowDust(newX,newY);
                    
                    player1.changePosition(newX, newY);
                    
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    {
                        player1.getCarImage().rotate(-1);
                        player1.changeAngle((1));
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    {
                        player1.getCarImage().rotate(1);
                        player1.changeAngle((-1));
                    }
                }
            }
            else{
                player1.setVelocity(0);
            }
        }
        player1.checkLaps();
        client.update();

        ArrayList<Packet> states = client.getStates();
        for(Packet state : states) {
            Integer key = state.playerID;
            Player player;
            if (players.containsKey(key)) {
                player = players.get(key);
                player.setPositionX(state.position.x);
                player.setPositionY(state.position.y);
                player.setAngle(state.angle); //no angle in player
            } else {
                player = new Player(state.position.x,state.position.y,"player2.png", state.angle,3,2,4);
                players.put(key, player);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        level.startView();
        level.draw(player1);
        level.drawPlayerLaps(player1);
        for (Map.Entry entry : players.entrySet()) {
            level.draw((Player)entry.getValue());
            level.drawPlayerLaps((Player)entry.getValue());
        }
        level.updateView();
    }

    @Override
    public void handleInput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void checkCollision(Float x, Float y){
        for (int i = 0; i < level.getCollisionObjects().getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) level.getCollisionObjects().get(i);
            Rectangle rect = obj.getRectangle();
            Rectangle recPlayer = new Rectangle();
            recPlayer.set(player1.getPositionX()+x,player1.getPositionY()+y,player1.getCarImage().getWidth()-6,player1.getCarImage().getHeight()-20);
            
            
            if (recPlayer.overlaps(rect)) {
                newX = 0.0f;
                newY = 0.0f;
                break;
            }
            
        }
    }
    
    void checkSlowDust(Float x, Float y){
        for (int i = 0; i < level.getSlowDustObjects().getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) level.getSlowDustObjects().get(i);
            Rectangle rect = obj.getRectangle();
            Rectangle recPlayer = new Rectangle();
            recPlayer.set(player1.getPositionX()+x,player1.getPositionY()+y,player1.getCarImage().getWidth(),player1.getCarImage().getHeight());
            
            
            if (recPlayer.overlaps(rect)) {
                newX /=2;
                newY /=2;
                break;
            }
        }
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Packet getPacket() {
        Packet packet = new Packet(new Vector2(player1.getPositionX(), player1.getPositionY()),
        player1.getAngle(),
        playerID);
        return packet;
    }
    
    public void setServer(String server) {

            this.server = server;
            client = new Client(server, Server.PORT, this);

            this.playerID = client.getSetupPacket().playerID;

    }
    
}
