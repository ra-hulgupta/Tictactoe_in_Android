package com.techsoldev.tictactoegame;

public class Minmax {

    // Retained for compatibility with existing code.
    static char player = 'x';
    static char opponent = 'o';

    public static class Move {
        public int row;
        public int col;

        public Move() {
            row = -1;
            col = -1;
        }

        public Move(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    // Existing API retained: assumes the computer plays X.
    public static Move findBestMove(char[][] board) {
        return findBestMove(board, 'x');
    }

    // New API: computer can play either X or O.
    public static Move findBestMove(char[][] board, char computerSymbol) {

        if (board == null || board.length != 3) {
            throw new IllegalArgumentException("Board must be a 3x3 array.");
        }

        for (int row = 0; row < 3; row++) {
            if (board[row] == null || board[row].length != 3) {
                throw new IllegalArgumentException("Board must be a 3x3 array.");
            }
        }

        computerSymbol = Character.toLowerCase(computerSymbol);

        if (computerSymbol != 'x' && computerSymbol != 'o') {
            throw new IllegalArgumentException("Computer symbol must be x or o.");
        }

        char humanSymbol = computerSymbol == 'x' ? 'o' : 'x';

        Move bestMove = new Move();
        int bestScore = Integer.MIN_VALUE;

        // Try every available position.
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (board[row][col] == '_') {

                    board[row][col] = computerSymbol;

                    int score = minimaxSearch(
                            board,
                            1,
                            false,
                            computerSymbol,
                            humanSymbol,
                            Integer.MIN_VALUE,
                            Integer.MAX_VALUE
                    );

                    // Undo the trial move.
                    board[row][col] = '_';

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(row, col);
                    }
                }
            }
        }

        return bestMove;
    }

    private static int minimaxSearch(
            char[][] board,
            int depth,
            boolean maximizing,
            char computerSymbol,
            char humanSymbol,
            int alpha,
            int beta) {

        if (hasWon(board, computerSymbol)) {
            return 10 - depth;
        }

        if (hasWon(board, humanSymbol)) {
            return depth - 10;
        }

        if (!isMovesLeft(board)) {
            return 0;
        }

        if (maximizing) {
            int best = Integer.MIN_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (board[row][col] == '_') {
                        board[row][col] = computerSymbol;

                        int score = minimaxSearch(
                                board,
                                depth + 1,
                                false,
                                computerSymbol,
                                humanSymbol,
                                alpha,
                                beta
                        );

                        board[row][col] = '_';

                        best = Math.max(best, score);
                        alpha = Math.max(alpha, best);

                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }

            return best;

        } else {
            int best = Integer.MAX_VALUE;

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {

                    if (board[row][col] == '_') {
                        board[row][col] = humanSymbol;

                        int score = minimaxSearch(
                                board,
                                depth + 1,
                                true,
                                computerSymbol,
                                humanSymbol,
                                alpha,
                                beta
                        );

                        board[row][col] = '_';

                        best = Math.min(best, score);
                        beta = Math.min(beta, best);

                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }

            return best;
        }
    }

    // Checks whether a specified symbol has won.
    private static boolean hasWon(char[][] board, char symbol) {

        for (int i = 0; i < 3; i++) {

            if (board[i][0] == symbol &&
                    board[i][1] == symbol &&
                    board[i][2] == symbol) {
                return true;
            }

            if (board[0][i] == symbol &&
                    board[1][i] == symbol &&
                    board[2][i] == symbol) {
                return true;
            }
        }

        return (board[0][0] == symbol &&
                board[1][1] == symbol &&
                board[2][2] == symbol)
                ||
               (board[0][2] == symbol &&
                board[1][1] == symbol &&
                board[2][0] == symbol);
    }

    // Retained API: true if an empty cell remains.
    public static Boolean isMovesLeft(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '_') {
                    return true;
                }
            }
        }

        return false;
    }

    // Retained API: evaluates positions from X's perspective.
    public static int evaluate(char[][] board) {
        if (hasWon(board, 'x')) {
            return 10;
        }

        if (hasWon(board, 'o')) {
            return -10;
        }

        return 0;
    }

    // Retained API for existing callers of minimax.
    public static int minimax(char[][] board, int depth, Boolean isMax) {

        if (Boolean.TRUE.equals(isMax)) {
            return legacySearch(board, depth, true);
        }

        return legacySearch(board, depth, false);
    }

    private static int legacySearch(char[][] board, int depth, boolean isMax) {

        if (hasWon(board, player)) {
            return 10 - depth;
        }

        if (hasWon(board, opponent)) {
            return depth - 10;
        }

        if (!isMovesLeft(board)) {
            return 0;
        }

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        char symbol = isMax ? player : opponent;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {

                if (board[row][col] == '_') {
                    board[row][col] = symbol;

                    int score = legacySearch(board, depth + 1, !isMax);

                    board[row][col] = '_';

                    best = isMax
                            ? Math.max(best, score)
                            : Math.min(best, score);
                }
            }
        }

        return best;
    }
}
