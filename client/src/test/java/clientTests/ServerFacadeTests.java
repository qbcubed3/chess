package clientTests;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.fail;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest(){
        try{
            ClearService.clearDatabase();
            var auth = facade.register("user", "password", "email@email.com");
            System.out.println(auth);
            Assertions.assertNotEquals(auth.authToken(), "");
            Assertions.assertEquals(SQLUserDAO.length(), 1);
        }
        catch (Exception e) {
            fail();
        }
    }
    @Test
    public void badRegisterTest(){
        try{
            ClearService.clearDatabase();
            facade.logout();
            var auth = facade.register("", "k", "email");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void loginTest(){
        try{
            ClearService.clearDatabase();
            facade.register("user", "pass", "email");
            var auth = facade.login("user", "pass");
            Assertions.assertNotNull(auth);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void badLoginTest(){
        try{
            ClearService.clearDatabase();
            facade.logout();
            facade.register("user", "pass", "email");
            var auth = facade.login("newguy", "huh");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2,2);
        }
    }
    @Test
    public void logoutTest(){
        try{
            ClearService.clearDatabase();
            var auth = facade.register("user", "pass", "email");
            var auth2 = facade.logout();
            Assertions.assertEquals(auth.authToken(), auth2.authToken());
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void badLogoutTest(){
        try{
            ClearService.clearDatabase();
            var auth = facade.logout();
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testCreate(){
        try{
            ClearService.clearDatabase();
            facade.register("user", "pass", "email");
            facade.create("game");
            Assertions.assertEquals(SQLGameDAO.length(), 1);
        }
        catch (Exception e){
            fail();
        }
    }
    @Test
    public void testBadCreate(){
        try{
            ClearService.clearDatabase();
            facade.logout();
            facade.create("game");
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testJoin(){
        try{
            ClearService.clearDatabase();
            facade.register("user", "pass", "email");
            int id = facade.create("game");
            facade.join("WHITE", id);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    public void testBadJoin(){
        try{
            ClearService.clearDatabase();
            facade.join("WHITE", 123456789);
            fail();
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }
    @Test
    public void testList(){
        try{
            ClearService.clearDatabase();
            facade.register("user2", "pass", "email");
            facade.create("game");
            facade.create("game2");
            var games = facade.list();
            System.out.println(games);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }
    }
    @Test
    public void testBadList(){
        try{
            ClearService.clearDatabase();
            facade.logout();
            facade.list();
            fail();
        }
        catch (Exception e){
            Assertions.assertTrue(true);
        }
    }
}
