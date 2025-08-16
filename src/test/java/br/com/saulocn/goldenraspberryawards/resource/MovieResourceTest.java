package br.com.saulocn.goldenraspberryawards.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class MovieResourceTest {

    @Test
    void shouldReturnErrorWhenYearIsInvalid() {
        given()
                .body("{\"year\": 1800, \"title\": \"Invalid Movie\", \"producer\": \"Test Producer\", \"winner\": false, \"studios\": \"Test Studios\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("violations[0].message", equalTo("Invalid year, min 1900"));
    }

    @Test
    void shouldReturnErrorWhenTitleIsEmpty() {
        given()
                .body("{\"year\": 2023, \"title\": \"\", \"producer\": \"Test Producer\", \"winner\": false, \"studios\": \"Test Studios\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("violations[0].message", equalTo("não deve estar vazio"));
    }

    @Test
    void shouldReturnErrorWhenProducersIsEmpty() {
        given()
                .body("{\"year\": 2023, \"title\": \"Test\", \"producer\": \"\", \"winner\": false, \"studios\": \"Test Studios\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("violations[0].message", equalTo("não deve estar vazio"));
    }

    @Test
    void shouldReturnErrorWhenStudiosIsEmpty() {
        given()
                .body("{\"year\": 2023, \"title\": \"Test\", \"producer\": \"Test Producer\", \"winner\": false, \"studios\": \"\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/movies")
                .then()
                .statusCode(400)
                .body("violations[0].message", equalTo("não deve estar vazio"));
    }

    @Test
    void shouldReturnCreatedWhenMovieIsValid() {
        given()
                .body("{\"year\": 2023, \"title\": \"Valid Movie\", \"producer\": \"Test Producer\", \"winner\": false, \"studios\": \"Test Studios\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/movies")
                .then()
                .statusCode(201)
                .body("title", equalTo("Valid Movie"))
                .body("year", equalTo(2023))
                .body("producer", equalTo("Test Producer"))
                .body("studios", equalTo("Test Studios"))
                .body("winner", equalTo(false));
    }

    @Test
    void shouldReturnMovieById() {
        given()
                .when()
                .get("/movies/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void shouldReturnNotFoundWhenMovieDoesNotExist() {
        given()
                .when()
                .get("/movies/999")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldReturnAllMovies() {
        given()
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}