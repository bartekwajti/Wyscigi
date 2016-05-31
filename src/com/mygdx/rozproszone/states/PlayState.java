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

    private Player currentPlayer;

    private HashMap<Integer, Player> players = new HashMap<>();
    private Level level;

    private int hostLaps;
    private int hostLives;

    private String server;
    private Client client;

    private int playerID =0;

    private Float angleDelta = 5.0f;
    private Float newX;
    private Float newY;
    private boolean velocityFlag = false;
    private boolean endGame;



    public PlayState(GameStateManager gsm) {

        super(gsm);
        level=new Level();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        endGame = false;

    }

    @Override
    public void update(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            if (endGame)
            {
                    currentPlayer.startedNewGame(hostLives,hostLaps);

                    endGame = false;
                    level.newGame();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            velocityFlag = true;
            newX = (float) (Math.sin(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
            newY = (float) (Math.cos(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
            checkCollision(newX,newY);
            checkSlowDust(newX,newY);
            
            currentPlayer.changePosition(newX,newY );

            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                currentPlayer.getCarImage().rotate(-angleDelta);
                currentPlayer.changeAngle((angleDelta));
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                currentPlayer.getCarImage().rotate(angleDelta);
                currentPlayer.changeAngle(((angleDelta*-1.0f)));
            }
            if(currentPlayer.getVelocity() < 300) currentPlayer.changeVelocity(10);
            
            if(currentPlayer.getVelocity()> 150)angleDelta = (float) (600 / currentPlayer.getVelocity());
        }
        
        
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            velocityFlag = false;
            newX = (float) (-Math.sin(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
            newY = (float) (-Math.cos(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
            checkCollision(newX,newY);
            checkSlowDust(newX,newY);

            currentPlayer.changePosition(newX, newY);
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            {
                currentPlayer.getCarImage().rotate(-angleDelta);
                currentPlayer.changeAngle((angleDelta));
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                currentPlayer.getCarImage().rotate(angleDelta);
                currentPlayer.changeAngle(((angleDelta*-1.0f)));
            }
            
            if(currentPlayer.getVelocity() < 100) currentPlayer.changeVelocity(5);
            
            if(currentPlayer.getVelocity() > 50)angleDelta = (float) (600 / currentPlayer.getVelocity());

        }
        else if(currentPlayer.getVelocity() > 0)
        {

                if (velocityFlag) {
                    newX = (float) (Math.sin(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
                    newY = (float) (Math.cos(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
                    currentPlayer.changeVelocity(-10);
                    checkCollision(newX,newY);
                    
                    checkSlowDust(newX,newY);

                    currentPlayer.changePosition(newX, newY);
                    
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    {
                        currentPlayer.getCarImage().rotate(-1);
                        currentPlayer.changeAngle((1));
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    {
                        currentPlayer.getCarImage().rotate(1);
                        currentPlayer.changeAngle((-1));
                    }

                }
                else{
                    currentPlayer.changeVelocity(-5);
                    newX = (float) (-Math.sin(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
                    newY = (float) (-Math.cos(currentPlayer.getAngle() * Math.PI / 180) * currentPlayer.getVelocity() * dt);
                    checkCollision(newX,newY);
                    checkSlowDust(newX,newY);

                    currentPlayer.changePosition(newX, newY);
                    
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    {
                        currentPlayer.getCarImage().rotate(-1);
                        currentPlayer.changeAngle((1));
                    }
                    else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    {
                        currentPlayer.getCarImage().rotate(1);
                        currentPlayer.changeAngle((-1));
                    }
            }
        }
        currentPlayer.checkLaps();
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
                player.setLaps(state.lapsCount);
                player.setLives(state.lives);
                player.setID(state.playerID);
                checkPlayersCollision(player);

            } else {
                player = new Player(state.position.x,state.position.y, Config.PLAYER_IMAGE_FILE_NAMES[state.playerID], state.angle,state.lapsCount,state.playerID,state.lives);
                players.put(key, player);
            }
        }
        checkWinConditions();
    }

    @Override
    public void render(SpriteBatch batch) {
        level.startView();
        level.draw(currentPlayer);
        level.drawPlayerLaps(currentPlayer);

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
        mainPlayer.set(currentPlayer.getPositionX(),currentPlayer.getPositionY(),currentPlayer.getCarImage().getWidth(),currentPlayer.getCarImage().getHeight());
        otherPlayer.set(player.getPositionX(),player.getPositionY(),player.getCarImage().getWidth(),player.getCarImage().getHeight());
        if (mainPlayer.overlaps(otherPlayer)){
            currentPlayer.crash(player);
        }

    }

    private void checkCollision(Float x, Float y){

        for (int i = 0; i < level.getCollisionObjects().getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) level.getCollisionObjects().get(i);

            Rectangle rect = obj.getRectangle();
            Rectangle recPlayer = new Rectangle();
            recPlayer.set(currentPlayer.getPositionX()+x,currentPlayer.getPositionY()+y,currentPlayer.getCarImage().getWidth(),currentPlayer.getCarImage().getHeight());


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
            recPlayer.set(currentPlayer.getPositionX()+x,currentPlayer.getPositionY()+y,currentPlayer.getCarImage().getWidth(),currentPlayer.getCarImage().getHeight());


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

        GamePacket gamePacket = new GamePacket(new Vector2(currentPlayer.getPositionX(), currentPlayer.getPositionY()),
                currentPlayer.getAngle(),
        playerID, currentPlayer.getLaps(),currentPlayer.getLives());
        return gamePacket;
    }

    @Override
    public void onPlayerDisconnected(Integer key) {

        players.remove(key);
    }

    public void setServer(String server) {

            this.server = server;
    }

    public void setClient(Client client) {

        this.client = client;

        this.playerID = client.getSetupGamePacket().playerID;

        currentPlayer= new Player(Config.PLAYER_STARTING_POSITION_X[this.playerID], Config.PLAYER_STARTING_POSITION_Y[this.playerID],Config.PLAYER_IMAGE_FILE_NAMES[this.playerID],360.0f,hostLaps,this.playerID,hostLives);

    }

    public void setLaps(int laps){

        this.hostLaps = laps;
    }

    public void setLives(int lives) {

        this.hostLives = lives;}

    private void checkWinConditions(){
        for (Map.Entry entry : players.entrySet()) {
            Player player = (Player)entry.getValue();
            if (player.getLaps() == 0){
                level.playerWinning(player.getID());
                currentPlayer.endGame();
                endGame = true;
                break;
            }
        }
        if (currentPlayer.getLaps() == 0){
            level.playerWinning(currentPlayer.getID());
            currentPlayer.endGame();
            endGame = true;
        }
    }
    
}
