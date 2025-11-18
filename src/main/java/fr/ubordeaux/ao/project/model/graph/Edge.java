package fr.ubordeaux.ao.project.model.graph;

/**
 * Représente une arête (arc) dans le graphe.
 * (Responsabilité : Personne 1)
 * * Rôle : Connecte deux Nœuds (source et cible) avec un poids.
 * Dans un labyrinthe simple, le poids est toujours 1.
 */
public class Edge {

    private final Node source;
    private final Node target;
    private final double weight; // Poids pour Dijkstra (sera 1.0)

    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    // --- Getters ---

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }
}