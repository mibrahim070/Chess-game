package com.chess.pieces;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;
import com.chess.pieces.check_move.CheckMoveHorizontalRightAndLeft;
import com.chess.pieces.check_move.CheckMoveVerticalUpAndDown;

import java.util.Map;

public class Rook extends Piece implements CheckMoveHorizontalRightAndLeft, CheckMoveVerticalUpAndDown {

	@Override
	public MoveResult isValidMove(Spot destination, Map<String, String> pieces, Piece lastPieceMoved) {
		Spot source = this.getSpot();

		MoveResult action = MoveResult.INVALID;
		// move horizontal
		if (source.getRow() == destination.getRow()) {
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
