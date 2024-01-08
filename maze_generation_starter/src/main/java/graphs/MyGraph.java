package graphs;

import adts.IGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An undirected graph implementation using adjacency lists.
 *
 * @author Josh Archer
 * @version 1.0
 * @param <V> the type of vertices in the graph
 */
public class MyGraph<V> implements IGraph<V>
{
    private Map<V, Node> adjacencyLists = new HashMap<>();
    private int edgeCount = 0;

    @Override
    public boolean addVertex(V element)
    {
        //the vertices are part of a set
        if (containsVertex(element))
        {
            return false;
        }

        //add it (the element and a null head reference in the LL)
        adjacencyLists.put(element, null);
        return true;
    }

    @Override
    public boolean addEdge(V first, V second)
    {
        //edges are part of a set (no duplicates)
        if (containsEdge(first, second))
        {
            return false;
        }

        addDirectedEdge(first, second);
        addDirectedEdge(second, first);
        edgeCount++;

        return true;
    }

    private void addDirectedEdge(V first, V second)
    {
        Node oldHead = adjacencyLists.get(first);
        if (oldHead == null)
        {
            adjacencyLists.put(first, new Node(second));
        }
        else
        {
            //put a new node at the start o fthe linked list
            adjacencyLists.put(first, new Node(second, oldHead));
        }
    }

    @Override
    public boolean containsVertex(V search)
    {
        return adjacencyLists.containsKey(search);
    }

    @Override
    public boolean containsEdge(V first, V second)
    {
        //preconditions? - make sure the vertices are in the graph
        if (containsVertex(first) && containsVertex(second))
        {
            //step #1 - get the adjacency list of first
            Node current = adjacencyLists.get(first);

            //step #2 - loop over the list and look for second in the list
            while (current != null)
            {
                if (current.otherVertex.equals(second))
                {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    @Override
    public List<V> getAdjacentVertices(V vertex)
    {
        if (!containsVertex(vertex))
        {
            return new ArrayList<>();
        }

        List<V> adjacentVerts = new ArrayList<>();

        Node list = adjacencyLists.get(vertex);
        while (list != null)
        {
            adjacentVerts.add(list.otherVertex);
            list = list.next;
        }

        return adjacentVerts;
    }

    @Override
    public int vertexSize()
    {
        return adjacencyLists.size();
    }

    @Override
    public int edgeSize()
    {
        return edgeCount;
    }

    @Override
    public String toString()
    {
        return "Graph {" + "vertices: " + vertexSize() +
                ", edges: " + edgeSize() + "}";
    }

    //inner classes
    private class Node
    {
        //data in the node
        private V otherVertex;
        private Node next;

        public Node(V otherVertex)
        {
            this.otherVertex = otherVertex;
        }

        public Node(V otherVertex, Node next)
        {
            this.otherVertex = otherVertex;
            this.next = next;
        }

        @Override
        public String toString()
        {
            return "Node: " + otherVertex + " -> " + next;
        }
    }
}