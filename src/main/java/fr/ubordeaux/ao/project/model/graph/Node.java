package fr.ubordeaux.ao.project.model.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un nœud (sommet) dans le graphe.
 * (Responsabilité : Personne 1)
 * * Rôle : Un nœud correspond à une seule case praticable dans le labyrinthe.
 * Il stocke sa position et la liste des arêtes (Edges)
 * qui le relient à ses voisins.
 */
public class Node {

    private final Position position;
    private final List<Edge> edges;

    public Node(Position position) {
        this.position = position;
        this.edges = new ArrayList<>();
    }

    /**
     * Ajoute une arête sortante de ce nœud.
     * @param edge L'arête (Edge) à ajouter.
     */
    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    // --- Getters ---

    public Position getPosition() {
        return position;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}