import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * classe de test fournie destinee a verifier les methodes
 */
public class TestLabyProf {

    /**
     * test de chargement + getChar + Constantes
     * (NE PAS MODIFIER)
      */
    @Test
    public void test_charger() throws Exception{
        // utilise laby0.txt fourni
        Labyrinthe l = Labyrinthe.chargerLabyrinthe("laby/laby0.txt");

        // verifie labyrinthe
        assertEquals(l.getChar(0,0),Labyrinthe.MUR);
        assertEquals(l.getChar(1,1),Labyrinthe.SORTIE);
        assertEquals(l.getChar(2,3),Labyrinthe.PJ);
        assertEquals(l.getChar(2,1),Labyrinthe.VIDE);
    }


    /**
     * test des methodes publiques
     * (NE PAS MODIFIER)
      */
    @Test
    public void test_methodes() throws Exception{
        // utilise laby0.txt fourni
        Labyrinthe l = Labyrinthe.chargerLabyrinthe("laby/laby0.txt");

        // getsuivant et constantes action
        int[] res;
        res = Labyrinthe.getSuivant(1,1,Labyrinthe.HAUT);
        res = Labyrinthe.getSuivant(1,1,Labyrinthe.BAS);
        res = Labyrinthe.getSuivant(1,1,Labyrinthe.DROITE);
        res = Labyrinthe.getSuivant(1,1,Labyrinthe.GAUCHE);

        // deplacerPerso
        l.deplacerPerso(Labyrinthe.HAUT);

        // etrefini
        boolean b = l.etreFini();

        // toString
        String s = l.toString();
    }

}
