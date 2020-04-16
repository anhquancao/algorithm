import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static void shiftLeft(char[] s, int i) {
        while (i > 0) {
            char temp = s[i - 1];
            s[i - 1] = s[i];
            s[i] = temp;
            i -= 1;
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] seq = new char[256];
        for (int i = 0; i < 256; i++) {
            seq[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            for (char i = 0; i < 256; i++) {
                if (c == seq[i]) {
                    BinaryStdOut.write(i);
                    shiftLeft(seq, i);
                }

            }
        }
        BinaryStdOut.flush();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[256];
        for (int i = 0; i < 256; i++) {
            seq[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            BinaryStdOut.write(seq[i]);
            shiftLeft(seq, i);

        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("+")) {
            MoveToFront.decode();
        } else {
            MoveToFront.encode();
        }
    }
}
