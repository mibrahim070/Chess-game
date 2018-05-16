package com.chess.core.listeners;

import com.chess.core.Command;
import com.chess.core.model.Player;

public interface OnCaptureListener {
    void onCapture(Command command, Player enemy);
}
