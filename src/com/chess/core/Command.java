package com.chess.core;

import com.chess.core.model.Move;
import com.chess.core.model.Player;

public class Command {
    Move move;
    private Player player;

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
