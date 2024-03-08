package passoffTests.ServerTests;
import chess.ChessGame;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.GameDataModel;
import model.UserDataModel;
import org.junit.jupiter.api.*;
import server.Server;
import dataAccess.*;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyServiceTests {
    @Test
    @Order(1)
    public void testServer(){
        Server server = new Server();
        System.out.println(server.run(5231));
    }

    @Test
    @Order(2)
    public void testRegister()throws UsernameTakenException, NullParameterException{
        ClearService.clearDatabase();
        int length = SQLUserDAO.length();
        UserDataModel user = new UserDataModel("punt", "tired", "gmail@gmail.com");
        UserService.registerUser(user);

        assertTrue(SQLUserDAO.length() > length);
    }

    @Test
    @Order(3)
    public void testBadRegister(){
        ClearService.clearDatabase();
        int length = SQLUserDAO.length();
        UserDataModel user = new UserDataModel("", "thing", "gamei");
        try{
            UserService.registerUser(user);
        }
        catch (Exception e){
            assertEquals(length, SQLUserDAO.length());
        }
    }
    @Test
    @Order(4)
    public void testLogin(){
        ClearService.clearDatabase();
        int length = SQLAuthDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "gamei");
        try{
            SQLUserDAO.registerUser(user);
            UserService.loginUser("username", "thing");
        }
        catch (Exception e){
            fail();
        }
        assertTrue(length < SQLAuthDAO.length());
    }
    @Test
    @Order(5)
    public void testBadLogin(){
        ClearService.clearDatabase();
        int length = SQLUserDAO.length();
        try{
            UserService.loginUser(null, "thing");
        }
        catch (Exception e){
            assertEquals(length, SQLUserDAO.length());
            return;
        }
        fail();
    }
    @Test
    @Order(6)
    public void testClear(){
        int length = SQLUserDAO.length() + SQLAuthDAO.length() + SQLGameDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "gamei");
        try {
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, "game");
        }
        catch (Exception e){
            fail();
            return;
        }
        ClearService.clearDatabase();

        assertTrue((SQLUserDAO.length() + SQLAuthDAO.length() + SQLGameDAO.length()) <= length);
    }
    @Test
    @Order(7)
    public void testLogout(){
        ClearService.clearDatabase();
        int length = SQLAuthDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            UserService.logoutUser(auth);
        }
        catch(Exception e){
            fail();
        }
        assertEquals(length, SQLAuthDAO.length());
    }
    @Test
    @Order(8)
    public void testBadLogout(){
        ClearService.clearDatabase();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = "authToken";
            UserService.logoutUser(auth);
        }
        catch (Exception e){
            assertEquals(2, 2);
        }
    }
    @Test
    @Order(9)
    public void testList(){
        ClearService.clearDatabase();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        ArrayList<GameDataModel> games = null;
        try{
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, "game1");
            games = GameService.listGames(auth);
        }
        catch (Exception e){
            fail();
        }
        GameDataModel game = new GameDataModel(1, null, null, "game1", new ChessGame());
        ArrayList<GameDataModel> testGames = new ArrayList<GameDataModel>();
        testGames.add(game);
        Assertions.assertEquals(games.getFirst().gameName(), testGames.getFirst().gameName());
        Assertions.assertNull(games.getFirst().whiteUsername());
        Assertions.assertNull(games.getFirst().blackUsername());
    }

    @Test
    @Order(10)
    public void testBadList(){
        ClearService.clearDatabase();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            UserService.registerUser(user);
            String auth = "authToken";
            GameService.listGames(auth);
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
            return;
        }
        fail();
    }
    @Test
    @Order(11)
    public void testCreate() {
        ClearService.clearDatabase();
        int length = SQLGameDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, "game");
        }
        catch(Exception e){
            fail();
        }
        Assertions.assertTrue(length < SQLGameDAO.length());
    }
    @Test
    @Order(12)
    public void testBadCreate(){
        ClearService.clearDatabase();
        int length = SQLGameDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, null);
        }
        catch (Exception e){
            Assertions.assertTrue(SQLGameDAO.length() == length);
        }
    }
    @Test
    @Order(13)
    public void testJoin(){
        ClearService.clearDatabase();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        UserDataModel user2 = new UserDataModel("username4", "thing", "ema4il");
        ArrayList<GameDataModel> games = null;
        try{
            String auth = UserService.registerUser(user);
            String auth2 = UserService.registerUser(user2);
            int gameId = GameService.createGame(auth, "game");
            GameService.joinGame(auth, gameId, "BLACK");
            GameService.joinGame(auth2, gameId, "WHITE");
            games = GameService.listGames(auth);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
        System.out.println(games);
        Assertions.assertEquals(games.getFirst().whiteUsername(), "username4");
        Assertions.assertEquals(games.getFirst().blackUsername(), "username");
    }
    @Test
    @Order(14)
    public void testBadJoin(){
        ClearService.clearDatabase();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            UserService.registerUser(user);
            String auth = "auth";
            GameService.joinGame(auth, 1, "");
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
            return;
        }
        fail();
    }

}
