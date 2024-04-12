import chess.*;
import server.ServerFacade;
import ui.ChessTerminalUI;

public class Main {
    public static void main(String[] args) {
        try {
            ServerFacade facade = new ServerFacade("http://localhost:8080");
            ChessTerminalUI ui = new ChessTerminalUI("http://localhost:8080");
            ui.runUI();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}