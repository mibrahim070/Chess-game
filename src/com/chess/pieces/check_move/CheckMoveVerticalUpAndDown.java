package com.chess.pieces.check_move;

import java.util.Map;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public interface CheckMoveVerticalUpAndDown {
	default MoveResult checkVerticalUpAndDown(Spot source, Spot destination, Map<String, String> pieces, int dim) {
		for (int i = 1; i <= 7; i++) {
			String key = source.getColumn() + String.valueOf(source.getRow() + (i * dim));

			if (pieces.get(key) == null && source.getRow() + (i * dim) == destination.getRow()) {
				return MoveResult.EMPTYMOVE;
			} else if (pieces.get(key) == null) {
				continue;
			} else if (pieces.get(key) != null && source.getRow() + (i * dim) == destination.getRow()
					&& destination.getPiece() != null
					&& !(destination.getPiece().getColor().equals(source.getPiece().getColor()))) {
				return MoveResult.CAPTURE;
			} else {
				return MoveResult.INVALID;
			}

		}
		return MoveResult.INVALID;
	}
}
