package com.chess.core.model;

import com.chess.core.CommandExecutor;
import com.chess.pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {
    private Color color;
    private HashMap<Spot, Piece> piecesSpots;
    private Piece lastPieceMoved = null;
    
    public Piece getLastPieceMoved(){
    	return lastPieceMoved;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public HashMap<Spot, Piece> getPiecesSpots() {
        return piecesSpots;
    }

    public void setPiecesSpots(HashMap<Spot, Piece> piecesSpots) {
        this.piecesSpots = piecesSpots;
    }

    private void setInitPiece(Spot spot, Color color, Piece piece) {
        piece.setSpot(spot);
        piece.setColor(color);
        spot.setPiece(piece);
        piece.setSpot(spot);
        this.piecesSpots.put(spot, piece);
    }

    public void initPiecesSpots() {
        piecesSpots = new HashMap<>();
        if (Color.WHITE.equals(color)) {
            setInitPiece(new Spot(null, 'a', 1), Color.WHITE, new Rook());
            setInitPiece(new Spot(null, 'b', 1), Color.WHITE, new Knight());
            setInitPiece(new Spot(null, 'c', 1), Color.WHITE, new Bishop());
            setInitPiece(new Spot(null, 'd', 1), Color.WHITE, new Queen());
            setInitPiece(new Spot(null, 'e', 1), Color.WHITE, new King());
            setInitPiece(new Spot(null, 'f', 1), Color.WHITE, new Bishop());
            setInitPiece(new Spot(null, 'g', 1), Color.WHITE, new Knight());
            setInitPiece(new Spot(null, 'h', 1), Color.WHITE, new Rook());

            setInitPiece(new Spot(null, 'a', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'b', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'c', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'd', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'e', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'f', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'g', 2), Color.WHITE, new Pawn());
            setInitPiece(new Spot(null, 'h', 2), Color.WHITE, new Pawn());

        } else {
            setInitPiece(new Spot(null, 'a', 8), Color.BLACK, new Rook());
            setInitPiece(new Spot(null, 'b', 8), Color.BLACK, new Knight());
            setInitPiece(new Spot(null, 'c', 8), Color.BLACK, new Bishop());
            setInitPiece(new Spot(null, 'd', 8), Color.BLACK, new Queen());
            setInitPiece(new Spot(null, 'e', 8), Color.BLACK, new King());
            setInitPiece(new Spot(null, 'f', 8), Color.BLACK, new Bishop());
            setInitPiece(new Spot(null, 'g', 8), Color.BLACK, new Knight());
            setInitPiece(new Spot(null, 'h', 8), Color.BLACK, new Rook());

            setInitPiece(new Spot(null, 'a', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'b', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'c', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'd', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'e', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'f', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'g', 7), Color.BLACK, new Pawn());
            setInitPiece(new Spot(null, 'h', 7), Color.BLACK, new Pawn());

        }
    }

    public King getKing() {
        for (Piece piece : piecesSpots.values()) {
            if (piece instanceof King)
                return (King) piece;
        }
        return null;
    }

    public void movePiece(Move move) {
        Spot src = move.getSourceSpot(), dest = move.getDestinationSpot();
        Piece piece = piecesSpots.get(src);
        piece.setNumberOfMoves(piece.getNumberOfMoves() + 1);
        dest.setPiece(piece);
        piece.setSpot(dest);
        dest.setPiece(piece);

        lastPieceMoved = piece;
        
        piecesSpots.remove(src);

        piecesSpots.put(dest, piece);
    }

    public void promotePawn(Spot dest) {
        Piece piece = piecesSpots.get(dest);
        if (piece instanceof Pawn) {
            Piece newQueen = new Queen();
            newQueen.setColor(piece.getColor());
            newQueen.setSpot(piece.getSpot());
            piecesSpots.put(dest, newQueen);
        }
    }

    public void castle(Move move) {
        Spot src = move.getSourceSpot(), dest = move.getDestinationSpot();
        Piece srcPiece = piecesSpots.get(src);
        Piece destPiece = piecesSpots.get(dest);
        srcPiece.setNumberOfMoves(srcPiece.getNumberOfMoves() + 1);
        destPiece.setNumberOfMoves(destPiece.getNumberOfMoves() + 1);
        piecesSpots.remove(dest);
        piecesSpots.remove(src);
        if (src.getColumn() > dest.getColumn())
            doCastling(move, destPiece, srcPiece, -1);
        else
            doCastling(move, destPiece, srcPiece, 1);


    }

    private void doCastling(Move move, Piece destPiece, Piece srcPiece, int i) {
        
    	Spot src = move.getSourceSpot(), dest = move.getDestinationSpot();
    	
        dest.setColumn((char) (src.getColumn() + i));
        destPiece.setSpot(dest);
        dest.setPiece(destPiece);
        destPiece.setSpot(dest);

        src.setColumn((char) (dest.getColumn() + i));
        srcPiece.setSpot(src);
        src.setPiece(srcPiece);
        srcPiece.setSpot(src);
        piecesSpots.put(dest, destPiece);
        piecesSpots.put(src, srcPiece);
    }

    public boolean canEscape(List<Move> allPossibleMoves, HashMap<String, String> tempPieces) throws CloneNotSupportedException {
        King myCheckedKing = getKing();
        for (Move move : allPossibleMoves) {
            HashMap<String, String> tempPieces2 = (HashMap<String, String>) tempPieces.clone();
            tempPieces2 = CommandExecutor.makeTempMove(move, tempPieces2);
            King myNewKingSpot = (King) myCheckedKing.clone();
            if (move.getSourceSpot().getPiece() instanceof King)
            	myNewKingSpot.setSpot(move.getDestinationSpot());
            if (!myNewKingSpot.isOnCheck(tempPieces2))
                return true;
        }
        return false;
    }

    public List<Move> getAllPossibleMoves(HashMap<String, String> tempPieces, Player enemy) {
        List<Move> possibleMoves = new ArrayList<>();
        for (Piece piece : piecesSpots.values()) {
            possibleMoves.addAll(piece.getAllPossibleMoves(tempPieces, this.getPiecesSpots(), enemy.getPiecesSpots()));
        }
        return possibleMoves;

    }
}
