/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {


    private int totalMoves;
    private SearchNode primeNode;
    // private int priority;
    // private boolean solveable;

    // private Queue<Board> gameTree;

    private static class SearchNode implements Comparable<SearchNode> {
        private Board searchBoard;
        private SearchNode prevNode;
        private int moveCount;
        // private int hammingVal;
        private int manhattanVal;

        private SearchNode(Board search, SearchNode prev, int moves) {
            searchBoard = search;
            prevNode = prev;
            moveCount = moves;
            // hammingVal = search.hamming();
            manhattanVal = search.manhattan();
        }

        private int moves() {
            return moveCount;
        }

        /* public int compare(SearchNode a, SearchNode b) {
            int priManA = a.manhattanVal + a.moves();
            int priManB = b.manhattanVal + b.moves();

            return Integer.compare(priManA, priManB);
        }*/

        public int compareTo(SearchNode that) {
            int priManA = this.manhattanVal + this.moves();
            int priManB = that.manhattanVal + that.moves();
            return Integer.compare(priManA, priManB);
        }

       /* private void 1incMoves() {
            moves++;
        }*/


    }

    /* private class NodeComparator implements Comparator<SearchNode> {

        public int compare(SearchNode a, SearchNode b) {
            int priManA = a.manhattanVal + a.moves();
            int priManB = b.manhattanVal + b.moves();
            // int priHamA = a.hammingVal + a.moves();
            // int priHamB = b.hammingVal + b.moves();

            // if (priManA < priManB) return -1;
            // if (priManA > priManB) return 1;
            // priManA == priManB
            return Integer.compare(priManA, priManB);
        }
    }*/

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();

        // if unsolveable, the twin will be solved first
        Board twinBoard = initial.twin();

        MinPQ<SearchNode> mPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();

        mPQ.insert(new SearchNode(initial, null, 0));
        twinPQ.insert(new SearchNode(twinBoard, null, 0));
        while (true) {

            SearchNode minNode = mPQ.delMin();
            SearchNode twinNode = twinPQ.delMin();

            if (minNode.searchBoard.isGoal()) {
                primeNode = minNode;
                totalMoves = minNode.moves();
                break;
            }
            if (twinNode.searchBoard.isGoal()) {
                primeNode = null;
                totalMoves = -1;
                break;
            }

            Iterable<Board> neighbors = minNode.searchBoard.neighbors();
            Iterable<Board> twinNeighbors = twinNode.searchBoard.neighbors();

            for (Board neighbor : neighbors) {

                if ((minNode.prevNode == null) || (!neighbor.equals(minNode.prevNode.searchBoard)))
                    mPQ.insert(new SearchNode(neighbor, minNode, minNode.moves() + 1));

            }

            for (Board twinNeighbor : twinNeighbors) {

                if ((twinNode.prevNode == null) || (!twinNeighbor.equals(
                        twinNode.prevNode.searchBoard)))
                    twinPQ.insert(new SearchNode(twinNeighbor, twinNode, twinNode.moves() + 1));

            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return totalMoves > 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return totalMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> stack = new Stack<Board>();

        while (true) {
            stack.push(primeNode.searchBoard);
            if (primeNode.prevNode == null) break;
            primeNode = primeNode.prevNode;
        }
        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // args[0] = "puzzle3x3-04.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-19.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-18.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-BenJ.txt"; // todo: DELETE ME later
        // args[0] = "puzzle3x3-31.txt"; // todo: DELETE ME later
        // args[0] = "puzzle4x4-01.txt"; // todo: DELETE ME later
        // args[0] = "puzzle2x2-01.txt"; // todo: DELETE ME later 1-off
        // args[0] = "puzzle2x2-06.txt"; // todo: DELETE ME later 1-off
        args[0] = "puzzle2x2-unsolvable2.txt"; // todo: DELETE ME later 1-off

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
