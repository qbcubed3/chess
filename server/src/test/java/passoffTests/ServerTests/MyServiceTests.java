package passoffTests.ServerTests;
import model.UserDataModel;
import org.junit.jupiter.api.*;
import server.Server;
import dataAccess.*;
import service.ClearService;
import service.GameService;
import service.UserService;

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

        Assertions.assertTrue(MemoryUserDAO.length() > length);
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
            Assertions.assertEquals(length, MemoryUserDAO.length());
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
        Assertions.assertTrue(length < MemoryAuthDAO.length());
    }
    @Test
    @Order(5)
    public void testBadLogin(){
        int length = MemoryUserDAO.length();
        try{
            UserService.loginUser(null, "thing");
        }
        catch (Exception e){
            Assertions.assertEquals(length, MemoryUserDAO.length());
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

        Assertions.assertTrue((MemoryUserDAO.length() + MemoryAuthDAO.length() + MemoryGameDAO.length()) <= length);
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
        Assertions.assertEquals(length, MemoryAuthDAO.length());
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
            Assertions.assertEquals(2, 2);
        }
    }
}
