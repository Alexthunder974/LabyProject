package graphe;

import java.util.List;

public interface Graphe{

    /**
     * Retourne tous les noeuds du graphe avec leur nom
     * @return les noms des noeuds du graphe
     */
    public List<String> listeNoeuds();

    /**
     * Retourne la liste des arcs partant du noeud passé en paramètre
     * @param n le noeud dont on veut connaitre les arcs adjacents
     * @return la liste des arcs adjacents au noeud
     */
    public List<Arc> suivants(String n);
}