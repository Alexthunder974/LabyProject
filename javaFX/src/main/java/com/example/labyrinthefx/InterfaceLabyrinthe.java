package com.example.labyrinthefx;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class InterfaceLabyrinthe extends Application {
    public static int TAILLE = 40;
    public static final String nomLaby = "laby/labyTest" + ".txt";
    public static Labyrinthe laby = Labyrinthe.chargerLabyrinthe(nomLaby);
    public static final int MILLIS = 20;
    public static final int LABY_Y = laby.getMurs().length;
    public static final int LABY_X = laby.getMurs()[0].length;


    /**
     * methode qui crée un carré de dimension taille
     * @param x la position x
     * @param y la position y
     * @param taille la taille du carré
     * @param color la couleur du carré
     * @return renvoie un carré
     */
    public Rectangle faireCase(int x, int y, int taille, Paint color, Paint stroke){
        Rectangle carre = new Rectangle(x, y, taille, taille);
        carre.setFill(color);
        carre.setStroke(stroke);
        return carre;
    }

    /**
     * methode qui crée le terrain de jeu
     * on fait un terrain de taille ligne * colonne
     */
    public void fairePlateau(Pane r){
        boolean[][] plateau = laby.getMurs();
        int x = plateau.length;
        int y = plateau[0].length;

        int taille_case = TAILLE;
        Rectangle case_tab = null;
        Paint color = null;
        //création du plateau
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if(plateau[i][j]) {
                    color = Color.BLACK;
                }
                else {
                    color = Color.WHITE;
                }
                case_tab = faireCase(j*taille_case, i*taille_case, taille_case, color, Color.WHITE);
                r.getChildren().add(case_tab);
            }
        }
    }

    /**
     * crée une sortie
     * @param r la racine
     */
    public void faireSortie(Pane r){
        Sortie sortie = laby.getSortie();
        int sortie_x = sortie.getX()*TAILLE;
        int sortie_y = sortie.getY()*TAILLE;
        Rectangle case_sortie = faireCase(sortie_y, sortie_x, TAILLE, Color.YELLOW, Color.BEIGE);
        Text text_sortie = new Text(sortie_y+TAILLE*0.25, sortie_x+TAILLE*0.75, "S");
        text_sortie.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, TAILLE*0.75));

        r.getChildren().addAll(case_sortie, text_sortie);
    }

    public Circle fairePersonnage(){
        Personnage perso = laby.getPersonnage();

        int perso_y = perso.getY() * TAILLE + (TAILLE / 2);
        int perso_x = perso.getX() * TAILLE + (TAILLE / 2);
        return new Circle(perso_y, perso_x, TAILLE/4, Color.BLUE);
    }

    /**
     * methode qui permet de dessiner une porte
     * @return un objet Rectangle représentant une porte
     */
    public Rectangle fairePorte(){
        Porte porte = laby.getPorte();
        int porte_x = porte.getX()*TAILLE;
        int porte_y = porte.getY()*TAILLE;
        Rectangle case_porte = faireCase(porte_y, porte_x, TAILLE, Color.SANDYBROWN, Color.BEIGE);
        return case_porte;
    }

    /**
     * methode qui permet de dessiner un bouton
     * @return un objet Circle qui représente un bouton
     */
    public Circle faireBouton(){
        Bouton bouton = laby.getPorte().getBouton();
        int bouton_x = bouton.getX()*TAILLE+(TAILLE/2);
        int bouton_y = bouton.getY()*TAILLE+(TAILLE/2);
        return new Circle(bouton_y, bouton_x, TAILLE/4, Color.RED);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Pane racine = new Pane();

        //création du terrain de jeu, de la sortie et du personnage
        fairePlateau(racine);
        faireSortie(racine);

        Rectangle porte = null;
        Circle bouton = null;
        if (laby.getPorte() != null){
            porte = fairePorte();
            bouton = faireBouton();
            racine.getChildren().addAll(porte, bouton);
        }

        Circle perso = fairePersonnage();
        racine.getChildren().add(perso);

        int taille_x = LABY_Y;
        int taille_y = LABY_X;

        Scene scene = new Scene(racine, TAILLE*taille_y, TAILLE*taille_x);

        Rectangle finalPorte = porte;
        Circle finalBouton = bouton;
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (laby.etreFini()){
                return;
            }
            //les vecteurs pour déplacer le personnage
            int deltaX=0;
            int deltaY=0;
            //on stock les anciennes positions pour calculer les vecteurs
            int anciennePosX = laby.getPersonnage().getX();
            int anciennePosY = laby.getPersonnage().getY();
            //on récupère la valeur de la touche du clavier
            KeyCode code = e.getCode();
            switch(code) {
                case UP:
                    try {
                        laby.deplacerPerso("haut");
                    } catch (ActionInconnueException ex) {
                        ex.printStackTrace();
                    }
                    deltaX = anciennePosX-laby.getPersonnage().getX();
                    break;
                case DOWN:
                    try {
                        laby.deplacerPerso("bas");
                    } catch (ActionInconnueException ex) {
                        ex.printStackTrace();
                    }
                    deltaX = anciennePosX-laby.getPersonnage().getX();
                    break;
                case LEFT:
                    try {
                        laby.deplacerPerso("gauche");
                    } catch (ActionInconnueException ex) {
                        ex.printStackTrace();
                    }
                    deltaY = anciennePosY-laby.getPersonnage().getY();
                    break ;
                case RIGHT:
                    try {
                        laby.deplacerPerso("droite");
                    } catch (ActionInconnueException ex) {
                        ex.printStackTrace();
                    }
                    deltaY = anciennePosY-laby.getPersonnage().getY();
                    break ;
                default:
                    deltaX = 0;
                    deltaY = 0;
            }
            TranslateTransition animation_perso = new TranslateTransition();
            int temps;
            if (deltaX != 0) {
                temps = MILLIS * Math.abs(deltaX);
            } else {
                temps = MILLIS * Math.abs(deltaY);
            }
            if (temps > 400) {
                temps = 400;
            }
            animation_perso.setDuration(Duration.millis(temps));

            animation_perso.setNode(perso);

            animation_perso.setByX(-deltaY * TAILLE);
            animation_perso.setByY(-deltaX * TAILLE);

            animation_perso.play();

            if (laby.getPorte() != null){
                if (laby.getPorte().etreOuvert()){
                    finalPorte.setFill(Color.LIGHTGRAY);
                    finalBouton.setFill(Color.LIGHTGREEN);
                }
                else{
                    finalPorte.setFill(Color.SANDYBROWN);
                    finalBouton.setFill(Color.RED);
                }
            }

            if (laby.etreFini()){
                Text fini = new Text(TAILLE*LABY_Y/4,TAILLE*LABY_X/4,"Jeu Terminée");
                fini.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, TAILLE));
                racine.getChildren().add(fini);
            }

        });

        Image icon = new Image("file:img/Bidoof.png");
        stage.getIcons().add(icon);
        stage.setTitle("Labyrinthe ricochet");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        if (LABY_X > 18 || LABY_Y > 25){
            TAILLE = 30;
        }
        launch();
    }
}