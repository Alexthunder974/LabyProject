package algorithme;

import graphe.Arc;
import graphe.Graphe;

import java.util.ArrayList;

public class Dijkstra implements Algorithme {

    /**
     * @param g      un graphe oriente avec une ponderation (poids) positive des arcs
     * @param depart un sommet (depart) de G
     * @return
     */
    public Valeur resoudre(Graphe g, String depart) {

        Valeur res = Algorithme.initialiser(g, depart);
        ArrayList<String> Q = new ArrayList<String>(g.listeNoeuds());


        while (Q.size() > 0) {
            String u = Q.get(0);
            for (String v : Q) {
                if (res.getValeur(v) < res.getValeur(u)) {
                    u = v;
                }
            }
            Q.remove(u);
            for (Arc a : g.suivants(u)) {
                if (res.getValeur(u) + a.getCout() < res.getValeur(a.getDest())) {
                    res.setValeur(a.getDest(), res.getValeur(u) + a.getCout());
                    res.setParent(a.getDest(), u);
                }
            }
        }

        return res;
    }

}