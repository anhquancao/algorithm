/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


public class Percolation {
    private boolean[][] grid;
    private int numberOfOpenSites;
    private int[] items;
    private int topNode;
    private int bottomNode;
    private int size;
    private int[] treesSize;
    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be bigger than 0");
        this.n = n;
        this.grid = new boolean[n][n];
        this.numberOfOpenSites = 0;
        this.size = n * n + 2;
        this.topNode = this.size - 2;
        this.bottomNode = this.size - 1;
        this.items = new int[this.size];
        this.treesSize = new int[this.size];

        for (int i = 0; i < this.size; i++) {
            this.items[i] = i;
            this.treesSize[i] = 1;
        }

    }

    private int root(int i) {
        while (i != items[i]) {
            items[i] = items[items[i]];
            i = items[i];
        }
        return i;
    }

    private void union(int i1, int i2) {
        // System.out.println("Union: " + i1 + ", " + i2);
        int root1 = this.root(i1);
        int root2 = this.root(i2);
        if (root1 == root2) return;
        if (treesSize[root1] > treesSize[root2]) {
            items[root2] = root1;
            treesSize[root2] += treesSize[root1];
        }
        else {
            items[root1] = root2;
            treesSize[root1] += treesSize[root2];
        }

    }

    private boolean connected(int i, int j) {
        return this.root(i) == this.root(j);
    }

    private int positionToId(int x, int y) {
        return x * this.n + y;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > this.n || row < 1) {
            throw new IllegalArgumentException();
        }
        if (col > this.n || col < 1) {
            throw new IllegalArgumentException();
        }
        int x = row - 1;
        int y = col - 1;
        if (!this.grid[x][y]) {
            this.grid[x][y] = true;
            this.numberOfOpenSites += 1;

            if (row == 1) {
                this.union(positionToId(x, y), this.topNode);
            }

            if (row == this.n) {
                this.union(positionToId(x, y), this.bottomNode);
            }


            // left
            if (col > 1 && this.isOpen(row, col - 1)) {
                // System.out.println("left");
                this.union(positionToId(x, y - 1), positionToId(x, y));
            }

            // right
            if (col <= n - 1 && this.isOpen(row, col + 1)) {
                // System.out.println("right");
                this.union(positionToId(x, y + 1), positionToId(x, y));
            }

            // top
            if (row > 1 && this.isOpen(row - 1, col)) {
                // System.out.println("top");
                this.union(positionToId(x - 1, y), positionToId(x, y));
            }

            // bottom
            if (row <= n - 1 && this.isOpen(row + 1, col)) {
                // System.out.println("bottom");
                this.union(positionToId(x + 1, y), positionToId(x, y));
            }

        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > this.n || row < 1) {
            throw new IllegalArgumentException();
        }
        if (col > this.n || col < 1) {
            throw new IllegalArgumentException();
        }
        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > this.n || row < 1) {
            throw new IllegalArgumentException();
        }
        if (col > this.n || col < 1) {
            throw new IllegalArgumentException();
        }
        int node = this.positionToId(row - 1, col - 1);
        boolean isFull = this.connected(this.topNode, node);
        // System.out.println("isFull: " + "(" + row + ", " + col + "): " + isFull);

        return isFull;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.connected(this.topNode, this.bottomNode);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(5);
        percolation.open(1, 2);
        percolation.open(2, 2);
        percolation.isFull(1, 2);
        percolation.isFull(2, 2);
    }
}
