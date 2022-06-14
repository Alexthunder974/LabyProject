package graphe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Noeud {
    /**
     * nom du Noeud
     */
    private String nom;
    /**
     * Liste des arcs partants du noeud.
     */
    private List<Arc> adj;


    public Noeud(String nom) {
        this.nom = nom;
        this.adj = new ArrayList<Arc>();
    }

    public void ajouterArc(String destination, double cout){
        Arc a = new Arc(destination,cout);
        adj.add(a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Noeud)) return false;
        Noeud noeud = (Noeud) o;
        return getNom().equals(noeud.getNom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNom(), getAdj());
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Arc> getAdj() {
        return adj;
    }

    public void setAdj(ArrayList<Arc> adj) {
        this.adj = adj;
    }
}