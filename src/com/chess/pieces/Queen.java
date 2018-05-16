package com.chess.pieces;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;
import com.chess.pieces.check_move.CheckMoveDiagonal;
import com.chess.pieces.check_move.CheckMoveHorizontalRightAndLeft;
import com.chess.pieces.check_move.CheckMoveVerticalUpAndDown;

import java.util.Map;

public class Queen extends Piece
		implements CheckMoveDiagonal, CheckMoveHorizontalRightAndLeft, CheckMoveVerticalUpAndDown {

	@Override
	public MoveResult isValidMove(Spot destination, Map<String, String> pieces, Piece lastPieceMoved) {
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

		} else if (source.getRow() == destination.getRow()) {
			// move horizontal
			if (source.getColumn() < destination.getColumn())
				action = checkHorizontalRightAndLeft(source, destination, pieces, 1);
			else
				action = checkHorizontalRightAndLeft(source, destination, pieces, -1);
		} else if (source.getColumn() == destination.getColumn()) {
			// move vertical
			if (source.getRow() < destination.getRow())
				action = checkVerticalUpAndDown(source, destination, pieces, 1);
			else
				action = checkVerticalUpAndDown(source, destination, pieces, -1);
		}
		return action;
	}

}
