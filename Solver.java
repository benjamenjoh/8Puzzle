/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {


    private int moves;
    // private int priority;
    private Board initialBoard;
    private MinPQ<Board> mPQ;
    Queue<Board> gameTree;

    private class BoardComparator implements Comparator<Board> {

        public int compare(Board a, Board b) {
            int priManA = a.manhattan() + moves;
            int priManB = b.manhattan() + moves;
            int priHamA = a.hamming() + moves;
            int priHamB = b.hamming() + moves;

            if (priManA < priManB) return -1;
            if (priManA > priManB) return 1;
            // priManA == priManB
            return Integer.compare(priHamA, priHamB);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        gameTree = new Queue<Board>();
        moves = 0;
        initialBoard = initial;
        mPQ = new MinPQ<Board>(new BoardComparator());
        // gameTree.enqueue(initial);
        mPQ.insert(initial);
        while (true) {

            Board minB = mPQ.delMin();
            gameTree.enqueue(minB);
            if (minB.isGoal()) break;
            moves++;
            if (moves > 100) break; // TODO: remove
            Iterable<Board> neighbors = minB.neighbors();
            for (Board nb : neighbors) {
                boolean uniqueBoard = true;
                for (Board goodBoard : gameTree)
                    if (goodBoard == nb) {
                        uniqueBoard = false;
                        break;
                    }
                if (uniqueBoard) mPQ.insert(nb);
            }

        }
        int x = 0;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        return gameTree;
    }

    // test client (see below)
    public static void main(String[] args) {
        // args[0] = "puzzle3x3-04.txt"; // todo: DELETE ME later
        args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-18.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-BenJ.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-07.txt"; // todo: DELETE ME later
        // args[0] = "puzzle4x4-01.txt"; // todo: DELETE ME later
        // args[0] = "puzzle2x2-01.txt"; // todo: DELETE ME later 1-off
        // args[0] = "puzzle2x2-02.txt"; // todo: DELETE ME later 1-off

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();

        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

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
