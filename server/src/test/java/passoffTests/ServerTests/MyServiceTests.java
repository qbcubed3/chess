package passoffTests.ServerTests;
import chess.ChessGame;
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
        int length = MemoryUserDAO.length();
        UserDataModel user = new UserDataModel("punt", "tired", "gmail@gmail.com");
        UserService.registerUser(user);

        assertTrue(MemoryUserDAO.length() > length);
    }

    @Test
    @Order(3)
    public void testBadRegister(){
        int length = MemoryUserDAO.length();
        UserDataModel user = new UserDataModel("", "thing", "gamei");
        try{
            UserService.registerUser(user);
        }
        catch (Exception e){
            assertEquals(length, MemoryUserDAO.length());
        }
    }
    @Test
    @Order(4)
    public void testLogin(){
        int length = MemoryAuthDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "gamei");
        try{
            MemoryUserDAO.registerUser(user);
            UserService.loginUser("username", "thing");
        }
        catch (Exception e){
            fail();
        }
        assertTrue(length < MemoryAuthDAO.length());
    }
    @Test
    @Order(5)
    public void testBadLogin(){
        int length = MemoryUserDAO.length();
        try{
            UserService.loginUser(null, "thing");
        }
        catch (Exception e){
            assertEquals(length, MemoryUserDAO.length());
            return;
        }
        fail();
    }
    @Test
    @Order(6)
    public void testClear(){
        int length = MemoryUserDAO.length() + MemoryAuthDAO.length() + MemoryGameDAO.length();
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

        assertTrue((MemoryUserDAO.length() + MemoryAuthDAO.length() + MemoryGameDAO.length()) <= length);
    }
    @Test
    @Order(7)
    public void testLogout(){
        int length = MemoryAuthDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            UserService.logoutUser(auth);
        }
        catch(Exception e){
            fail();
        }
        assertEquals(length, MemoryAuthDAO.length());
    }
    @Test
    @Order(8)
    public void testBadLogout(){
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
        Assertions.assertEquals(games.getFirst().gameID(), testGames.getFirst().gameID());
        Assertions.assertEquals(games.getFirst().gameName(), testGames.getFirst().gameName());
        Assertions.assertNull(games.getFirst().whiteUsername());
        Assertions.assertNull(games.getFirst().blackUsername());
    }

    @Test
    @Order(10)
    public void testBadList(){
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
        int length = MemoryGameDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, "game");
        }
        catch(Exception e){
            fail();
        }
        Assertions.assertTrue(length < MemoryGameDAO.length());
    }
    @Test
    @Order(12)
    public void testBadCreate(){
        int length = MemoryGameDAO.length();
        UserDataModel user = new UserDataModel("username", "thing", "email");
        try{
            String auth = UserService.registerUser(user);
            GameService.createGame(auth, null);
        }
        catch (Exception e){
            Assertions.assertTrue(MemoryGameDAO.length() == length);
        }
    }
}
