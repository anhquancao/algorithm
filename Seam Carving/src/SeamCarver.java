import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int[][] energies;
    private int[][] edgeTo;
    private int[][] distTo;
    private int width;
    private int height;
    private boolean isTranspose = false;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        extract(picture);
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    private int[] getRGB(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color) & 0xFF;
        return new int[]{r, g, b};
    }

    private double squaredGradient(int color1, int color2) {
        int[] rgb1 = getRGB(color1);
        int[] rgb2 = getRGB(color2);
        double grad = 0;
        for (int i = 0; i < 3; i++) {
            grad += Math.pow(rgb1[i] - rgb2[i], 2);
        }
        return grad;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }
        int left = picture.getRGB(x - 1, y);
        int right = picture.getRGB(x + 1, y);
        int top = picture.getRGB(x, y - 1);
        int bottom = picture.getRGB(x, y + 1);
        double gradX = squaredGradient(left, right);
        double gradY = squaredGradient(top, bottom);

        return Math.sqrt(gradX + gradY);
    }


    private int[] verticalSeam() {
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > 0 && distTo[x][y] > distTo[x - 1][y - 1] + energies[x][y]) {
                    edgeTo[x][y] = -1;
                    distTo[x][y] = distTo[x - 1][y - 1] + energies[x][y];
                }

                if (distTo[x][y] > distTo[x][y - 1] + energies[x][y]) {
                    edgeTo[x][y] = 0;
                    distTo[x][y] = distTo[x][y - 1] + energies[x][y];
                }

                if (x < width - 1 && distTo[x][y] > distTo[x + 1][y - 1] + energies[x][y]) {
                    edgeTo[x][y] = 1;
                    distTo[x][y] = distTo[x + 1][y - 1] + energies[x][y];
                }

            }
        }
        double minDistance = Double.POSITIVE_INFINITY;
        int minX = -1;
        for (int x = 0; x < width; x++) {
            if (distTo[x][height - 1] < minDistance) {
                minDistance = distTo[x][height - 1];
                minX = x;
            }
        }

        int[] stream = new int[height];
        int y = height - 1;
        stream[y] = minX;
        while (y > 0) {
            stream[y - 1] = stream[y] + edgeTo[stream[y]][y];
            y -= 1;
        }

        return stream;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
//        if (!isTranspose)
        transpose();
        int[] seam = verticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
//        if (isTranspose) transpose();
        return verticalSeam();
    }

    private void extract(Picture picture) {
        this.picture = picture;
        width = picture.width();
        height = picture.height();

        energies = new double[width][height];
        edgeTo = new int[width][height];
        distTo = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energies[x][y] = energy(x, y);
                if (y == 0) distTo[x][y] = energies[x][y];
                else distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
    }

    private void transpose() {
        this.isTranspose = !isTranspose;
        Picture transposed = new Picture(picture.height(), picture.width());

        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                transposed.set(y, x, picture.get(x, y));
            }
        }
        extract(transposed);

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width || height <= 1) throw new IllegalArgumentException();
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height || width <= 1) throw new IllegalArgumentException();
        Picture res = new Picture(width - 1, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                if (x >= seam[y]) {
                    res.set(x, y, picture.get(x + 1, y));
                } else {
                    res.set(x, y, picture.get(x, y));
                }
            }
        }
        extract(res);
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }
}
