import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostmanEchoTest {
    private final String baseUrl = "https://postman-echo.com";

    @Test
    @DisplayName("Проверка GET метода")
    public void testGetMethod() {
        Response response = RestAssured.get(baseUrl + "/get?foo1=bar1&foo2=bar2");
        assertEquals(200, response.getStatusCode());

        response.then()
                .body("args.foo1", equalTo("bar1"))
                .body("args.foo2", equalTo("bar2"));
    }

    @Test
    @DisplayName("Проверка POST Raw Text метода")
    public void testPostRawMethod() {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("Проверка RAW текста")
                .post(baseUrl + "/post");

        assertEquals(200, response.getStatusCode());

        response.then()
                .body("data", equalTo("Проверка RAW текста"));
    }

    @Test
    @DisplayName("Проверка POST Form Data метода")
    public void testPostFormMethod(){
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"foo\":\"bar\"}")
                .post(baseUrl + "/post");

        assertEquals(200, response.getStatusCode());

        response.then()
                .body("data.foo", equalTo("bar"));
    }

    @Test
    @DisplayName("Проверка PUT метода")
    public void testPutMethod() {
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"foo\":\"bar\"}")
                .put(baseUrl + "/put");

        assertEquals(200, response.getStatusCode());

        response.then()
                .body("data.foo", equalTo("bar"));
    }

    @Test
    @DisplayName("Проверка PATCH метода")
    public void testPatchMethod(){
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{ \"key\": \"value\" }")
                .patch(baseUrl + "/patch");

        assertEquals(200, response.getStatusCode());

        response.then()
                .body("data.key", equalTo("value"));
    }

    @Test
    @DisplayName("Проверка Delete метода")
    public void testDeleteMethod() {
        Response response = RestAssured.delete(baseUrl + "/delete");

        assertEquals(200, response.getStatusCode());

        response.then().body("args", equalTo(new HashMap<>()));
    }
}
