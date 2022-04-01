package com.example.labyrinthefx;

public class Porte extends Position{

    //attributs
    private Bouton bouton;

    /**
     * constructeur d'une porte
     * @param xP position x de la porte
     * @param yP position y de la porte
     * @param xB position x du bouton
     * @param yB position y du bouton
     */
    public Porte(int xP, int yP, int xB, int yB){
        super(xP,yP);
        this.bouton = new Bouton(xB, yB);
    }

    public Bouton getBouton() {
        return bouton;
    }

    //methodes
    public boolean etreOuvert(){
        return bouton.getActive();
    }
}
