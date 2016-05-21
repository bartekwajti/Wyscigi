/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.rozproszone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Daniel
 */
public class Player {
    private float positionX;
    private float positionY;
    private float angle;
    private double velocity;
    private double acceleration;    //przyśpieszenie samochodu
    private double deaccelerate;    //zwalnianie podczas hamowania
    private int laps;
    private int ID;
    private boolean[] lapsCheck;
    private int lapsCounter;

    private Music[] sounds;
    private Sprite carImage;
    private Texture carTexture;
    private boolean ableToMove;
    private boolean slowedDust;
    
    public Player(float positionX, float positionY,String carTextureName, float angle, int laps, int ID, int lapsCount){
        this.carTexture=new Texture(carTextureName);
        this.carImage=new Sprite(this.carTexture,0,0,this.carTexture.getWidth(),this.carTexture.getHeight());
        this.positionX=positionX;
        this.positionY=positionY;
        this.angle = angle;
        this.carImage.setPosition((float)this.positionX,(float) this.positionY);
        this.velocity=0;
        this.acceleration=10000;
        this.deaccelerate=50000;
        this.ableToMove = true;
        this.laps = laps;
        this.ID = ID;
        this.lapsCheck = new boolean[lapsCount];
        this.lapsCounter = 0;
        for (int i =0;i<lapsCount;i++){
            lapsCheck[i] = false;
        }
        lapsCheck[lapsCounter] = true;
    }
    public void setLaps(int laps)
    {
        this.laps = laps;
    }
    public void checkLaps(){
        if (lapsCheck[0] && positionX>=720)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
        }
        else if (lapsCheck[1] && positionY<=336)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
        }
        else if (lapsCheck[2] && positionX<=272)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter++;
            lapsCheck[lapsCounter] = true;
        }
        else if (lapsCheck[3] && positionX>=320 && positionY >=496)
        {
            lapsCheck[lapsCounter] = false;
            lapsCounter = 0;
            lapsCheck[lapsCounter] = true;
            this.laps--;
        }
    }
    public void setAngle(float angle){
        carImage.setRotation(angle*(-1));
        this.angle = angle;
    }
    public float getAngle(){return  this.angle;}
    public void changeAngle(float angle)
    {

        this.angle += angle;
        if (this.angle > 360.0f)
        {
            this.angle  -= 360.0f;
        }
        else if (this.angle <0.0f)
        {
            this.angle += 360.0f;
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
    
    public double getAcceleration(){
        return this.acceleration;
     }
    
    public double getDeccelerate(){
        return this.deaccelerate;
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
        this.positionX+=changeX;
        this.positionY+=changeY;
        this.carImage.setPosition(this.positionX,this.positionY);
    }
    
    public void changeVelocity(double changeVelocity){
        this.velocity+=changeVelocity;
    }
    
    public void speedUp(float time){
       if(this.velocity<300) this.velocity=0.5*acceleration*time*time;
       positionY+=velocity;
       this.carImage.setPosition(this.positionX,this.positionY);
       //v=v0+1/2*a*t*t
    }
    
    public void slowDown(float time){
       if(this.velocity>-10000) this.velocity=-0.5*acceleration*time*time;
       positionY+=velocity;
       this.carImage.setPosition(this.positionX,this.positionY);
    }
    
    public void toBreak(float time){
       if(this.velocity>0) this.velocity=-0.5*deaccelerate*time*time;
       positionY+=velocity;
       this.carImage.setPosition(this.positionX,this.positionY);
    }
    public int getLaps(){return this.laps;}
    public void noAccelerateAction(float time){
       if(this.velocity>-5 && this.velocity<5){
           this.velocity=0;
       }
       else{
           if(this.velocity>0) this.velocity=-0.5*3000*time*time;
           else this.velocity=0.5*3000*time*time;
       }
       this.positionY+=velocity;
       this.carImage.setPosition(this.positionX,this.positionY);
    }

    public int getID(){ return this.ID;}
}
