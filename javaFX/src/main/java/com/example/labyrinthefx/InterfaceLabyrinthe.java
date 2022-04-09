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
    public static final int TAILLE = 40;
    public static final String NOM_LABY = "laby_test.txt";
    public static Labyrinthe laby;

    static {
        try {
            laby = Labyrinthe.chargerLabyrinthe(NOM_LABY);
        } catch (FichierIncorrectException e) {
            e.printStackTrace();
        }
    };
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


    @Override
    public void start(Stage stage) throws IOException {
        Pane racine = new Pane();

        //création du terrain de jeu, de la sortie et du personnage
        fairePlateau(racine);
        faireSortie(racine);
        Circle perso = fairePersonnage();
        racine.getChildren().add(perso);

        int taille_x = LABY_Y;
        int taille_y = LABY_X;

        Scene scene = new Scene(racine, TAILLE*taille_y, TAILLE*taille_x);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            //les vecteurs pour déplacer le personnage
            int deltaX=0;
            int deltaY=0;
            //on stock les anciennes positions pour calculer les vecteurs
            int anciennePosX = laby.getPersonnage().getX();
            int anciennePosY = laby.getPersonnage().getY();
            //on récupère la valeur de la touche du clavier
            KeyCode code = e.getCode();
            if (laby.etreFini()){
                code = KeyCode.getKeyCode("A");
            }
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

            if (laby.etreFini()){
                int x = TAILLE * LABY_Y / 4;
                int y = TAILLE * LABY_X / 4;
                Text fini = new Text(x, y,"Jeu Terminée");
                fini.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, TAILLE));
                fini.setFill(Color.WHITE);
                Rectangle backFini = new Rectangle(x-5, y-35, 260, 40);
                backFini.setFill(Color.GRAY);
                backFini.setArcHeight(10.0d);
                backFini.setArcWidth(10.0d);
                racine.getChildren().addAll(backFini, fini);
            }

        });
        Image icon = new Image("file:img/Bidoof.png");
        stage.getIcons().add(icon);
        stage.setTitle("Labyrinthe ricochet");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) throws IOException {
        Labyrinthe.creerLabyrinthe(15, 20, NOM_LABY);
        launch();
    }
}