package graphe;

public class Arc {
    private String dest;
    private double cout;

    public Arc(String dest, double cout) {
        this.dest = dest;
        //si le cout est négatif, on le met à 0
        //si il est positif, on l'affecte au cout
        this.cout = cout > 0 ? cout : 0;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "" + getDest() + "(" + getCout() + ")";
    }
}