import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {
    private final int n;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = copyTiles(tiles);
    }


    // string representation of this board
    public String toString() {
        StringBuilder res = new StringBuilder(n + "\n");
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                res.append(tiles[r][c]).append(" ");
            }
            res.append("\n");
        }
        return res.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        int goal = 1;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int num = tiles[r][c];

//                System.out.println(goal + " " + num);
                if (goal != num && goal != 0) {
                    distance += 1;
                }
                goal = (goal + 1) % (n * n);
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int num = tiles[r][c];
                if (num != 0) {
                    int goalCol = (num - 1) % n;
                    int goalRow = (num - 1) / n;
                    int t = Math.abs(goalCol - c) + Math.abs(goalRow - r);
                    distance += t;
//                    System.out.println(num + ": " + r + "," + c + " " + goalRow + "," + goalCol + ": " + t);
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) return false;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (this.tiles[r][c] != that.tiles[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] copyTiles(int[][] tiles) {
        int[][] newTitles = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                newTitles[r][c] = tiles[r][c];
            }
        }
        return newTitles;
    }

    private Board exchange(int[][] tiles, int fromRow, int fromCol, int toRow, int toColumn) {
        int[][] newTiles = this.copyTiles(tiles);
        int oldValue = newTiles[fromRow][fromCol];
        newTiles[fromRow][fromCol] = newTiles[toRow][toColumn];
        newTiles[toRow][toColumn] = oldValue;
        return new Board(newTiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        int zeroColumn = 0;
        int zeroRow = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (this.tiles[r][c] == 0) {
                    zeroRow = r;
                    zeroColumn = c;
                }
            }
        }
        // top
        if (zeroRow > 0) {
            Board newBoard = exchange(tiles, zeroRow - 1, zeroColumn, zeroRow, zeroColumn);
            neighbors.add(newBoard);
        }

        // bottom
        if (zeroRow < n - 1) {
            Board newBoard = exchange(tiles, zeroRow + 1, zeroColumn, zeroRow, zeroColumn);
            neighbors.add(newBoard);

        }

        // left
        if (zeroColumn > 0) {
            Board newBoard = exchange(tiles, zeroRow, zeroColumn - 1, zeroRow, zeroColumn);
            neighbors.add(newBoard);
        }

        // right
        if (zeroColumn < n - 1) {
            Board newBoard = exchange(tiles, zeroRow, zeroColumn + 1, zeroRow, zeroColumn);
            neighbors.add(newBoard);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int r1 = -1;
        int c1 = -1;

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (c1 == -1 && this.tiles[r][c] != 0) {
                    r1 = r;
                    c1 = c;
                }
            }
        }

        // top
        if (r1 > 0 && (tiles[r1 - 1][c1] != 0)) {
            return exchange(tiles, r1 - 1, c1, r1, c1);
        }

        // bottom
        if (r1 < n - 1 && (tiles[r1 + 1][c1] != 0)) {
            return exchange(tiles, r1 + 1, c1, r1, c1);
        }

        // left
        if (c1 > 0 && (tiles[r1][c1 - 1] != 0)) {
            return exchange(tiles, r1, c1 - 1, r1, c1);
        }

        // right
        if (c1 < n - 1 && (tiles[r1][c1 + 1] != 0)) {
            return exchange(tiles, r1, c1 + 1, r1, c1);
        }
        return this;
    }

    // unit testing (not graded)
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
        System.out.println("Hamming: " + initial.hamming());
        System.out.println("Manhattan: " + initial.manhattan());
        System.out.println("Twin: " + initial.twin());

        initial.neighbors();
//        for (Board b : initial.neighbors()) {
//            System.out.println(b);
//        }
        // solve the puzzle
//        Solver solver = new Solver(initial);
//
//        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
//            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
//        }
    }

}
