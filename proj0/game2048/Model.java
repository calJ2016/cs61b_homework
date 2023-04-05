package game2048;

import java.util.Enumeration;
import java.util.Formatter;
import java.util.Observable;


/**
 * The state of a game of 2048.
 *
 * @author lei
 */
public class Model extends Observable {
    /**
     * Current contents of the board.
     */
    private Board board;
    /**
     * Current score.
     */
    private int score;
    /**
     * Maximum score so far.  Updated when game ends.
     */
    private int maxScore;
    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces
     * and score 0.
     */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     * 0 <= COL < size(). Returns null if there is no tile there.
     * Used for testing. Should be deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board.
     * Used for testing. Should be deprecated and removed.
     */
    public int size() {
        return board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or
     * there is a tile with value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the
     * same position.
     */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE. Return true iff this changes the board.
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have
     * the same value, they are merged into one Tile of twice the original
     * value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     * tilt. So each move, every tile will only ever be part of at most one
     * merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     * value, then the leading two tiles in the direction of motion merge,
     * and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        board.setViewingPerspective(side);
        for (int col = 0; col < board.size(); col += 1) {
            changed = columnTilt(col, board.size()) || changed;
        }
        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }


    /**
     * Process the single column of the board
     * row_location:  current location to move.
     * merged: if merged, next tile don't need to merge.
     * pre_tile: use to track the previous tile if the col has some null tiles.
     * move_status: use to mark if motions have been occurred.
     */
    public boolean columnTilt(int col, int size) {
        int row_location = size - 1;
        int merged = 0; //0 - merge , 1- not merge
        Tile pre_tile = board.tile(col, size -1);
        boolean move_status = false;

        //2. from the second tile in the col, compare the current tile with the previous one.
        for (int row = size - 2; row >= 0; row -= 1) {
            Tile t0 = this.board.tile(col, row + 1);
            Tile t1 = this.board.tile(col, row);
            // 1. check the top tile is null or not.
            if (t0 == null && t1 == null) {
                continue;
            }
            if (t0 == null && t1 != null) {// check pre_tile is null or not.
                if (pre_tile != null) { //compare pre_tile and t1
                    if (merged == 0) {
                        if (pre_tile.value() == t1.value()) { // t1 == pre_tile merge motion
                            this.board.move(col, row_location, t1); // t1--> pre_tile
                            score += this.board.tile(col, row_location).value();
                            merged = 1;
                            move_status = true;
                            row_location = row_location - 1;
                            pre_tile = null;
                        } else { // t1 != pre_tile  no merge but move t1 to pre_tile.row() - 1
                            this.board.move(col, row_location - 1, t1);
                            pre_tile = board.tile(col, row_location - 1);
                            move_status = true;
                            row_location = row_location - 1;
                        }
                    }
                    else{
                        board.move(col, row_location, t1);
                        pre_tile = board.tile(col, row_location);
                        move_status = true;
                    }
                } else {
                    //move t1 to row_location
                    board.move(col, row_location, t1);
                    pre_tile = board.tile(col, row_location);
                    merged = 0;
                    move_status = true;
                }
            }
            if (t0 != null && t1 == null) {
                pre_tile = t0;
                row_location = row + 1;
            }
            if (t0 != null && t1 != null) {
                if (merged == 0) {
                    if (t0.value() == t1.value()) { // merge motion
                        board.move(col, row_location, t1); // t1--> pre_tile
                        score += board.tile(col, row_location).value();
                        merged = 1;
                        move_status = true;
                        row_location = row_location - 1;
                        pre_tile = null;
                    }
                    else {
                        pre_tile = t1;
                        row_location = row_location - 1;
                    }
                } else {
                    pre_tile = t1;
                    row_location = row_location - 1;
                }
            }
        }
        return move_status;
    }


    /**
     * Checks if the game is over and sets the gameOver variable
     * appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /**
     * Determine whether game is over.
     */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /**
     * Returns true if at least one space on the Board is empty.
     * Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col += 1) {
            for (int row = 0; row < size; row += 1) {
                Tile t = b.tile(col, row);
                if (t == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        int size = b.size();
        for (int col = 0; col < size; col += 1) {
            for (int row = 0; row < size; row += 1) {
                Tile t = b.tile(col, row);
                if (t != null) {
                    if (t.value() == MAX_PIECE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        //there is at least one empty space
        if (emptySpaceExists(b)) {
            return true;
        }
        //two adjacent tile with the same value
        int size = b.size();
        // value(col, row)==value(col, row + 1)
        for (int col = 0; col < size; col += 1) {
            for (int row = 0; row < size - 1; row += 1) {
                Tile t1 = b.tile(col, row);
                Tile t2 = b.tile(col, row + 1);
                if (t1.value() == t2.value()) {
                    return true;
                }
            }
        }
        // value(col, row)==value(col + 1, row)
        for (int row = 0; row < size; row += 1) {
            for (int col = 0; col < size - 1; col += 1) {
                Tile t1 = b.tile(col, row);
                Tile t2 = b.tile(col + 1, row);
                if (t1.value() == t2.value()) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
