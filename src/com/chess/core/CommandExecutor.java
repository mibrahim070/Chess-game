package com.chess.core;

import com.chess.core.listeners.*;
import com.chess.core.model.Move;
import com.chess.core.model.MoveResult;
import com.chess.core.model.Player;
import com.chess.core.model.Spot;
import com.chess.exceptions.NotValidMoveException;
import com.chess.pieces.King;
import com.chess.pieces.Piece;
import com.chess.ui.Board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandExecutor {

	private OnDrawListener onDrawListener;
	private OnPassantListener onPassantListener;
	private OnCheckKingMateListener onCheckKingMateListener;
	private OnCaptureListener onCaptureListener;
	private OnCaptureAndPromoteListener onCaptureAndPromoteListener;
	private OnCheckKingListener onCheckKingListener;
	private OnPromoteListener onPromoteListener;
	private OnEmptyMoveListener onEmptyMoveListener;
	private OnCastleListener onCastleListener;

	public static HashMap<String, String> makeTempMove(Move move, HashMap<String, String> pieces) {
		Spot source = move.getSourceSpot();
		Spot destination = move.getDestinationSpot();
		String sourceKey = source.getColumn() + Integer.toString(source.getRow());
		String destinationKey = destination.getColumn() + Integer.toString(destination.getRow());
		String valueOfSource = pieces.get(sourceKey);
		String valueOfDestination = pieces.get(destinationKey);
		pieces.remove(sourceKey);

		if (move.getMoveResult().equals(MoveResult.CAPTURE)) {
			pieces.put(destinationKey, valueOfSource);
		} else if (move.getMoveResult().equals(MoveResult.CASTLE)) {
			pieces.remove(destinationKey);
			if (sourceKey.charAt(0) < destinationKey.charAt(0)) {
				pieces.put(((char) (sourceKey.charAt(0) + 1)) + "" + sourceKey.charAt(1), valueOfDestination);
				pieces.put(((char) (sourceKey.charAt(0) + 2)) + "" + sourceKey.charAt(1), valueOfSource);
			} else if (sourceKey.charAt(0) > destinationKey.charAt(0)) {
				pieces.put(((char) (sourceKey.charAt(0) - 1)) + "" + sourceKey.charAt(1), valueOfDestination);
				pieces.put(((char) (sourceKey.charAt(0) - 2)) + "" + sourceKey.charAt(1), valueOfSource);
			}
		}

		else if (move.getMoveResult().equals(MoveResult.EMPTYMOVE)) {
			pieces.put(destinationKey, valueOfSource);
		}

		else if (move.getMoveResult().equals(MoveResult.PROMOTE)) {
			pieces.put(destinationKey, valueOfSource.charAt(0) + "Q");
		}

		else if (move.getMoveResult().equals(MoveResult.CAPTURE_AND_PROMOTE)) {
			pieces.put(destinationKey, valueOfSource.charAt(0) + "Q");
		} else if (move.getMoveResult().equals(MoveResult.PASSANT)) {
			String removedPawn = destinationKey.charAt(0) + "" + sourceKey.charAt(1);
			pieces.remove(removedPawn);
			pieces.put(destinationKey, valueOfSource);
		}

		return pieces;
	}

	public void setOnPassantListener(OnPassantListener onPassantListener) {
		this.onPassantListener = onPassantListener;
	}

	public void setOnDrawListener(OnDrawListener onDrawListener) {
		this.onDrawListener = onDrawListener;
	}

	public void setOnCheckKingMateListener(OnCheckKingMateListener onCheckKingMateListener) {
		this.onCheckKingMateListener = onCheckKingMateListener;
	}

	public void setOnCaptureListener(OnCaptureListener onCaptureListener) {
		this.onCaptureListener = onCaptureListener;
	}

	public void setOnCaptureAndPromoteListener(OnCaptureAndPromoteListener onCaptureAndPromoteListener) {
		this.onCaptureAndPromoteListener = onCaptureAndPromoteListener;
	}

	public void setOnCheckKingListener(OnCheckKingListener onCheckKingListener) {
		this.onCheckKingListener = onCheckKingListener;
	}

	public void setOnPromoteListener(OnPromoteListener onPromoteListener) {
		this.onPromoteListener = onPromoteListener;
	}

	public void setOnEmptyMoveListener(OnEmptyMoveListener onEmptyMoveListener) {
		this.onEmptyMoveListener = onEmptyMoveListener;
	}

	public void setOnCastleListener(OnCastleListener onCastleListener) {
		this.onCastleListener = onCastleListener;
	}

	public void execute(Command command, Board board, Player enemy)
			throws NotValidMoveException, CloneNotSupportedException {

		Piece piece = command.getPlayer().getPiecesSpots().get(command.getMove().getSourceSpot());
		HashMap<String, String> tempPieces = (HashMap<String, String>) board.pieces.clone();
		MoveResult moveResult = piece.isValidMove(command.getMove().getDestinationSpot(), tempPieces,
				enemy.getLastPieceMoved());

		//previous king status before make the move , using in castle rule 
		boolean previousKingStatus = false;
		
		if (!moveResult.equals(MoveResult.INVALID)) {
			command.getMove().setMoveResult(moveResult);
			King king = (King) command.getPlayer().getKing().clone();
			
			if (king.isOnCheck(tempPieces)) {
				previousKingStatus = true;
			}
			
			tempPieces = makeTempMove(command.getMove(), tempPieces);

			if (piece instanceof King) {
				king.setSpot(command.getMove().getDestinationSpot());
			} else {
				king = command.getPlayer().getKing();
			}
			if (king.isOnCheck(tempPieces)) {
				throw new NotValidMoveException("King is on check");
			} else {
				if (moveResult.equals(MoveResult.CAPTURE)) {
					command.getPlayer().movePiece(command.getMove());
					onCaptureListener.onCapture(command, enemy);
				} else if (moveResult.equals(MoveResult.PROMOTE)) {
					command.getPlayer().movePiece(command.getMove());
					onPromoteListener.onPromote(command);
				} else if (moveResult.equals(MoveResult.CASTLE) && previousKingStatus == false ) {
					onCastleListener.onCastle(command);
				} else if (moveResult.equals(MoveResult.EMPTYMOVE)) {
					command.getPlayer().movePiece(command.getMove());
					onEmptyMoveListener.onEmptyMove(command);
				} else if (moveResult.equals(MoveResult.CAPTURE_AND_PROMOTE)) {
					command.getPlayer().movePiece(command.getMove());
					onCaptureAndPromoteListener.onCaptureAndPromote(command, enemy);
				} else if (moveResult.equals(MoveResult.PASSANT)) {
					command.getPlayer().movePiece(command.getMove());
					onPassantListener.onPawnPassant(command, enemy);
				}
				logData(command, enemy, tempPieces);

				CheckIfGameOver(command, tempPieces, enemy);
			}
		} else {
			throw new NotValidMoveException("Invalid move");
		}
	}

	private void CheckIfGameOver(Command command, HashMap<String, String> tempPieces, Player enemy)
			throws CloneNotSupportedException {
		King enemyKing = enemy.getKing();
		boolean isEnemyKingOnCheck = enemyKing.isOnCheck(tempPieces);
		List<Move> allPossibleMoves = enemy.getAllPossibleMoves(tempPieces, enemy);
		if (isEnemyKingOnCheck) {
			if (enemy.canEscape(allPossibleMoves, tempPieces))
				onCheckKingListener.onCheckKing(command, enemy);
			else
				onCheckKingMateListener.onCheckKingMate(command.getPlayer());
		} else {
			if (allPossibleMoves.size() == 0) {
				onDrawListener.onDraw();
			}
		}
	}

	private void logData(Command command, Player enemy, HashMap<String, String> tempPieces) {
		System.out.println(command.getPlayer().getColor().name() + " player spots");
		for (Piece piece1 : command.getPlayer().getPiecesSpots().values()) {
			System.out.println(
					piece1.getColor() + "  " + piece1.getSpot().getConcatinated() + " " + piece1.getClass().getName());
		}
		System.out.println("---------------------------------------------------------");
		System.out.println(enemy.getColor().name() + " player spots");
		for (Piece piece1 : enemy.getPiecesSpots().values()) {
			System.out.println(
					piece1.getColor() + "  " + piece1.getSpot().getConcatinated() + " " + piece1.getClass().getName());
		}
		System.out.println("---------------------------------------------------------");
		for (Map.Entry<String, String> entry : tempPieces.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

	}
}
