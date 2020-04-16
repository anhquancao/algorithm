import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {

    private final Digraph digraph;
    private int minDistance;
    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(edu.princeton.cs.algs4.Digraph G) {
        digraph = new Digraph(G);
    }

    private void findAncestor(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        minDistance = Integer.MAX_VALUE;
        ancestor = -1;
        for (int vertex = 0; vertex < digraph.V(); vertex++) {
            if (vPath.hasPathTo(vertex) && wPath.hasPathTo(vertex)) {
                int vDistance = vPath.distTo(vertex);
                int wDistance = wPath.distTo(vertex);

                int distance = vDistance + wDistance;
                if (distance < minDistance) {
                    minDistance = distance;
                    ancestor = vertex;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE) {
            minDistance = -1;
            ancestor = -1;
        }

    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        ArrayList<Integer> vs = new ArrayList<>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<>();
        ws.add(w);
        findAncestor(vs, ws);
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> vs = new ArrayList<>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<>();
        ws.add(w);
        findAncestor(vs, ws);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
        }
        for (Integer i : w) {
            if (i == null) throw new IllegalArgumentException();
        }
        findAncestor(v, w);
        return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        findAncestor(v, w);
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

}
