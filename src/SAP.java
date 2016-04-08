
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

/**
 * 
 * @author Weituo
 * 
 */

public class SAP {
    private final Digraph graph;
    private Map<String, SAPProcessor> cache;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        graph = new Digraph(g);
        cache = new HashMap<String, SAPProcessor>();
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException("not valid vertex");
        }
        return cachedResult(v, w).distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException("not valid vertex");
        }
        return cachedResult(v, w).ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException("not valid vertex");
        }
        return cachedResult(v, w).distance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException("not valid vertex");
        }
        return cachedResult(v, w).ancestor;   
    }
        
               
    private boolean validIndex(int i) {
        if (i < 0 || i >= graph.V()) {
            return false;
        }
        return true;
    }
    
    private boolean validIndex(Iterable<Integer> vertices) {
        for (int vertex : vertices) {
            if (!validIndex(vertex)) {
                return false;
            }
        }
        return true;
    }
    
    private SAPProcessor cachedResult(int v, int w) {
        String k = v + "_" + w;
        if (cache.containsKey(k)) {
            SAPProcessor p = cache.get(k);
            cache.remove(k);
            return p;
        }
        SAPProcessor p = new SAPProcessor(v ,w);
        cache.put(k, p);
        return p;   
    }
    
    private SAPProcessor cachedResult(Iterable<Integer> v, Iterable<Integer> w) {
        String k = v + "_" + w;
        if (cache.containsKey(k)) {
            SAPProcessor p = cache.get(k);
            cache.remove(k);
            return p;
        }
        SAPProcessor p = new SAPProcessor(v ,w);
        cache.put(k, p);
        return p;   
    }   
    
    
    private class SAPProcessor {
        int ancestor;
        int distance;
        
        public SAPProcessor(int v, int w) {
            BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(graph, v);
            BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(graph, w);
            
            process(a, b);
        }
        
        public SAPProcessor(Iterable<Integer> v, Iterable<Integer> w) {
            BreadthFirstDirectedPaths a = new BreadthFirstDirectedPaths(graph, v);
            BreadthFirstDirectedPaths b = new BreadthFirstDirectedPaths(graph, w);
            
            process(a, b);            
        
        }
        
        private void process(BreadthFirstDirectedPaths a, BreadthFirstDirectedPaths b) {
            List<Integer> ancestors = new ArrayList<Integer>();
            for (int i = 0; i< graph.V(); i++) {
                if (a.hasPathTo(i) && b.hasPathTo(i)) {
                    ancestors.add(i);
                }
            }
            
            int shortestAncestor = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int ancestor : ancestors) {
                int dist = a.distTo(ancestor) + b.distTo(ancestor);
                if (dist < minDistance) {
                    minDistance = dist;
                    shortestAncestor = ancestor;
                }
            }
            
            if (minDistance == Integer.MAX_VALUE) {
                distance = -1;
            } else {
                distance = minDistance;
            }
            ancestor = shortestAncestor;      
        }
    
    
    }

    // do unit testing of this class
    //public static void main(String[] args)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }


}