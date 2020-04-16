import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

    private final CircularSuffixString[] sorted;

    private class CircularSuffixString {
        private final int offset;
        private final String s;

        public CircularSuffixString(String s, int offset) {
            this.s = s;
            this.offset = offset;
        }

        public int charAt(int d) {
            if (d < s.length()) return s.charAt((d + offset) % s.length());
            return -1;
        }

        public int getOffset() {
            return offset;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                ret.append(s.charAt((i + offset) % s.length()) + " ");
            }
            return ret.toString();
        }
    }

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        CircularSuffixString[] strings = new CircularSuffixString[s.length()];
        sorted = new CircularSuffixString[s.length()];
        for (int i = 0; i < s.length(); i++) {
            CircularSuffixString suffixString = new CircularSuffixString(s, i);
            strings[i] = suffixString;
            sorted[i] = suffixString;
        }
        sort(sorted, 0, s.length() - 1, 0);
    }

    private void exchange(CircularSuffixString[] strings, int i, int j) {
        CircularSuffixString temp = strings[i];
        strings[i] = strings[j];
        strings[j] = temp;
    }

    private void sort(CircularSuffixString[] strings, int lo, int hi, int d) {
        if (hi < lo) return;
        int i = lo + 1;
        int oldLo = lo;
        int oldHi = hi;
        int t = strings[lo].charAt(d);

        while (i <= hi) {
            if (strings[i].charAt(d) < t) exchange(strings, i++, lo++);
            else if (strings[i].charAt(d) > t) exchange(strings, i, hi--);
            else i++;
        }
        sort(strings, oldLo, lo - 1, d);
        if (t >= 0) sort(strings, lo, hi, d + 1);
        sort(strings, hi + 1, oldHi, d);
    }

    // length of s
    public int length() {
        return sorted.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException();
        return sorted[i].getOffset();
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }
}


