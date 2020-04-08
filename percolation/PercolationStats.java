/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] percolatesTime;
    private int trials;

    public PercolationStats(int n, int trials) {
        percolatesTime = new double[trials];
        this.trials = trials;
        for (int t = 0; t < trials; t++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
                this.percolatesTime[t] = percolation.numberOfOpenSites() * 1.0 / (n * n);
                // System.out.println(this.percolatesTime[t] + ", " + percolation.percolates());
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.percolatesTime);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.percolatesTime);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - 1.96 * this.stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + 1.96 * this.stddev() / Math.sqrt(trials);

    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(200, 100);
        System.out.println(percolationStats.mean());
        System.out.println(percolationStats.stddev());
        System.out.println(
                "[" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi()
                        + "]");
    }
}
