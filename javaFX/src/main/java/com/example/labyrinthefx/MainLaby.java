package com.example.labyrinthefx;

import java.io.IOException;
import java.util.Scanner;

public class MainLaby {

    public static void main(String[] args) throws ActionInconnueException, FichierIncorrectException {
        String nomLaby = "laby/laby_test.txt";

        try {
            Labyrinthe.creerLabyrinthe(10, 10, nomLaby);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Labyrinthe.sauvegarderLaby(nomLaby);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Labyrinthe laby = Labyrinthe.chargerLabyrinthe(nomLaby);

        //boite de dialogue avec le joueur
        Scanner sc = new Scanner(System.in);
        String action;
        //boucle du jeu
        boolean run = true;
        //boolean action valide
        boolean actionOK;
        System.out.println(laby);
        while (run){
            System.out.println("Entrez une action :");
            action = sc.next();
            if (action.equalsIgnoreCase("exit")){
                run = false;
            }
            else{
                actionOK = false;
                while(!actionOK) {
                    try {
                        laby.deplacerPerso(action);
                        actionOK = true;
                    }
                    catch (ActionInconnueException e){
                        System.out.println("Action inconnue, veuillez saisir une autre valeur :");
                        action = sc.next();
                        if (action.equalsIgnoreCase("exit")){
                            run = false;
                            break;
                        }
                    }
                }
                System.out.println(laby);
                if (laby.etreFini()) {
                    run = false;
                }
            }
        }
        System.out.println("Jeu termine");
    }
}
