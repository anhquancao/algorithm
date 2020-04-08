import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.valueOf(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }


        for (String s : queue) {
            System.out.println(s);
            k -= 1;
            if (k == 0) break;
        }

    }
}
