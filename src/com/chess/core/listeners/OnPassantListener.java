package com.chess.core.listeners;

import com.chess.core.Command;
import com.chess.core.model.Player;

public interface OnPassantListener {
    void onPawnPassant(Command command, Player enemy);
}
