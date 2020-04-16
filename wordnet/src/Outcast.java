import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxDistance = Integer.MIN_VALUE;
        String result = null;
        for (String n1 : nouns) {
            int distance = 0;
            for (String n2 : nouns) {
                int t = wordnet.distance(n1, n2);
                distance += t;
//                System.out.println(n1 + ", " + n2 + " " + t);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                result = n1;
            }

        }
        return result;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 0; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
