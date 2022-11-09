/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private int[][] boardTiles;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        boardTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                boardTiles[i][j] = tiles[i][j];
    }

    // string representation of this board
    public String toString() {
        // taken from https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", boardTiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boardTiles[i][j] == 0) continue;
                if (boardTiles[i][j] != (i * n) + j + 1) hammingCount++;
            }
        }
        return hammingCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // int[][] TEST = new int[3][3];
        int manhattanCount = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boardTiles[i][j] == 0) continue;
                if (boardTiles[i][j] == (i * n) + j + 1) continue;
                int goalRow = (boardTiles[i][j] - 1) / n;
                int goalCol = boardTiles[i][j] - (goalRow * n) - 1;
                manhattanCount += Math.abs(goalRow - i) + Math.abs(goalCol - j);
                // TEST[i][j] = Math.abs(goalRow - i) + Math.abs(goalCol - j);
            }
        }
        return manhattanCount;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y.getClass() != this.getClass()) return false;
        Board boardY = (Board) y;
        return Arrays.deepEquals(this.boardTiles, boardY.boardTiles);
        /*
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.boardTiles[i][j] != boardY.boardTiles[i][j]) return false;
        return true;*/
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] twinArray = new int[n][n];
        boolean swapComplete = false;
        int row1 = -1;
        int col1 = -1;
        int row2 = -1;
        int col2 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinArray[i][j] = boardTiles[i][j];
                if ((row1 < 0) && (col1 < 0) && (twinArray[i][j] != 0)) {
                    row1 = i;
                    col1 = j;
                }
                if ((row1 >= 0) && (col1 >= 0) && (twinArray[i][j] != 0)) {
                    row2 = i;
                    col2 = j;
                }
            }
        }
        int tmp = twinArray[row1][col1];
        twinArray[row1][col1] = twinArray[row2][col2];
        twinArray[row2][col2] = tmp;
        return new Board(twinArray);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read in the board specified in the filename
        // puzzle3x3-00.txt
        // args[0] = "puzzle3x3-04.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-00.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-BenJ.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-07.txt"; // todo: DELETE ME later
        args[0] = "puzzle4x4-01.txt"; // todo: DELETE ME later
        for (String filename : args) {
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();

                }
            }

            Board initial = new Board(tiles);
            Board copy = new Board(tiles);
            StdOut.println("Equals Identical Copy? " + initial.equals(copy));
            StdOut.println("Initial " + initial.toString());
            StdOut.println("Is goal? " + initial.isGoal());
            Board twin = initial.twin();
            StdOut.println("Twin " + twin.toString());
            StdOut.println("Equals Imperfect Twin? " + initial.equals(twin));

            StdOut.println("Hamming: " + String.valueOf(initial.hamming()));
            StdOut.println("Manhatten: " + String.valueOf(initial.manhattan()));
        }
    }
}
