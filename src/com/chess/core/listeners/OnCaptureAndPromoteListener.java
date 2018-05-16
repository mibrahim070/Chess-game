package com.chess.core.listeners;

import com.chess.core.Command;
import com.chess.core.model.Player;

public interface OnCaptureAndPromoteListener {
    void onCaptureAndPromote(Command command, Player enemy);
}
