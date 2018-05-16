package com.chess.pieces;

import java.util.Map;

import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public class Knight extends Piece {

    @Override
    public MoveResult isValidMove(Spot destination, Map<String, String> pieces , Piece lastMovePiece) {
        Spot source = this.getSpot();

        
        //knight can jump like L character
        if ((Math.abs(source.getColumn() - destination.getColumn()) == 1
                && Math.abs(source.getRow() - destination.getRow()) == 2)
                || (Math.abs(source.getColumn() - destination.getColumn()) == 2
                && Math.abs(source.getRow() - destination.getRow()) == 1)) {

            if (destination.getPiece() == null) {
                return MoveResult.EMPTYMOVE;
            } else {
                if (!destination.getPiece().getColor().equals(source.getPiece().getColor()))
                    return MoveResult.CAPTURE;
            }

        }
        return MoveResult.INVALID;
    }

}
