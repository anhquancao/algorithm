import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {
    private final ST<String, Bag<Integer>> nounToIds;
    private final ArrayList<String> synsets;
    private final Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFileName, String hypernymsFileName) {
        if (synsetsFileName == null || hypernymsFileName == null) throw new IllegalArgumentException();

        In in = new In(synsetsFileName);
        synsets = new ArrayList<>();
        nounToIds = new ST<>();

        String st = in.readLine();
        while (st != null) {
            String[] fields = st.split("\\,");
            int id = Integer.parseInt(fields[0]);
            String[] nouns = fields[1].split(" ");
            synsets.add(id, fields[1]);
            for (String n : nouns) {
                if (!nounToIds.contains(n)) {
                    nounToIds.put(n, new Bag<>());
                }
                nounToIds.get(n).add(id);
            }
            st = in.readLine();
        }
        digraph = new Digraph(nounToIds.size());

        In hypterIn = new In(hypernymsFileName);
        st = hypterIn.readLine();
        while (st != null) {
            String[] fields = st.split("\\,");
            int from = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                int to = Integer.parseInt(fields[i]);
                digraph.addEdge(from, to);
            }
            st = hypterIn.readLine();
        }
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounToIds.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(digraph);
        return sap.length(nounToIds.get(nounA), nounToIds.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP sap = new SAP(digraph);
        int ancestorId = sap.ancestor(nounToIds.get(nounA), nounToIds.get(nounB));
        return synsets.get(ancestorId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");

//        while (!StdIn.isEmpty()) {
//            String v = StdIn.readString();
//            String w = StdIn.readString();
//        String v = "clostridia";
        String v = "clostridia";
        String w = "officer";
        int length = wordnet.distance(v, w);
        String ancestor = wordnet.sap(v, w);
        StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
//        }

    }
}
