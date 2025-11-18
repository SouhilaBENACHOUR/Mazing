package fr.ubordeaux.ao.project.model.graph;



import fr.ubordeaux.ao.project.model.Maze;

import java.util.HashMap;
import java.util.Map;

/**
 * Représente le labyrinthe en tant que structure de graphe.
 * (Responsabilité : Personne 1)
 * * Rôle : Convertir la grille 2D (Maze) en un ensemble de Nœuds et d'Arêtes.
 * C'est cette structure qui sera utilisée par le PathFinder.
 */
public class MazeGraph {

    // Une Map est parfaite pour retrouver un Nœud à partir de sa Position
    final Map<Position, Node> nodes;

    public MazeGraph() {
        this.nodes = new HashMap<>();
    }

    /**
     * Construit le graphe à partir d'un objet Maze.
     * @param maze Le labyrinthe (grille) chargé depuis un fichier.
     */
    public void buildGraph(Maze maze) {
        nodes.clear(); // Vider le graphe précédent

        // --- 1ère Étape : Créer un Nœud pour chaque case praticable ---
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                if (maze.isWalkable(x, y)) {
                    Position pos = new Position(x, y);
                    Node node = new Node(pos);
                    nodes.put(pos, node);
                }
            }
        }

        // --- 2ème Étape : Créer les Arêtes entre les Nœuds voisins ---
        for (Node sourceNode : nodes.values()) {
            Position sourcePos = sourceNode.getPosition();
            
            // Vérifier les 4 voisins (Nord, Sud, Est, Ouest)
            for (Direction dir : Direction.values()) {
                Position neighborPos = sourcePos.getNeighbor(dir);
                
                // Vérifier si ce voisin existe dans notre Map de nœuds
                Node targetNode = nodes.get(neighborPos);
                
                if (targetNode != null) {
                    // Si le voisin est aussi un nœud praticable, créer une arête
                    Edge edge = new Edge(sourceNode, targetNode, 1.0); // Poids de 1
                    sourceNode.addEdge(edge);
                }
            }
        }
    }

    /**
     * Récupère le Nœud à une position donnée.
     * @return Le Nœud, ou null si la position n'est pas un nœud (ex: un mur).
     */
    public Node getNode(Position pos) {
        return nodes.get(pos);
    }
}