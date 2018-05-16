package com.chess.core.listeners;

import com.chess.core.Command;
import com.chess.core.model.Player;

public interface OnCheckKingListener {
    void onCheckKing(Command command, Player enemy);
}
