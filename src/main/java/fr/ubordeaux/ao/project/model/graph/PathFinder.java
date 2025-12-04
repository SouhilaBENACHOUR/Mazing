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
        
        // Structures de données pour Dijkstra
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );

        // 1. Initialisation
        for (Node node : graph.nodes.values()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(startNode, 0.0);
        priorityQueue.add(startNode);

        // 2. Boucle principale de Dijkstra
        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            // Si on a atteint la destination
            if (currentNode.equals(endNode)) {
                break; // Chemin trouvé
            }

            // Parcourir les voisins
            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getTarget();
                double newDist = distances.get(currentNode) + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    // Un chemin plus court a été trouvé vers ce voisin
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, currentNode);
                    
                    // Mettre à jour la file de priorité
                    priorityQueue.remove(neighbor); // Retirer l'ancienne entrée (si elle existe)
                    priorityQueue.add(neighbor); // Ajouter la nouvelle avec la distance mise à jour
                }
            }
        }

        // 3. Reconstruction du chemin
        return reconstructPath(previousNodes, startNode, endNode);
    }

    /**
     * Remonte le chemin à partir de la carte des "previousNodes".
     * @return Une liste de positions du début à la fin.
     */
    private List<Position> reconstructPath(Map<Node, Node> previousNodes, Node startNode, Node endNode) {
        List<Position> path = new ArrayList<>();
        Node current = endNode;

        // Si endNode n'est pas dans previousNodes (et n'est pas startNode),
        // cela signifie qu'aucun chemin n'a été trouvé.
        if (previousNodes.get(current) == null && !current.equals(startNode)) {
            return path; // Retourne un chemin vide
        }
        
        // Remonter de la fin au début
        while (current != null) {
            path.add(current.getPosition());
            current = previousNodes.get(current);
        }

        // Le chemin est à l'envers (de la fin au début), il faut le retourner
        Collections.reverse(path);
        return path;
    }
}