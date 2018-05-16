package com.chess.pieces;

import java.util.HashMap;
import java.util.Map;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public class King extends Piece implements Cloneable {

    private boolean checkBetweenEmpty(Spot source, Spot destination, Map<String, String> pieces) {
        int step = 1, iterator = 0;
        if (source.getColumn() > destination.getColumn()) {
            step = -1;
        }
        iterator += step;
        while ((char) (source.getColumn() + iterator) != destination.getColumn()) {
            String key = ((char) (source.getColumn() + iterator)) + String.valueOf(source.getRow());
            if (pieces.get(key) != null)
                return false;
            iterator += step;
        }
        System.out.println("");
        return true;
    }

    @Override
    public MoveResult isValidMove(Spot destination, Map<String, String> pieces , Piece lastPieceMoved) {
        Spot source = this.getSpot();
        // Castling
        if (destination.getPiece() != null && destination.getPiece().getColor().equals(source.getPiece().getColor())) {
            if (destination.getPiece() instanceof Rook) {
                // if there are not something between them and first move
                if (source.getPiece().numberOfMoves == 0 && destination.getPiece().numberOfMoves == 0
                        && checkBetweenEmpty(source, destination, pieces))
                    return MoveResult.CASTLE;
            }
            return MoveResult.INVALID;
        }
        if ((Math.abs(source.getColumn() - destination.getColumn()) == 1 && source.getRow() - destination.getRow() == 0)
                || (Math.abs(source.getRow() - destination.getRow()) == 1
                && source.getColumn() - destination.getColumn() == 0)
                || (Math.abs(source.getColumn() - destination.getColumn()) == 1
                && Math.abs(source.getRow() - destination.getRow()) == 1)) {
            if (destination.getPiece() == null) {
                return MoveResult.EMPTYMOVE;
            } else {
                return MoveResult.CAPTURE;
            }
        }
        return MoveResult.INVALID;
    }

    private Character getEnemyColorCharacter() {
        if (this.getColor().name().charAt(0) == 'B')
            return 'w';
        else
            return 'b';
    }

    public boolean isOnCheck(HashMap<String, String> pieces) {
        Character enemyColor = getEnemyColorCharacter();
        return isOnKnightAttack(enemyColor, pieces) || isOnDiagonalAttack(enemyColor, pieces) || isOnHorizontalAttack(enemyColor, pieces) || isOnVerticalAttack(enemyColor, pieces);
    }

    private boolean isOnKnightAttack(Character enemyColor, HashMap<String, String> pieces) {
        String knightAttackPositions[] = new String[]{
                (char) (spot.getColumn() - 1) + String.valueOf(spot.getRow() + 2),
                (char) (spot.getColumn() - 1) + String.valueOf(spot.getRow() - 2),
                (char) (spot.getColumn() + 1) + String.valueOf(spot.getRow() + 2),
                (char) (spot.getColumn() + 1) + String.valueOf(spot.getRow() - 2),

                (char) (spot.getColumn() - 2) + String.valueOf(spot.getRow() + 1),
                (char) (spot.getColumn() - 2) + String.valueOf(spot.getRow() - 1),
                (char) (spot.getColumn() + 2) + String.valueOf(spot.getRow() + 1),
                (char) (spot.getColumn() + 2) + String.valueOf(spot.getRow() - 1)};

        for (String key : knightAttackPositions) {
            if (pieces.get(key) != null && pieces.get(key).charAt(0) == enemyColor && pieces.get(key).charAt(1) == 'N')
                return true;
        }

        return false;
    }

    private boolean isOnDiagonalAttack(Character enemyColor, HashMap<String, String> pieces) {
        return (checkDiagonal(1, 1, enemyColor, pieces)
                || checkDiagonal(-1, -1, enemyColor, pieces)
                || checkDiagonal(-1, 1, enemyColor, pieces)
                || checkDiagonal(1, -1, enemyColor, pieces));
    }

    private boolean checkDiagonal(int dim1, int dim2, Character enemyColor, HashMap<String, String> pieces) {
        for (int i = 1; i <= 7; i++) {

        	String key = (char) (spot.getColumn() + (i * dim1)) + String.valueOf(spot.getRow() + (i * dim2));
            if (pieces.get(key) != null) {
                if ((pieces.get(key).charAt(0) == enemyColor) && (pieces.get(key).charAt(1) == 'Q' || pieces.get(key).charAt(1) == 'B' || pieces.get(key).charAt(1) == 'P' || pieces.get(key).charAt(1) == 'K')) {

                    if (pieces.get(key).charAt(1) == 'P') {
                        if (enemyColor == 'w' && i == 1) {
                            if ((dim1 == -1 && dim2 == -1) || (dim1 == 1 && dim2 == -1)) {
                                return true;
                            }
                        }
                        if (enemyColor == 'b' && i == 1) {
                            if ((dim1 == 1 && dim2 == 1) || (dim1 == -1 && dim2 == 1)) {
                                return true;
                            }
                        }
                    } else

                        return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean isOnHorizontalAttack(Character enemyColor, HashMap<String, String> pieces) {
        return (checkHorizontalRightAndLeft(enemyColor, pieces, -1) || checkHorizontalRightAndLeft(enemyColor, pieces, 1));
    }

    private boolean checkHorizontalRightAndLeft(Character enemyColor, HashMap<String, String> pieces, int dim) {
        for (int i = 1; i <= 7; i++) {
            String key = (char) (spot.getColumn() + (i * dim)) + String.valueOf(spot.getRow());
            if (pieces.get(key) != null) {
                if ((pieces.get(key).charAt(0) == enemyColor) && (pieces.get(key).charAt(1) == 'Q' || pieces.get(key).charAt(1) == 'R' || pieces.get(key).charAt(1) == 'K')) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private boolean isOnVerticalAttack(Character enemyColor, HashMap<String, String> pieces) {
        return (checkVerticalUpAndDown(enemyColor, pieces, -1) || checkVerticalUpAndDown(enemyColor, pieces, 1));
    }

    private boolean checkVerticalUpAndDown(Character enemyColor, HashMap<String, String> pieces, int dim) {
        for (int i = 1; i <= 7; i++) {
            String key = spot.getColumn() + String.valueOf(spot.getRow() + (i * dim));
            if (pieces.get(key) != null) {
                if ((pieces.get(key).charAt(0) == enemyColor) && (pieces.get(key).charAt(1) == 'Q' || pieces.get(key).charAt(1) == 'R' || pieces.get(key).charAt(1) == 'K')) {
                    return true;
                }
                break;
            }
        }
        return false;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
