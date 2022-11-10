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


    private int totalMoves;
    // private int priority;
    private Board initialBoard;
    private MinPQ<SearchNode> mPQ;

    Queue<Board> gameTree;

    private class SearchNode {
        private Board searchBoard;
        private Board prevBoard;
        private int moveCount;
        private int hammingVal;
        private int manhattanVal;

        private SearchNode(Board search, Board prev, int moves) {
            searchBoard = search;
            prevBoard = prev;
            moveCount = moves;
            hammingVal = search.hamming();
            manhattanVal = search.manhattan();
        }

        private int moves() {
            return moveCount;
        }

       /* private void 1incMoves() {
            moves++;
        }*/


    }

    private class NodeComparator implements Comparator<SearchNode> {

        public int compare(SearchNode a, SearchNode b) {
            int priManA = a.manhattanVal + a.moves();
            int priManB = b.manhattanVal + b.moves();
            int priHamA = a.hammingVal + a.moves();
            int priHamB = b.hammingVal + b.moves();

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
        totalMoves = 0;
        initialBoard = initial;
        mPQ = new MinPQ<SearchNode>(new NodeComparator());
        // gameTree.enqueue(initial);
        // mPQ.insert(initial);
        mPQ.insert(new SearchNode(initial, null, 0));
        while (true) {

            // Board minB = mPQ.delMin();
            SearchNode minNode = mPQ.delMin();
            gameTree.enqueue(minNode.searchBoard);
            if (minNode.searchBoard.isGoal()) {
                totalMoves = minNode.moves();
                break;
            }
            // moves++;
            if (minNode.moves() > 30000) {
                totalMoves = minNode.moves();
                break; // TODO: remove
            }
            Iterable<Board> neighbors = minNode.searchBoard.neighbors();

            for (Board neighbor : neighbors) {
                boolean uniqueBoard = true;
                for (Board goodBoard : gameTree)
                    if (goodBoard.equals(neighbor)) {
                        uniqueBoard = false;
                        break;
                    }
                // if (uniqueBoard) mPQ.insert(nb);
                if (uniqueBoard) {
                    // minB.incMoves();
                    mPQ.insert(new SearchNode(neighbor, minNode.searchBoard, minNode.moves() + 1));
                }
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
        return totalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        return gameTree;
    }

    // test client (see below)
    public static void main(String[] args) {
        // args[0] = "puzzle3x3-04.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-18.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-BenJ.txt"; // todo: DELETE ME later
        args[0] = "puzzle3x3-31.txt"; // todo: DELETE ME later
        // args[0] = "puzzle4x4-01.txt"; // todo: DELETE ME later
        // args[0] = "puzzle2x2-01.txt"; // todo: DELETE ME later 1-off
        // args[0] = "puzzle2x2-06.txt"; // todo: DELETE ME later 1-off

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
