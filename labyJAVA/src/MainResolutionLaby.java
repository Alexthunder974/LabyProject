import algorithme.Dijkstra;
import algorithme.Valeur;
import graphe.Graphe;
import graphe.GrapheListe;

public class MainResolutionLaby {

    public static void main(String[] args) throws ActionInconnueException {
        String nomLaby = "laby/laby1.txt";
        Labyrinthe laby = Labyrinthe.chargerLabyrinthe(nomLaby);

        GrapheListe g = laby.genererGraphe();
        System.out.println(g);

        Dijkstra d = new Dijkstra();
        String posPerso = "("+ laby.getPersonnage().getX() + "," + laby.getPersonnage().getY() + ")";
        String posSortie = "("+ laby.getSortie().getX() + "," + laby.getSortie().getY() + ")";
        Valeur v = d.resoudre(g, posPerso);
        System.out.println(v.calculerChemin(posSortie));
    }
}
