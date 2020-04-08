import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node goal;
    private final Stack<Board> solutions;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solutions = new Stack<>();

        MinPQ<Node> minPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));

        MinPQ<Node> minPQTwin = new MinPQ<>();
        minPQTwin.insert(new Node(initial.twin(), 0, null));

        while (true) {
            Node node = minPQ.delMin();
            Node twin = minPQTwin.delMin();

            if (node.board.isGoal() || twin.board.isGoal()) {
                if (node.board.isGoal()) goal = node;
                else goal = null;
                break;
            }

            for (Board b : node.board.neighbors()) {
                Node newNode = new Node(b, node.moves + 1, node);
                if (node.getPrev() == null || !node.getPrev().getBoard().equals(newNode.getBoard()))
                    minPQ.insert(newNode);
            }

            for (Board b : twin.board.neighbors()) {
                Node newNode = new Node(b, twin.moves + 1, twin);
                if (twin.getPrev() == null || !twin.getPrev().getBoard().equals(newNode.getBoard()))
                    minPQTwin.insert(new Node(b, twin.moves + 1, twin));
            }
        }

        if (goal != null) {
            Node cur = goal;
            solutions.push(cur.board);
            while (cur.getPrev() != null) {
                cur = cur.getPrev();
                solutions.push(cur.board);
            }
        }
    }

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node prev;
        private final int priority;

        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
        }

        public int priority() {
            return priority;
        }

        @Override
        public int compareTo(Node that) {
            return Integer.compare(this.priority, that.priority);
        }

        public Board getBoard() {
            return board;
        }

        public void setBoard(Board board) {
            this.board = board;
        }

        public int getMoves() {
            return moves;
        }

        public void setMoves(int moves) {
            this.moves = moves;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goal != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!isSolvable()) return -1;
        return goal.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        System.out.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);
//
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
