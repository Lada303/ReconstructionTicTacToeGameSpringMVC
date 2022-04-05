package Lada303.controllersTest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;

public class GameControllerTest {

    @Test
    public void testModeGame() {
        Response response = given()//.log().all()
                            .when().param("mode", "AI").param("type", "json")
                            .contentType(ContentType.JSON)
                            .get("/gameplay/game");
        Assertions.assertEquals(200, response.statusCode());
    }

}
