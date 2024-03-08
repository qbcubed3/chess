package passoffTests.dataAccessTests;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import dataAccess.SQLGameDAO;
import model.UserDataModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;

import static org.junit.jupiter.api.Assertions.fail;

public class SQLDataAccessTests {
    @Test
    public void testUserRegister(){
        try{
            SQLUserDAO.clearUsers();
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            UserDataModel check = SQLUserDAO.getUser("punitiveMedal");
            Assertions.assertNotNull(check);
            Assertions.assertEquals(check.username(), user.username());
            Assertions.assertEquals(check.email(), user.email());
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadUserRegister(){
        try {
            SQLUserDAO.clearUsers();
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            var user2 = new UserDataModel("punitiveMedal", "passfkey", "email@email.com");
            SQLUserDAO.registerUser(user2);
            fail();
        }
        catch(Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testGetAuth() {
        try {
            SQLUserDAO.clearUsers();
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            String username = SQLUserDAO.getAuth("punitiveMedal", "passkey");
            Assertions.assertEquals(username, "punitiveMedal");
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadGetAuth() {
        try {
            SQLUserDAO.clearUsers();
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            String username = SQLUserDAO.getAuth("punitiveMedal", "parsskey");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }

    @Test
    public void testUserClear(){
        SQLUserDAO.clearUsers();
        try{
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            SQLUserDAO.clearUsers();
            Assertions.assertEquals(SQLUserDAO.length(), 0);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testLength(){
        ClearService.clearDatabase();
        try{
            var user = new UserDataModel("punitiveMedal", "passkey", "email@email.com");
            SQLUserDAO.registerUser(user);
            Assertions.assertEquals(SQLUserDAO.length(), 1);
        }
        catch (Exception e){
            fail();
        }
    }

    /**
     * The next tests are for the SQLAuthDAO
     */
    @Test
    public void testClearAuths(){
        try{
            SQLAuthDAO.addAuth("username");
            SQLAuthDAO.clearAuths();
            Assertions.assertEquals(SQLAuthDAO.length(), 0);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testCheckAuth(){
        ClearService.clearDatabase();
        try{
            String auth = SQLAuthDAO.addAuth("username");
            boolean check = SQLAuthDAO.checkAuth(auth);
            Assertions.assertTrue(check);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadCheckAuth(){
        ClearService.clearDatabase();
        try{
            SQLAuthDAO.addAuth("username");
            boolean check = SQLAuthDAO.checkAuth("thing");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testRemoveUsername(){
        ClearService.clearDatabase();
        try{
            String auth = SQLAuthDAO.addAuth("username");
            SQLAuthDAO.removeUsername(auth);
            Assertions.assertEquals(SQLAuthDAO.length(), 0);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadRemoveUsername(){
        ClearService.clearDatabase();
        try{
            SQLAuthDAO.addAuth("username");
            SQLAuthDAO.removeUsername("bad");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testGetUsername(){
        ClearService.clearDatabase();
        try{
            String auth = SQLAuthDAO.addAuth("username");
            String username = SQLAuthDAO.getUsername(auth);
            Assertions.assertEquals(username, "username");
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadGetUsername(){
        ClearService.clearDatabase();
        try{
            String auth = SQLAuthDAO.addAuth("username");
            SQLAuthDAO.getUsername("badAuth");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }

    /*
    this is where the gameDAO tests are
     */
    @Test
    public void testClearGame(){
        try{
            SQLGameDAO.createGame("game");
            SQLGameDAO.clearGame();
            Assertions.assertEquals(SQLGameDAO.length(), 0);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testGetGames(){
        ClearService.clearDatabase();
        try{
            SQLGameDAO.createGame("game");
            SQLGameDAO.createGame("game2");
            var gameList = SQLGameDAO.getGames();
            Assertions.assertEquals(gameList.size(), 2);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testCreateGame(){
        ClearService.clearDatabase();
        try{
            SQLGameDAO.createGame("game");
            var gameList = SQLGameDAO.getGames();
            Assertions.assertEquals(gameList.getFirst().gameName(), "game");
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadCreateGame(){
        ClearService.clearDatabase();
        try{
            String auth = SQLAuthDAO.addAuth("username");
            GameService.createGame("thing", "game");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2,2 );
        }
    }
    @Test
    public void testJoinGame(){
        ClearService.clearDatabase();
        try{
            int id = SQLGameDAO.createGame("game");
            SQLGameDAO.joinGame(id, "WHITE", "whiteUser");
            SQLGameDAO.joinGame(id, "BLACK", "blackUser");
            SQLGameDAO.joinGame(id, "", "watcher");
            var gameList = SQLGameDAO.getGames();
            Assertions.assertEquals(gameList.getFirst().blackUsername(), "blackUser");
            Assertions.assertEquals(gameList.getFirst().whiteUsername(), "whiteUser");
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadJoinGame(){
        ClearService.clearDatabase();
        try{
            int id = SQLGameDAO.createGame("game");
            SQLGameDAO.joinGame(3002, "WHITE", "whiteUser");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
}
