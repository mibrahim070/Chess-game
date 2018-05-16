package com.chess.pieces;

import java.util.Map;

import com.chess.core.model.Color;
import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public class Pawn extends Piece {

	public MoveResult checkMovePawn(Spot source, Spot destination, int step, int promote, Map<String, String> pieces,
			Piece lastPieceMoved) {

		String key = source.getColumn().toString() + (source.getRow() + step);
		// one step forward or two step forward in start with empty destination
		if (destination.getColumn() == source.getColumn() && (destination.getRow() - source.getRow() == step
				|| (destination.getRow() - source.getRow() == (2 * step) && source.getPiece().getNumberOfMoves() == 0
						&& pieces.get(key) == null))) {
			if (destination.getPiece() == null) {
				if (destination.getRow() == promote)
					return MoveResult.PROMOTE;

				return MoveResult.EMPTYMOVE;
			}

		} else if (destination.getRow() - source.getRow() == step
				&& Math.abs(destination.getColumn() - source.getColumn()) == 1) {
			// diagonal 
			if (destination.getPiece() != null) {
				if (!destination.getPiece().getColor().equals(source.getPiece().getColor())) {
					if (destination.getRow() == promote)
						return MoveResult.CAPTURE_AND_PROMOTE;
					else
						return MoveResult.CAPTURE;
				}
			} else {
				key = destination.getColumn() + String.valueOf(source.getRow());
				if (pieces.get(key) != null) {
					String piece = pieces.get(key);
					if (piece.charAt(1) == 'P' && lastPieceMoved != null) {
						if (lastPieceMoved.numberOfMoves == 1
								&& destination.getColumn() == lastPieceMoved.getSpot().getColumn()
								&& source.getRow() == lastPieceMoved.getSpot().getRow()) {

							if ((piece.charAt(0) == 'b' && step == 1 && source.getRow() == 5)
									|| (piece.charAt(0) == 'w' && step == -1 && source.getRow() == 4)){
								
								return MoveResult.PASSANT;
							}
						}
					}
				}
			}

		}
		return MoveResult.INVALID;
	}

	@Override
	public MoveResult isValidMove(Spot destination, Map<String, String> pieces, Piece lastPieceMoved) {
		Spot source = this.getSpot();

		MoveResult action = MoveResult.INVALID;

		// white player
		if (source.getPiece().getColor().equals(Color.WHITE)) {
			action = checkMovePawn(source, destination, 1, 8, pieces, lastPieceMoved);
		} else {
			action = checkMovePawn(source, destination, -1, 1, pieces, lastPieceMoved);
		}

		return action;
	}

}
