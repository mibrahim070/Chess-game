package com.chess.pieces.check_move;

import java.util.Map;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public interface CheckMoveHorizontalRightAndLeft {
	default MoveResult checkHorizontalRightAndLeft(Spot source, Spot destination, Map<String, String> pieces, int dim) {
		for (int i = 1; i <= 7; i++) {
			String key = (char) (source.getColumn() + (i * dim)) + String.valueOf(source.getRow());
			if (pieces.get(key) == null && key.charAt(0) == destination.getColumn()) {
				return MoveResult.EMPTYMOVE;
			} else if (pieces.get(key) == null) {
				continue;
			} else if (pieces.get(key) != null && key.charAt(0) == destination.getColumn()
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
