import chess.*;
import server.ServerFacade;
import ui.ChessTerminalUI;

public class Main {
    public static void main(String[] args) {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ChessTerminalUI ui = new ChessTerminalUI(facade);
        ui.runUI();
    }
}