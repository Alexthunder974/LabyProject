package graphe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GrapheListe implements Graphe{
    private List<String> ensNom;
    private List<Noeud> ensNoeuds;

    public GrapheListe() {
        this.ensNom = new ArrayList<String>();
        this.ensNoeuds = new ArrayList<Noeud>();
    }



    public GrapheListe(String Fichier) throws IOException {
        this.ensNom = new ArrayList<String>();
        this.ensNoeuds = new ArrayList<Noeud>();

        FileReader r = new FileReader(Fichier);
        BufferedReader br = new BufferedReader(r);

        String ligne = br.readLine();
        while (ligne != null){
            String[] tabS = ligne.split("\t");
            ajouterArc(tabS[0], tabS[1], Double.parseDouble(tabS[2]));
            ligne = br.readLine();
        }


    }

    @Override
    public List<String> listeNoeuds() {
        return this.ensNom;
    }

    @Override
    public List<Arc> suivants(String n) {
        List<Arc> arcs = new ArrayList<Arc>();
        for (Noeud noeud : this.ensNoeuds) {
            if (noeud.equals(new Noeud(n))) {
                arcs= noeud.getAdj();
            }
        }
        return arcs;
    }

    @Override
    public String toString() {
        String res = "";
        for (Noeud ensNoeud : ensNoeuds) {
            if (ensNoeud.getAdj().size()!=0) {
                res += ensNoeud.getNom() + " -> ";
                for (Arc arc : ensNoeud.getAdj()) {
                    res += arc.getDest() + "(" + arc.getCout() + ") ";
                }
                res += "\n";
            }
        }

        return res;
    }

    /**
     *
     * @param depart le nom du noeud de départ
     * @param destination le nom du noeud d'arrivée
     * @param cout le cout de l'arc
     */
    public void ajouterArc(String depart, String destination, double cout){
        //on cree un nouveau noeuds de depart et de destination
        Noeud nDepart = new Noeud(depart);
        Noeud nDestination = new Noeud(destination);

        //on verifie si les noeuds de depart et de destination existent deja
        boolean existeDepart = false;
        boolean existeDestination = false;
        for (Noeud n : ensNoeuds){
            if (nDepart.equals(n)){
                nDepart = n;
                existeDepart = true;
            }else if (nDestination.equals(n)){
                nDestination = n;
                existeDestination = true;
            }
        }
        //si les noeuds n'existent pas, on les ajoute
        if (!existeDepart){
            ensNoeuds.add(nDepart);
            ensNom.add(depart);
        }
        if (!existeDestination){
            ensNoeuds.add(nDestination);
            ensNom.add(destination);
        }

        //a la fin on ajoute l'arc
        nDepart.ajouterArc(destination, cout);

    }

    public String toGraphViz() {
        String res = "digraph {\n";
        for (Noeud ensNoeud : ensNoeuds) {
            for (Arc arc : ensNoeud.getAdj()) {
                res += ensNoeud.getNom() + " -> " + arc.getDest() + "[label = "+ arc.getCout() +"]\n";
            }
        }
        return res+="}";
    }

}