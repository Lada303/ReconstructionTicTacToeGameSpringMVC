package Lada303.controllersTest;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class StartControllerTest {

    @Test
    public void testForStartPage() {
        given() .when().get("/gameplay")
                .then().statusCode(200);
    }

}

