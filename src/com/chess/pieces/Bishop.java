package com.chess.pieces;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;
import com.chess.pieces.check_move.CheckMoveDiagonal;

import java.util.Map;

public class Bishop extends Piece implements CheckMoveDiagonal{
    
	@Override
    public MoveResult isValidMove(Spot destination, Map<String, String> pieces , Piece lastPieceMoved) {
        Spot source = this.getSpot();

        MoveResult action = MoveResult.INVALID;

        // move up right
        if (destination.getColumn() > source.getColumn() && destination.getRow() > source.getRow()) {
            action = checkDiagonal(source, destination, 1, 1, pieces);
        } else if (destination.getColumn() > source.getColumn() && destination.getRow() < source.getRow()) {
            // move down right
            action = checkDiagonal(source, destination, 1, -1, pieces);

        } else if (destination.getColumn() < source.getColumn() && destination.getRow() < source.getRow()) {
            // move down left
            action = checkDiagonal(source, destination, -1, -1, pieces);

        } else if (destination.getColumn() < source.getColumn() && destination.getRow() > source.getRow()) {
            // move up left
            action = checkDiagonal(source, destination, -1, 1, pieces);

        }
        return action;
    }

}
