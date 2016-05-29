package com.mygdx.rozproszone.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.rozproszone.Config;
import com.mygdx.rozproszone.GameStateManager;
import com.mygdx.rozproszone.Level;
import com.mygdx.rozproszone.Player;
import com.mygdx.rozproszone.network.Client;
import com.mygdx.rozproszone.network.Client.PacketProvider;
import com.mygdx.rozproszone.network.packets.GamePacket;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class PlayState extends GameState implements PacketProvider {

    private Player player1;
    private Level level;
    private int hostLaps;
    private int hostLives;
    String server;
    private int playerID = 0;
    Client client;
    
    private Float angleDelta = 5.0f;
    private Float newX;
    private Float newY;
    private boolean velocityFlag = false;
    private Float startingPositionX = 150.0f;
    private Float startingPositionY = 400.0f;

    private HashMap<Integer, Player> players = new HashMap<>();
    
    public PlayState(GameStateManager gsm) {
        super(gsm);
        level=new Level(Config.FILES_PLANSZA);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

    }

    @Override
    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            velocityFlag = true;
            newX = (float) (Math.sin(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
            newY = (float) (Math.cos(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
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
            
            if(player1.getVelocity()> 150)angleDelta = (float) (600 / player1.getVelocity());
        }
        
        
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            velocityFlag = false;
            newX = (float) (-Math.sin(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
            newY = (float) (-Math.cos(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
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

                if (velocityFlag) {
                    newX = (float) (Math.sin(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
                    newY = (float) (Math.cos(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
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
                    newX = (float) (-Math.sin(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
                    newY = (float) (-Math.cos(player1.getAngle() * Math.PI / 180) * player1.getVelocity() * dt);
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
        player1.checkLaps();
        client.update();

        ArrayList<GamePacket> states = client.getStates();
        for(GamePacket state : states) {
            Integer key = state.playerID;
            Player player;
            if (players.containsKey(key)) {
                player = players.get(key);
                player.setPositionX(state.position.x);
                player.setPositionY(state.position.y);
                player.setAngle(state.angle);
                player.setLaps(state.lapsCount);//no angle in player
                player.setLives(state.lives);
                checkPlayersCollision(player);

            } else {
                player = new Player(state.position.x,state.position.y, Config.PLAYER_IMAGE_FILE_NAMES[state.playerID], state.angle,state.lapsCount,playerID,state.lives);
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

    private void checkPlayersCollision(Player player){

        Rectangle mainPlayer = new Rectangle();
        Rectangle otherPlayer = new Rectangle();
        mainPlayer.set(player1.getPositionX(),player1.getPositionY(),player1.getCarImage().getWidth(),player1.getCarImage().getHeight());
        otherPlayer.set(player.getPositionX(),player.getPositionY(),player.getCarImage().getWidth(),player.getCarImage().getHeight());
        if (mainPlayer.overlaps(otherPlayer)){
            player1.crash(player);
        }

    }

    private void checkCollision(Float x, Float y){

        for (int i = 0; i < level.getCollisionObjects().getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) level.getCollisionObjects().get(i);

            Rectangle rect = obj.getRectangle();
            Rectangle recPlayer = new Rectangle();
            recPlayer.set(player1.getPositionX()+x,player1.getPositionY()+y,player1.getCarImage().getWidth(),player1.getCarImage().getHeight());

            
            if (recPlayer.overlaps(rect)) {
                newX = 0.0f;
                newY = 0.0f;
                break;
            }
            
        }
    }
    
    private void checkSlowDust(Float x, Float y){
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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        client.disconnect();
    }

    @Override
    public GamePacket getPacket() {
        GamePacket gamePacket = new GamePacket(new Vector2(player1.getPositionX(), player1.getPositionY()),
        player1.getAngle(),
        playerID, player1.getLaps(),player1.getLives());
        return gamePacket;
    }

    @Override
    public void onPlayerDisconnected(Integer key) {
        players.remove(key);
    }

    public void setServer(String server) {

            this.server = server;
            /*client = new Client(server, Server.PORT, this);

            this.playerID = client.getSetupGamePacket().playerID;
            switch (this.playerID)
            {
                case 0:
                    player1= new Player(150,450,"player1.png",360.0f,hostLaps,1);
                    break;
                case 1:
                    player1= new Player(150,450,"player2.png",360.0f,hostLaps,1);
                    break;
                case 2:
                    player1= new Player(150,450,"player3.png",360.0f,hostLaps,1);
                    break;
                case 3:
                    player1= new Player(150,450,"player4.png",360.0f,hostLaps,1);
                    break;
            }*/
    }
    public void setClient(Client client) {
        this.client = client;

        this.playerID = client.getSetupGamePacket().playerID;
        switch (this.playerID)
        {
            case 0:
                player1= new Player(startingPositionX, startingPositionY,"player1.png",360.0f,hostLaps,0,hostLives);
                break;
            case 1:
                player1= new Player(startingPositionX +60.0f, startingPositionY,"player2.png",360.0f,hostLaps,1,hostLives);
                break;
            case 2:
                player1= new Player(startingPositionX, startingPositionY -80.0f,"player3.png",360.0f,hostLaps,2,hostLives);
                break;
            case 3:
                player1= new Player(startingPositionX +60.0f, startingPositionY -80.0f,"player4.png",360.0f,hostLaps,3,hostLives);
                break;
        }
    }

    public void setLaps(int laps){
        this.hostLaps = laps;
    }
    public void setLives(int lives) {this.hostLives = lives;}
    
}
