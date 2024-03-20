package clientTests;

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
            var auth = facade.register("", "k", "email");
        }
        catch (Exception e){
            Assertions.assertEquals(2, 2);
        }
    }

}
