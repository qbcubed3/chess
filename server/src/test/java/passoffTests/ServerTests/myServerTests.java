package passoffTests.ServerTests;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class myServerTests {
    @Test
    @Order(1)
    public void testServer(){
        Server server = new Server();
        server.run(8056);
    }
}
