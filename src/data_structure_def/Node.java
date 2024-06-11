package data_structure_def;

import java.util.HashSet;
import java.util.Set;

public class Node {

    private String label;
    private HashSet<Edge> edgeSet;

    public void setVertex(String label) {
        this.label = label;
        edgeSet = new HashSet<Edge>();
    }

    public String getVertex() {
        return label;
    }

    public void add(Edge edge) {
        edgeSet.add(edge);
    }

    public Set<Edge> getEdgeSet() {
        return (Set<Edge>) edgeSet.clone();
    }

    /*public boolean equals(Node graphnode) {
        return this.label.equals(graphnode.label) && this.edgeSet.equals(graphnode.edgeSet);
    }*/
}