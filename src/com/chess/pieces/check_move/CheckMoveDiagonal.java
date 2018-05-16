package com.chess.pieces.check_move;

import java.util.Map;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public interface CheckMoveDiagonal {
	default MoveResult checkDiagonal(Spot source, Spot destination, int dim1, int dim2, Map<String, String> pieces) {
		for (int i = 1; i <= 7; i++) {
			String key = (char) (source.getColumn() + (i * dim1)) + String.valueOf(source.getRow() + (i * dim2));
			if (pieces.get(key) == null && key.charAt(0) == destination.getColumn()
					&& source.getRow() + (i * dim2) == destination.getRow()) {
				return MoveResult.EMPTYMOVE;
			} else if (pieces.get(key) == null) {
				continue;
			} else if (pieces.get(key) != null && key.charAt(0) == destination.getColumn()
					&& source.getRow() + (i * dim2) == destination.getRow() && destination.getPiece() != null
					&& !(destination.getPiece().getColor().equals(source.getPiece().getColor()))) {
				return MoveResult.CAPTURE;
			} else {
				return MoveResult.INVALID;
			}

		}
		return MoveResult.INVALID;
	}
}
