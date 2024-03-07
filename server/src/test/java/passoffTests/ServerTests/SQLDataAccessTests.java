package passoffTests.ServerTests;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import dataAccess.SQLGameDAO;
import model.UserDataModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class SQLDataAccessTests {
    @Test
    public void testUserRegister(){
        try{
            SQLUserDAO.createTable();
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
            SQLUserDAO.createTable();
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
            SQLUserDAO.createTable();
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
            SQLUserDAO.createTable();
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
}
