import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private final TrieSET dict;
    private boolean[][] marked;

    public BoggleSolver(String[] dictionary) {
        dict = new TrieSET();
        for (String s : dictionary) {
            dict.add(s);
        }
    }

    private void dfs(int i, int j, String s, BoggleBoard board, TrieSET results) {
        if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || marked[i][j]) return;


        if (!dict.hasPrefix(s)) return;

        marked[i][j] = true;


        if (s.length() >= 3 && dict.contains(s)) results.add(s);


        // top
        for (int r = -1; r <= 1; r++) {
            int nextI = i + r;
            if (nextI < 0 || nextI >= board.rows()) continue;
            for (int c = -1; c <= 1; c++) {

                int nextJ = j + c;
                if (nextJ < 0 || nextJ >= board.cols()) continue;
                dfs(nextI, nextJ, s + getLetter(board, nextI, nextJ), board, results);


            }
        }

        marked[i][j] = false;
    }

    private String getLetter(BoggleBoard board, int i, int j) {
        if (board.getLetter(i, j) == 'Q') {
            return "QU";
        }
        return board.getLetter(i, j) + "";
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        TrieSET results = new TrieSET();
        marked = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                dfs(i, j, getLetter(board, i, j), board, results);
            }
        }
        return results;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;
        int length = word.length();
        if (length == 3 || length == 4) return 1;
        if (length == 5) return 2;
        if (length == 6) return 3;
        if (length == 7) return 5;
        if (length >= 8) return 11;
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            count++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Count = " + count);

        StdOut.println("DANGLING = " + solver.scoreOf("DANGLING"));
    }

}
