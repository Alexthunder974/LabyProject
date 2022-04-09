package com.example.labyrinthefx;

import java.io.*;

/**
 * Classe labyrinthe qui représente le terrain de jeu
 */
public class Labyrinthe {
    //constantes
    public static final char MUR = 'X';
    public static final char PJ = 'P';
    public static final char SORTIE = 'S';
    public static final char VIDE = '.';

    //constantes commandes
    public static final String HAUT = "Haut";
    public static final String BAS = "Bas";
    public static final String GAUCHE = "Gauche";
    public static final String DROITE = "Droite";

    //attributs
    private boolean[][] murs; //vrai si il y a un mur, faux si case vide
    private Personnage personnage;
    private Sortie sortie;

    //Constante avec le nom de fichier de sauvegarde
    public static final String NOM_BACKUP = "laby/mazeBackup.txt";


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
                if (this.murs[x][y] == false) {
                    valeurCase = VIDE;
                }
                //sinon la case est un mur
                else {
                    valeurCase = MUR;
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
        }
        else{
            if (direction.equalsIgnoreCase(BAS)) {
                res[0] = x + 1;
                res[1] = y;
            }
            else{
                if (direction.equalsIgnoreCase(GAUCHE)) {
                    res[0] = x;
                    res[1] = y - 1;
                }
                else{
                    if (direction.equalsIgnoreCase(DROITE)) {
                        res[0] = x;
                        res[1] = y + 1;
                    }
                    else{
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
        while (valCase != MUR) {
            personnage.setX(caseSuivante[0]);
            personnage.setY(caseSuivante[1]);
            caseSuivante = getSuivant(personnage.getX(), personnage.getY(), action);
            valCase = getChar(caseSuivante[0], caseSuivante[1]);
        }
    }


    public String toString() {

        String res = "";

        res += murs.length+"\n";
        res += murs[0].length+"\n";
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
     * @param nom le nom du fichier contenant le labyrinthe
     */
    public static Labyrinthe chargerLabyrinthe(String nom) throws FichierIncorrectException {
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
        } catch (NumberFormatException e) {
            throw new FichierIncorrectException("pb num ligne ou colonne");
        }
        labyrinthe.murs = new boolean[x][y];

        //on parcourt le fichier texte jusqu'à la fin pour remplir le tableau de boolean, murs
        String ligne = null;
        int compteurLigne = 0;

        //compteurs qui vont permettre de vérifier s'il y a deux ou plus personnages ou sorties
        int compteurPerso = 0;
        int compteurSortie = 0;

        for (int i = 0; i < x; i++) {
            try {
                ligne = fileLaby.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Vérifie si le nombre de lignes correspond à ce qui a été annoncé
            if (ligne == null){
                if (compteurLigne != x){
                    throw new FichierIncorrectException("nbLignes ne correspond pas");
                }
            }
            else {
                compteurLigne++;
            }
            if (ligne.length() != y){
                throw new FichierIncorrectException("nbColonnes ne correspond pas");
            }
            for (int j = 0; j < y; j++) {
                char value = ligne.charAt(j);
                if (value == MUR){
                    labyrinthe.murs[i][j] = true;
                }
                else{
                    labyrinthe.murs[i][j] = false;
                    if(value == PJ){
                        labyrinthe.personnage = new Personnage(i,j);
                        compteurPerso++;
                    }
                    else {
                        if (value == SORTIE){
                            labyrinthe.sortie = new Sortie(i,j);
                            compteurSortie++;
                        }
                        else {
                            if (value != VIDE){
                                throw new FichierIncorrectException("caractere inconnu "+value);
                            }
                        }
                    }
                }
            }
        }
        //on vérifie s'il reste des lignes à lire
        try {
            ligne = fileLaby.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //s'il reste des lignes, cela veut dire qu'il y a plus de lignes à ce qui a été annoncé
        if (ligne != null && !ligne.isEmpty()){
            throw new FichierIncorrectException("nbLignes ne correspond pas");
        }

        //si le personnage est à null, c'est qu'on n'a pas trouvé de P dans le fichier
        if (compteurPerso == 0)
            throw new FichierIncorrectException("personnage inconnu");
        if (compteurPerso >= 2)
            throw new FichierIncorrectException("plusieurs personnages");

        //si la sortie est à null, c'est qu'on n'a pas trouvé de S dans le fichier
        if (compteurSortie == 0)
            throw new FichierIncorrectException("sortie inconnue");
        if (compteurSortie >= 2)
            throw new FichierIncorrectException("plusieurs sorties");

        try {
            fileLaby.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labyrinthe;
    }

    public int[] getPos(){
        int[] res = new int[2];
        res[0] = personnage.getX();
        res[1] = personnage.getY();

        return res;
    }

    /**
     * methode qui permet de créer un labyrinthe pour le stocker dans un fichier txt
     *
     * @param x la taille x du labyrinthe
     * @param y la taille y du labyrinthe
     * @param nom le nom du fichier texte que l'on veut créer
     */
    public static void creerLabyrinthe(int x, int y, String nom) throws IOException {
        double nombreRandom;
        //On ouvre un fichier NOM.txt en écriture par caractère
        BufferedWriter file = new BufferedWriter(new FileWriter(nom));
        //On met x dans le fichier puis y
        file.write(""+x);
        file.newLine();
        file.write(""+y);
        file.newLine();
        //On met y fois le cara X à la première ligne
        for (int i = 0; i < y; i++) {
            file.write(MUR);
        }
        file.newLine();
        //on fait la première ligne avec un perso
        file.write(MUR);
        file.write(PJ);
        for (int i = 0; i < y-3; i++) {
            nombreRandom = Math.random();
            if (nombreRandom < 0.2)
                file.write(MUR);
            else
                file.write(VIDE);
        }
        file.write(MUR);
        file.newLine();
        //Pour les lignes suivantes on commence et on fini par X
        for (int i = 0; i < x-4; i++) {
            file.write(MUR);
            for (int j = 0; j < y-2; j++) {
                nombreRandom = Math.random();
                if (nombreRandom < 0.2)
                    file.write(MUR);
                else
                    file.write(VIDE);
            }
            file.write(MUR);
            file.newLine();
        }
        //on fait la première ligne avec une sortie
        file.write(MUR);
        for (int i = 0; i < y-3; i++) {
            nombreRandom = Math.random();
            if (nombreRandom < 0.2)
                file.write(MUR);
            else
                file.write(VIDE);
        }
        file.write(SORTIE);
        file.write(MUR);
        file.newLine();
        //On met y fois un MUR à la dernière ligne
        for (int i = 0; i < y; i++) {
            file.write(MUR);
        }
        file.close();
    }

    /**
     * Méthode qui permet de sauvegarder un labyrinthe dans un fichier mazeBackup.txt
     * @param nomLaby le nom de fichier du labyrinthe qu'on va sauvegarder
     */
    public static void sauvegarderLaby(String nomLaby) throws IOException {
        // Le fichier source
        File src = new File(nomLaby);
        // Le fichier destination
        File dest = new File(NOM_BACKUP);
        // Créer l'objet File Reader
        FileReader fr = null;
        try {
            fr = new FileReader(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Créer l'objet BufferedReader
        BufferedReader br = new BufferedReader(fr);
        // Créer l'objet File Writer
        FileWriter fw = null;
        try {
            fw = new FileWriter(dest, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String str = null;
        // Copie le contenu dans le nouveau fichier
        while((str = br.readLine()) != null)
        {
            fw.write(str);
            fw.write("\n");
            fw.flush();
        }
        fw.close();
    }

}
