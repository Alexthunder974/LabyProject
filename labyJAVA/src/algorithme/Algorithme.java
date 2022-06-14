package algorithme;

import graphe.Graphe;

import java.util.ArrayList;

public interface Algorithme {

    Valeur resoudre(Graphe g, String depart);

    static Valeur initialiser(Graphe g, String depart) {
        Valeur res = new Valeur();
        ArrayList<String> Q = new ArrayList<String>(g.listeNoeuds());

        for (String v : Q) {
            res.setValeur(v, Double.MAX_VALUE);
            res.setParent(v, null);
        }
        res.setValeur(depart, 0);
        return res;
    }

}