package ui.websocket;

import chess.ChessGame;

public interface NotificationHandler {
    void notif(String message);
    void error(String error);
    void draw(ChessGame game);
}
