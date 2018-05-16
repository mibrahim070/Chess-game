package com.chess.core;

import com.chess.core.listeners.*;
import com.chess.core.model.Color;
import com.chess.core.model.Move;
import com.chess.core.model.Player;
import com.chess.core.model.Spot;
import com.chess.exceptions.NotValidMoveException;
import com.chess.pieces.Piece;
import com.chess.ui.Window;

public class Core implements OnCaptureListener, OnPromoteListener, OnCastleListener, OnEmptyMoveListener,
		OnCheckKingListener, OnCaptureAndPromoteListener, OnCheckKingMateListener, OnDrawListener, OnPassantListener {
	private Window window;
	private Player player1;
	private Player player2;
	private CommandExecutor commandExecutor;
	private Boolean player1Turn = true;

	public Core(Window window) {
		this.window = window;
		commandExecutor = new CommandExecutor();
		commandExecutor.setOnCaptureAndPromoteListener(this);
		commandExecutor.setOnPassantListener(this);
		commandExecutor.setOnCaptureListener(this);
		commandExecutor.setOnPromoteListener(this);
		commandExecutor.setOnEmptyMoveListener(this);
		commandExecutor.setOnCastleListener(this);
		commandExecutor.setOnCheckKingListener(this);
		commandExecutor.setOnCheckKingMateListener(this);
		commandExecutor.setOnDrawListener(this);
	}

	public void toggleTurn() {
		player1Turn = !player1Turn;
	}

	public void initPlayer1() {
		player1 = new Player();
		player1.setColor(Color.WHITE);
		player1.initPiecesSpots();
	}

	public void initPlayer2() {
		player2 = new Player();
		player2.setColor(Color.BLACK);
		player2.initPiecesSpots();
	}

	public void performMove(String origin, String target) throws NotValidMoveException, CloneNotSupportedException {

		Spot source = new Spot();
		Spot destination = new Spot();

		if (source.isValid(origin) && destination.isValid(target)) {
			destination.setPiece(player1.getPiecesSpots().get(destination));
			if (destination.getPiece() == null)
				destination.setPiece(player2.getPiecesSpots().get(destination));

			if (player1Turn) {
				Piece piece = player1.getPiecesSpots().get(source);
				source.setPiece(piece);
				if (piece != null) {
					Command command = new Command();
					command.setPlayer(player1);
					command.setMove(new Move(source, destination));
					commandExecutor.execute(command, window.getBoard(), player2);
					toggleTurn();
				} else {
					throw new NotValidMoveException("Invalid Move");
				}
			} else {
				Piece piece = player2.getPiecesSpots().get(source);
				source.setPiece(piece);
				if (piece != null) {
					Command command = new Command();
					command.setPlayer(player2);
					command.setMove(new Move(source, destination));
					commandExecutor.execute(command, window.getBoard(), player1);
					toggleTurn();
				} else {
					throw new NotValidMoveException("Invalid Move");
				}
			}
		}

	}

	@Override
	public void onCastle(Command command) {
		System.out.println("onCastle");
		Color color = (player1Turn) ? Color.BLACK : Color.WHITE;
		castleInUI(command, color.name() + " Turn Now");
		command.getPlayer().castle(command.getMove());
	}

	private void castleInUI(Command command, String message) {
		String origin = command.getMove().getSourceSpot().getConcatinated();
		String target = command.getMove().getDestinationSpot().getConcatinated();
		String king = window.getBoard().pieces.get(origin);
		String rook = window.getBoard().pieces.get(target);
		window.getBoard().pieces.remove(target);
		window.getBoard().pieces.remove(origin);
		if (origin.charAt(0) < target.charAt(0)) {
			window.getBoard().pieces.put(((char) (origin.charAt(0) + 1)) + "" + origin.charAt(1), rook);
			window.getBoard().pieces.put(((char) (origin.charAt(0) + 2)) + "" + origin.charAt(1), king);
		} else if (origin.charAt(0) > target.charAt(0)) {
			window.getBoard().pieces.put(((char) (origin.charAt(0) - 1)) + "" + origin.charAt(1), rook);
			window.getBoard().pieces.put(((char) (origin.charAt(0) - 2)) + "" + origin.charAt(1), king);
		}
		window.setLabetText(message);
		window.getBoard().draw();
	}

	@Override
	public void onCapture(Command command, Player enemy) {
		System.out.println("onCapture");
		makeMoveInUI(command, enemy.getColor().name() + " Turn Now");
		enemy.getPiecesSpots().remove(command.getMove().getDestinationSpot());
	}

	@Override
	public void onPromote(Command command) {
		System.out.println("onPromote");
		String target = command.getMove().getDestinationSpot().getConcatinated();
		Color color = (player1Turn) ? Color.BLACK : Color.WHITE;
		makeMoveInUI(command, color.name() + " Turn Now");
		window.getBoard().pieces.put(target, command.getPlayer().getColor().name().toLowerCase().charAt(0) + "Q");
		command.getPlayer().promotePawn(command.getMove().getDestinationSpot());
		window.getBoard().draw();
	}

	@Override
	public void onEmptyMove(Command command) {
		System.out.println("onEmptyMove");
		Color color = (player1Turn) ? Color.BLACK : Color.WHITE;
		makeMoveInUI(command, color.name() + " Turn Now");
	}

	@Override
	public void onCheckKing(Command command, Player enemy) {
		System.out.println("onKingCheck");
		window.setLabetText(enemy.getColor().name() + " King Checked");
	}

	private void makeMoveInUI(Command command, String message) {
		String origin = command.getMove().getSourceSpot().getConcatinated();
		String target = command.getMove().getDestinationSpot().getConcatinated();

		String piece = window.getBoard().pieces.get(origin);
		window.getBoard().pieces.put(target, piece);
		window.getBoard().pieces.remove(origin);
		window.setLabetText(message);
		window.getBoard().draw();

	}

	@Override
	public void onCaptureAndPromote(Command command, Player enemy) {
		System.out.println("onCaptureAndPromote");
		makeMoveInUI(command, enemy.getColor().name() + " Turn Now");
		enemy.getPiecesSpots().remove(command.getMove().getDestinationSpot());
		String target = command.getMove().getDestinationSpot().getConcatinated();
		command.getPlayer().promotePawn(command.getMove().getDestinationSpot());
		window.getBoard().pieces.put(target, command.getPlayer().getColor().name().toLowerCase().charAt(0) + "Q");
		window.getBoard().draw();
	}

	@Override
	public void onDraw() {
		System.out.println("onDraw");
		window.setLabetText("DRAW");
		window.submitButton.setEnabled(false);
		window.getBoard().draw();
	}

	@Override
	public void onCheckKingMate(Player winner) {
		System.out.println("onCheckKingMate");
		window.setLabetText(winner.getColor() + " WINS");
		window.submitButton.setEnabled(false);
		window.getBoard().draw();
	}

	@Override
	public void onPawnPassant(Command command, Player enemy) {
		System.out.println("onPawnPassant");
		Spot removedPawnSpot = new Spot(null, command.getMove().getDestinationSpot().getColumn(),
				command.getMove().getSourceSpot().getRow());
		enemy.getPiecesSpots().remove(removedPawnSpot);

		String sourceKey = command.getMove().getSourceSpot().getConcatinated();
		String destinationKey = command.getMove().getDestinationSpot().getConcatinated();
		String removedPawn = destinationKey.charAt(0) + "" + sourceKey.charAt(1);

		window.getBoard().pieces.remove(removedPawn);
		window.getBoard().pieces.put(destinationKey, window.getBoard().pieces.get(sourceKey));
		window.getBoard().pieces.remove(sourceKey);
		Color color = (player1Turn) ? Color.BLACK : Color.WHITE;
		window.setLabetText(color.name() + " Turn Now");
		window.getBoard().draw();
	}
}
