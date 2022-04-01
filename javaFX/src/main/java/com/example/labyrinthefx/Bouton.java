package com.example.labyrinthefx;

/**
 * Un bouton fonctionne comme un interrupteur, mais permet d'autoriser le passage d'une porte
 */
public class Bouton extends Position{
    //attributs
    private boolean active;

    public Bouton(int x, int y){
        super(x,y);
        this.active = false;
    }

    public boolean getActive(){
        return active;
    }

    public void tournerBouton(){
        if (active){
            this.active = false;
        }
        else {
            this.active = true;
        }
    }

}
