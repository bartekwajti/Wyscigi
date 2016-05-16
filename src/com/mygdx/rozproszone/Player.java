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

    private double velocity;
    private double acceleration;    //przyśpieszenie samochodu
    private double deaccelerate;    //zwalnianie podczas hamowania
    
    
    private Music[] sounds;
    private Sprite carImage;
    private Texture carTexture;
    private boolean ableToMove;
    private boolean slowedDust;
    
    public Player(float positionX, float positionY,String carTextureName){
        this.carTexture=new Texture(carTextureName);
        this.carImage=new Sprite(this.carTexture,0,0,this.carTexture.getWidth(),this.carTexture.getHeight());
        this.positionX=positionX;
        this.positionY=positionY;
        this.carImage.setPosition((float)this.positionX,(float) this.positionY);
        this.velocity=0;
        this.acceleration=10000;
        this.deaccelerate=50000;
        this.ableToMove = true;
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

    
}
