import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private ST<Integer, String> nodes;
    private Bag<Integer>[] adjs;
    private int numNodes = 0;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFileName, String hypernymsFileName) {

        In in = new In(synsetsFileName);
        nodes = new ST<Integer, String>();
        String st;
        while ((st = in.readLine()) != null) {
            String[] fields = st.split("\\,");
            int id = Integer.parseInt(fields[0]);
            nodes.put(id, fields[1]);
        }
        numNodes = nodes.size();
        adjs = new Bag[numNodes];
        for (int i = 0; i < numNodes; i++) {
            adjs[i] = new Bag<>();
        }

        In hypterIn = new In(hypernymsFileName);
        while ((st = hypterIn.readLine()) != null) {
            String[] fields = st.split("\\,");
            int from = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                adjs[from].add(Integer.parseInt(fields[i]));
            }

        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return null;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return true;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");

        System.out.println(wordnet.numNodes);
    }
}
