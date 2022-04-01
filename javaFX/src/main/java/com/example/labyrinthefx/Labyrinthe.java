package com.example.labyrinthefx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe labyrinthe qui représente le terrain de jeu
 */
public class Labyrinthe {
    //constantes
    public static final char MUR = 'X';
    public static final char PJ = 'P';
    public static final char SORTIE = 'S';
    public static final char VIDE = '.';
    public static final char PORTE = '#';
    public static final char BOUTON = '*';

    //constantes commandes
    public static final String HAUT = "Haut";
    public static final String BAS = "Bas";
    public static final String GAUCHE = "Gauche";
    public static final String DROITE = "Droite";

    //attributs
    private boolean[][] murs; //vrai si il y a un mur, faux si case vide
    private Personnage personnage;
    private Sortie sortie;
    private Porte porte;

    //getters

    public boolean[][] getMurs() {
        return murs;
    }

    public Sortie getSortie() {
        return sortie;
    }

    public Personnage getPersonnage() {
        return personnage;
    }

    public Porte getPorte() {
        return porte;
    }

    //méthodes

    /**
     * La méthode a pour objectif de retourner un caractère décrivant le contenu de la case
     *
     * @param x coordonnée horizontale
     * @param y coordonnée verticale
     * @return le caractère à la position(x,y)
     */
    public char getChar(int x, int y) {
        char valeurCase;
        //si la case contient le personnage
        if (this.personnage.getX() == x && this.personnage.getY() == y) {
            valeurCase = PJ;
        } else {
            //sinon si la case contient la sortie
            if (this.sortie.getX() == x && this.sortie.getY() == y) {
                valeurCase = SORTIE;
            } else {
                //si la case est vide
                if (!this.murs[x][y]) {
                    valeurCase = VIDE;
                }
                //sinon la case est un mur
                else {
                    valeurCase = MUR;
                }
                if (this.porte != null) {
                    if (this.porte.getX() == x && this.porte.getY() == y) {
                        valeurCase = PORTE;
                    } else {
                        if (this.porte.getBouton().getX() == x && this.porte.getBouton().getY() == y) {
                            valeurCase = BOUTON;
                        }
                    }
                }
            }
        }
        return valeurCase;
    }


    /**
     * methode qui permet de donner la case suivante en fonction de la direction
     *
     * @param direction la direction qui va permettre de determiner la case suivante
     * @return renvoie un tableau contenant les coordonnées de la case suivante, (x,y)
     * @throws ActionInconnueException lance une erreur dans l'action/la direction n'est pas connue
     */
    public static int[] getSuivant(int x, int y, String direction) throws ActionInconnueException {
        boolean actionOk = true;
        int[] res = new int[2];

        if (direction.equalsIgnoreCase(HAUT)) {
            res[0] = x - 1;
            res[1] = y;
        } else {
            if (direction.equalsIgnoreCase(BAS)) {
                res[0] = x + 1;
                res[1] = y;
            } else {
                if (direction.equalsIgnoreCase(GAUCHE)) {
                    res[0] = x;
                    res[1] = y - 1;
                } else {
                    if (direction.equalsIgnoreCase(DROITE)) {
                        res[0] = x;
                        res[1] = y + 1;
                    } else {
                        actionOk = false;
                    }
                }
            }
        }
        if (!actionOk) {
            throw new ActionInconnueException(direction);
        } else {
            return res;
        }
    }

    /**
     * La methode a pour objectif de modifier les coordonees du personnage en fonction de l'action
     * Le personnage va glisser jusqu'au prochain obstable (un mur)
     *
     * @param action l'action que la personnage va effectuer
     */
    public void deplacerPerso(String action) throws ActionInconnueException {
        //tant que la case suivante dans la direction n'est pas un mur, deplacer le perso a la case suivante
        int[] caseSuivante = getSuivant(personnage.getX(), personnage.getY(), action);
        char valCase = getChar(caseSuivante[0], caseSuivante[1]);
        boolean porteOuverte = true;
        if (this.porte != null) {
            if (this.porte.getX() == caseSuivante[0] && this.porte.getY() == caseSuivante[1]) {
                porteOuverte = this.porte.etreOuvert();
            }
        }
        while (valCase != MUR && porteOuverte) {
            if (this.porte != null) {
                //si on passe par le bouton, on l'active ou le désactive
                if (this.porte.getBouton().getX() == caseSuivante[0] && this.porte.getBouton().getY() == caseSuivante[1]) {
                    this.porte.getBouton().tournerBouton();
                }
            }
            personnage.setX(caseSuivante[0]);
            personnage.setY(caseSuivante[1]);
            caseSuivante = getSuivant(personnage.getX(), personnage.getY(), action);
            valCase = getChar(caseSuivante[0], caseSuivante[1]);
            if (this.porte != null) {
                if (this.porte.getX() == caseSuivante[0] && this.porte.getY() == caseSuivante[1]) {
                    porteOuverte = this.porte.etreOuvert();
                }
            }
        }
    }


    public String toString() {

        String res = "";

        res += murs.length + "\n";
        res += murs[0].length + "\n";
        for (int i = 0; i < murs.length; i++) {
            for (int j = 0; j < murs[i].length; j++) {
                res += getChar(i, j);
            }
            res += "\n";
        }
        return res;
    }

    /**
     * methode qui verifie si le jeu doit etre arrete
     *
     * @return un boolean permettant d'arreter le jeu lorsque le perso se trouve sur la sortie
     */
    public boolean etreFini() {
        return (personnage.getX() == sortie.getX() && personnage.getY() == sortie.getY());

    }

    /**
     * methode chargerLabyrinthe qui permet d'instancier toutes les valeurs d'un labyrinthe, depuis un fichier texte
     *
     * @param nom le nom du fichier contenant le labyrinthe
     */
    public static Labyrinthe chargerLabyrinthe(String nom) {
        Labyrinthe labyrinthe = new Labyrinthe();
        //on ouvre un fichier texte (en Buffered pour lire les lignes) qui représente le labyrinthe
        BufferedReader fileLaby = null;
        try {
            fileLaby = new BufferedReader(new FileReader(nom));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //on récupère les deux premières lignes qui correspondent à la hauteur et la largeur
        int x = 0;
        int y = 0;
        try {
            x = Integer.parseInt(fileLaby.readLine());
            y = Integer.parseInt(fileLaby.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        labyrinthe.murs = new boolean[x][y];

        int[] posPorte = new int[0];
        int[] posBouton = new int[0];
        //on parcourt le fichier texte jusqu'à la fin pour remplir le tableau de boolean, murs
        String ligne = null;
        for (int i = 0; i < x; i++) {
            try {
                ligne = fileLaby.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < y; j++) {
                char value = ligne.charAt(j);
                if (value == MUR) {
                    labyrinthe.murs[i][j] = true;
                } else {
                    labyrinthe.murs[i][j] = false;
                    if (value == PJ) {
                        labyrinthe.personnage = new Personnage(i, j);
                    } else {
                        if (value == SORTIE) {
                            labyrinthe.sortie = new Sortie(i, j);
                        } else {
                            if (value == PORTE) {
                                posPorte = new int[]{i, j};
                            } else {
                                if (value == BOUTON) {
                                    posBouton = new int[]{i, j};
                                }
                            }
                        }
                    }
                }
            }
        }
        if (posBouton.length != 0 || posPorte.length != 0) {
            labyrinthe.porte = new Porte(posPorte[0], posPorte[1], posBouton[0], posBouton[1]);
        }
        try {
            fileLaby.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labyrinthe;
    }

}
