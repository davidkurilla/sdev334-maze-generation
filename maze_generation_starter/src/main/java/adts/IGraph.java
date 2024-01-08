package adts;

import java.util.List;

/**
 * Defines a basic undirected graph.
 *
 * @author Josh Archer
 * @version 1.0
 * @param <V> the generic type for vertices in the graph
 */
public interface IGraph<V>
{
    /**
     * Adds a vertex to the graph.
     * @param element the new vertex value
     * @return true if the vertex was successfully added, or false
     * if this is a duplicate vertex.
     */
    boolean addVertex(V element);

    /**
     * Adds an undirected edge to the graph.
     * @param first the first vertex
     * @param second the second vertex
     * @return true if the edge was successfully added, or false
     * if this is a duplicate edge.
     */
    boolean addEdge(V first, V second);

    /**
     * Reports whether a vertex exists.
     * @param search the vertex to search for
     * @return true if the vertex exists, or false otherwise
     */
    boolean containsVertex(V search);

    /**
     * Reports whether an edge exists.
     * @param first the first vertex to search for
     * @param second the second vertex to search for
     * @return true if the edge exists, or false otherwise
     */
    boolean containsEdge(V first, V second);

    /**
     * Returns all adjacent vertices to the vertex parameter.
     *
     * @param vertex the vertex to search with
     * @return a list of adjacent vertices
     */
    List<V> getAdjacentVertices(V vertex);

    /**
     * Returns the number of vertices in the graph
     * @return the number of vertices
     */
    int vertexSize();

    /**
     * Returns the number of edges in the graph
     * @return the number of edges
     */
    int edgeSize();
}

