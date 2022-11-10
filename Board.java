/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private int[][] boardTiles;
    private int n;
    // private int hammingCount;
    // private int manhattanCount;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // hammingCount = -1;
        // manhattanCount = -1;

        n = tiles.length;
        boardTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                boardTiles[i][j] = tiles[i][j];
        // hammingCount = hamming();
        // manhattanCount = manhattan();
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
        // if (hammingCount >= 0) return hammingCount; // return cached value
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
        // if (manhattanCount >= 0) return manhattanCount; // return cached value
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
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board boardY = (Board) y;
        if (this.dimension() != boardY.dimension()) return false;
        return Arrays.deepEquals(this.boardTiles, boardY.boardTiles);
        /*
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.boardTiles[i][j] != boardY.boardTiles[i][j]) return false;
        return true;*/
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> boards = new Queue<Board>();
        int[] rowColSpacetile = locateSpacetile(this);
        if (rowColSpacetile == null)
            return boards; // houston, we have a problem (if we don't have an "open" tile space)

        int row = rowColSpacetile[0];
        int col = rowColSpacetile[1];

        // check North
        if (row > 0) {
            Board nBoard = new Board(this.boardTiles);
            nBoard.swapTiles(nBoard, row, col, row - 1, col);
            boards.enqueue(nBoard);
        }
        // check South
        if (row < dimension() - 1) {
            Board sBoard = new Board(this.boardTiles);
            sBoard.swapTiles(sBoard, row, col, row + 1, col);
            boards.enqueue(sBoard);
        }
        // check West
        if (col > 0) {
            Board wBoard = new Board(this.boardTiles);
            wBoard.swapTiles(wBoard, row, col - 1, row, col);
            boards.enqueue(wBoard);
        }
        // check East
        if (col < dimension() - 1) {
            Board eBoard = new Board(this.boardTiles);
            eBoard.swapTiles(eBoard, row, col + 1, row, col);
            boards.enqueue(eBoard);
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        // int[][] twinArray = new int[n][n];
        Board twinBoard = new Board(new int[n][n]);
        int row1 = -1;
        int col1 = -1;
        int row2 = -1;
        int col2 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinBoard.boardTiles[i][j] = boardTiles[i][j];
                if ((row1 < 0) && (col1 < 0) && (twinBoard.boardTiles[i][j] != 0)) {
                    row1 = i;
                    col1 = j;
                }
                if ((row1 >= 0) && (col1 >= 0) && (twinBoard.boardTiles[i][j] != 0)) {
                    row2 = i;
                    col2 = j;
                }
            }
        }
        swapTiles(twinBoard, row1, col1, row2, col2);
        return twinBoard;
    }

    private int[] locateSpacetile(Board board) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board.boardTiles[i][j] == 0) return new int[] { i, j };
        return null; // not bullet-proof, but should not happen with our clean inputs...
    }

    private void swapTiles(Board b, int row1, int col1, int row2, int col2) {
        int tmp = b.boardTiles[row1][col1];
        b.boardTiles[row1][col1] = b.boardTiles[row2][col2];
        b.boardTiles[row2][col2] = tmp;
        /* hammingCount = -1;
        manhattanCount = -1;
        hammingCount = hamming();
        manhattanCount = manhattan(); // reset*/
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read in the board specified in the filename

        // args[0] = "puzzle3x3-04.txt"; // todo: DELETE ME later
        args[0] = "puzzle3x3-02.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-18.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-BenJ.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-07.txt"; // todo: DELETE ME later
        // args[0] = "puzzle4x4-01.txt"; // todo: DELETE ME later
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

            Iterable<Board> neighborBoards = initial.neighbors();
            for (Board neighbor : neighborBoards) {
                StdOut.println("Neighbor: " + neighbor.toString());
            }
        }
    }
}
