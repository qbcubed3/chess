package passoffTests.ServerTests;
import org.junit.jupiter.api.*;
import server.Server;
import dataAccess.*;

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
    public void testClear(){
    }
}
