package fr.ubordeaux.ao.project.model.graph;

import java.util.*;

/**
 * Implémente l'algorithme de Dijkstra pour trouver le chemin le plus court.
 * (Responsabilité : Personne 1)
 * * Rôle : Fournit une méthode findPath() que l'IA ennemie (P2)
 * pourra appeler pour chasser le joueur.
 */
public class PathFinder {

    /**
     * Trouve le chemin le plus court entre un nœud de départ et d'arrivée.
     * @param graph Le graphe du labyrinthe.
     * @param startNode Le nœud de départ (ex: position de l'ennemi).
     * @param endNode Le nœud d'arrivée (ex: position du joueur).
     * @return Une Liste de Positions représentant le chemin,
     * ou une liste vide si aucun chemin n'est trouvé.
     */
    public List<Position> findPath(MazeGraph graph, Node startNode, Node endNode) {
        

        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );


        for (Node node : graph.nodes.values()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(startNode, 0.0);
        priorityQueue.add(startNode);


        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();


            if (currentNode.equals(endNode)) {
                break;
            }


            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getTarget();
                double newDist = distances.get(currentNode) + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    

                    priorityQueue.remove(neighbor);
                    priorityQueue.add(neighbor);
                }
            }
        }


        return reconstructPath(previousNodes, startNode, endNode);
    }

    /**
     * Remonte le chemin à partir de la carte des "previousNodes".
     * @return Une liste de positions du début à la fin.
     */
    private List<Position> reconstructPath(Map<Node, Node> previousNodes, Node startNode, Node endNode) {
        List<Position> path = new ArrayList<>();
        Node current = endNode;

        if (previousNodes.get(current) == null && !current.equals(startNode)) {
            return path;
        }

        while (current != null) {
            path.add(current.getPosition());
            current = previousNodes.get(current);
        }


        Collections.reverse(path);
        return path;
    }
}