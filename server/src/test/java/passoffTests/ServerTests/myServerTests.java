package passoffTests.ServerTests;
import model.UserDataModel;
import org.junit.jupiter.api.*;
import server.Server;
import dataAccess.*;
import service.ClearService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class myServerTests {
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
    public void testClear(){
        int length = MemoryUserDAO.length() + MemoryAuthDAO.length() + MemoryGameDAO.length();
        ClearService.clearDatabase();

        Assertions.assertTrue((MemoryUserDAO.length() + MemoryAuthDAO.length() + MemoryGameDAO.length()) <= length);
    }
}
