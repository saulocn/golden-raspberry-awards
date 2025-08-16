package br.com.saulocn.goldenraspberryawards.resource;

import br.com.saulocn.goldenraspberryawards.resource.vo.Interval;
import br.com.saulocn.goldenraspberryawards.resource.vo.MovieVO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class IntervalResourceTest {

    @Test
    void testGetIntervals() {
        given()
                .when()
                .get("/intervals")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .body("min", notNullValue())
                .body("max", notNullValue());
    }

    @Test
    void testGetIntervalsValues() {
        var minInterval = new Interval("Joel Silver", 1, 1990, 1991);
        var maxInterval = new Interval("Matthew Vaughn", 13, 2002, 2015);

        given()
                .when()
                .get("/intervals")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("min[0].followingWin", equalTo(minInterval.followingWin()))
                .body("min[0].interval", equalTo(minInterval.interval()))
                .body("min[0].producer", equalTo(minInterval.producer()))
                .body("min[0].previousWin", equalTo(minInterval.previousWin()))
                .body("max[0].followingWin", equalTo(maxInterval.followingWin()))
                .body("max[0].interval", equalTo(maxInterval.interval()))
                .body("max[0].producer", equalTo(maxInterval.producer()))
                .body("max[0].previousWin", equalTo(maxInterval.previousWin()));

    }

    @Test
    void testGetIntervalsInsertingValues() {
        var minInterval = new Interval("Allan Carr", 1, 1980, 1981);
        var maxInterval = new Interval("Matthew Vaughn", 13, 2002, 2015);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            MovieVO movieVO = MovieVO.of(1981, "Can't Stop the Music", "Allan Carr", true, "Polygram");
            given()
                    .body(jsonb.toJson(movieVO))
                    .header("Content-Type", "application/json")
                    .when()
                    .post("/movies")
                    .then()
                    .statusCode(201);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        given()
                .when()
                .get("/intervals")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("min[1].followingWin", equalTo(minInterval.followingWin()))
                .body("min[1].interval", equalTo(minInterval.interval()))
                .body("min[1].producer", equalTo(minInterval.producer()))
                .body("min[1].previousWin", equalTo(minInterval.previousWin()))
                .body("max[0].followingWin", equalTo(maxInterval.followingWin()))
                .body("max[0].interval", equalTo(maxInterval.interval()))
                .body("max[0].producer", equalTo(maxInterval.producer()))
                .body("max[0].previousWin", equalTo(maxInterval.previousWin()));

    }
}