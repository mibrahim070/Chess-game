package com.chess.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.core.model.Color;
import com.chess.core.model.Move;
import com.chess.core.model.MoveResult;
import com.chess.core.model.Spot;

public abstract class Piece{
    protected Color color;
    protected Spot spot;
    protected Integer numberOfMoves = 0;

    public abstract MoveResult isValidMove(Spot destination, Map<String, String> pieces , Piece lastPieceMoved);

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Integer getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(Integer numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public List<Move> getAllPossibleMoves(HashMap<String, String> tempPieces, HashMap<Spot, Piece> myMap, HashMap<Spot, Piece> enemyMap) {
        List<Move> moves = new ArrayList<>();
        for (int col = 97; col < 105; col++) {
            for (int row = 1; row < 9; row++) {
                Spot destinationSpot = new Spot(null, (char) col, row);
                Piece destinationPiece = myMap.get(destinationSpot);
                if (destinationPiece == null)
                    destinationPiece = enemyMap.get(destinationSpot);
                if (destinationPiece != null)
                    destinationSpot = destinationPiece.getSpot();
                MoveResult moveResult = isValidMove(destinationSpot, tempPieces , null);
                if (!moveResult.equals(MoveResult.INVALID)) {
                    moves.add(new Move(spot, destinationSpot, moveResult));
                }
            }
        }
        return moves;
    }

}
