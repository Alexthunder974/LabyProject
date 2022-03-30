package com.example.labyrinthefx;

public class Position {
    private int x;
    private int y;

    /**
     */
    public Position(int abscisse, int ordonnee){
        this.y = ordonnee;
        this.x = abscisse;
    }
    public Position(int a[]){
        this.x = a[0];
        this.y = a[1];
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
