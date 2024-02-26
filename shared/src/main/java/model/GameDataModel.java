package model;
import chess.ChessGame;

public record GameDataModel(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
