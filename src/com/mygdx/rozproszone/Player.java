package com.mygdx.rozproszone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Daniel && Bartlomiej && Przemysław
 */

public class Player {

    private float positionX;
    private float positionY;

    private float angle;
    private double velocity;
    private int laps;
    private int ID;
    private boolean[] lapsCheck;
    private int lapsCounter;
    private int lives;
    private int checkLapsHelper;
    private boolean isCrashed;

    private Sprite carImage;
    private Texture carTexture;
    private boolean ableToMove;
    private boolean slowedDust;
    
    public Player(float positionX, float positionY,String carTextureName, float angle, int laps, int ID, int lives){

        this.carTexture=new Texture(carTextureName);
        this.carImage=new Sprite(this.carTexture,0,0,this.carTexture.getWidth(),this.carTexture.getHeight());
        this.positionX=positionX;
        this.positionY=positionY;
        this.angle = angle;
        this.carImage.setPosition((float)this.positionX,(float) this.positionY);
        this.velocity=0;
        this.ableToMove = true;
        this.laps = laps;
        this.ID = ID;
        this.lives = lives;
        this.lapsCheck = new boolean[4];
        this.lapsCounter = 0;
        isCrashed = false;
        this.checkLapsHelper = 4;
        for (int i =0;i<4;i++){
            lapsCheck[i] = false;
        }
        lapsCheck[lapsCounter] = true;
    }

    public void setLaps(int laps){

        this.laps = laps;
    }

    public void checkLaps(){

        if (lapsCheck[0] && positionX>=720)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
            this.checkLapsHelper = 1;
        }
        else if (lapsCheck[1] && positionY<=336)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
            this.checkLapsHelper = 2;
        }
        else if (lapsCheck[2] && positionX<=272)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
            this.checkLapsHelper = 3;
        }
        else if (lapsCheck[3] && positionX>=320 && positionY >=496)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter = 0;
            lapsCheck[lapsCounter] = true;
            this.laps--;
            this.checkLapsHelper = 0;
        }
    }

    public int checkLapsWhenCollision(){

        return checkLapsHelper;
    }

    public void setAngle(float angle){

        if (ableToMove) {
            carImage.setRotation(angle * (-1));
            this.angle = angle;
        }
    }

    public float getAngle(){

        return  this.angle;
    }

    public void changeAngle(float angle){

        if (ableToMove) {
            this.angle += angle;
            if (this.angle > 360.0f) {
                this.angle -= 360.0f;
            } else if (this.angle < 0.0f) {
                this.angle += 360.0f;
            }
        }
    }

    public void setAbleToMove(boolean b){

        this.ableToMove = b;
    }

    public boolean getAbleToMove(){

        return this.ableToMove;
    }

     public void setSlowedDust(boolean b){

        this.slowedDust = b;
    }

    public boolean getSlowedDust(){

        return this.slowedDust;
    }

    public void setPositionX(float positionX){

        this.positionX=positionX;
        this.carImage.setPosition(this.positionX,this.positionY);
    }

    public void setPositionY(float positionY){

        this.positionY=positionY;
        this.carImage.setPosition(this.positionX,this.positionY);
    }
   
    public void setVelocity(double velocity){

        this.velocity=velocity;
    }
   
    public float getPositionX(){

        return this.positionX;
    }
   
    public float getPositionY(){

        return this.positionY;
    }
    
    public double getVelocity(){

        return this.velocity;
    }
    
    public Sprite getCarImage(){

        return this.carImage;
    }
    
    public void changePositionX(float changeX){
        this.positionX+=changeX;
        this.carImage.setPosition(this.positionX,this.positionY);
    }
    
    public void changePositionY(float changeY){
        this.positionY+=changeY;
        this.carImage.setPosition(this.positionX,this.positionY);
    }
    
    public void changePosition(float changeX,float changeY){

        if (ableToMove) {
            this.positionX += changeX;
            this.positionY += changeY;
            this.carImage.setPosition(this.positionX, this.positionY);
        }
    }
    
    public void changeVelocity(double changeVelocity){

        this.velocity+=changeVelocity;
    }

    public int getLaps(){

        return this.laps;
    }

    public int getID(){

        return this.ID;
    }

    public int getLives(){

        return this.lives;
    }

    public void setLives(int lives){

        this.lives = lives;
    }

    public void decLives(){

        if (!isCrashed) {
            this.lives--;
        }
    }

    public void addLives(){

        this.lives++;
    }

    public void changeTextureToPlay(Player otherPlayer){

        carTexture = new Texture(Config.PLAYER_IMAGE_FILE_NAMES[this.ID]);
        carImage.setRegion(carTexture);
        carImage.setPosition(getPositionX(),getPositionY());

        otherPlayer.carTexture = new Texture(Config.PLAYER_IMAGE_FILE_NAMES[otherPlayer.ID]);
        otherPlayer.getCarImage().setRegion(otherPlayer.carTexture);
        otherPlayer.getCarImage().setPosition(otherPlayer.positionX,otherPlayer.getPositionY());
    }

    public void crash(Player player) {

        ableToMove = false;
        decLives();
        isCrashed = true;

        this.carTexture = new Texture(Config.PLAYER_CRASH_IMAGE_FILE_NAMES[this.ID]);
        carImage.setRegion(carTexture);

        player.carTexture = new Texture(Config.PLAYER_CRASH_IMAGE_FILE_NAMES[player.ID]);
        player.getCarImage().setRegion(player.carTexture);

        new Timer().schedule(new WaitAfterCrash(player){
        },3000);

    }

    private class WaitAfterCrash extends TimerTask{

        private Player otherPlayer;

        WaitAfterCrash(Player player){

            otherPlayer = player;
        }

        @Override
        public void run() {
            Gdx.app.postRunnable(() -> {

                isCrashed = false;
                changeTextureToPlay(otherPlayer);

                switch (checkLapsWhenCollision())
                {
                    case 3:
                        setPositionX(130.0f + getID()*35.0f);
                        setAngle(0.0f);
                        break;
                    case 0:
                        setPositionY(530.0f + getID()*35.0f);
                        setAngle(90.0f);
                        break;
                    case 1:
                        setPositionX(850.0f + getID()*35.0f);
                        setAngle(180.0f);
                        break;
                    case 2:
                        setPositionY(150.0f + getID()*35.0f);
                        setAngle(270.0f);
                        break;
                    default:
                        if (getPositionX()>=300)
                        {
                            setPositionY(530.0f + getID()*35.0f);
                            setAngle(90.0f);
                        }
                        else{
                            setPositionX(130.0f + getID()*35.0f);
                            setAngle(0.0f);
                        }
                        break;
                }
                ableToMove = true;
            });


        }

    }
}
