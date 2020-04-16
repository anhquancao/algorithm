import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        StringBuilder t = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int offset = circularSuffixArray.index(i);
            if (offset == 0) BinaryStdOut.write(i);
            t.append(s.charAt((s.length() - 1 + offset) % s.length()));
        }
        BinaryStdOut.write(t.toString());
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();

        int[] next = new int[t.length()];
        int[] count = new int[256];

        for (int i = 0; i < t.length(); i++) {
            count[t.charAt(i)]++;
        }
        int[] index = new int[257];
        for (int i = 1; i < 257; i++) {
            index[i] = index[i - 1] + count[i - 1];
        }

        char[] firstCol = new char[t.length()];
        for (int i = 0; i < t.length(); i++) {
            firstCol[index[t.charAt(i)]] = t.charAt(i);
            next[index[t.charAt(i)]++] = i;
        }


        StringBuilder origin = new StringBuilder();
        for (int i = 0; i < t.length(); i++) {
            origin.append(firstCol[first]);
            first = next[first];
        }

        BinaryStdOut.write(origin.toString());
        BinaryStdOut.flush();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        } else {
            BurrowsWheeler.transform();
        }
    }
}
