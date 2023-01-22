import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class DataChangeTest {
    @Before
    public void setUp() {
                RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void changeEmail() {
        String token =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().extract().body().path("accessToken");
        Response response=given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ", ""))
                .body("{\"email\": \"4iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .patch("/api/auth/user");
        response.then().assertThat().body("user.email", equalTo("4iratest@gmail.com"))
                .and()
                .statusCode(200);
        String token2 = given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"4iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .post("/api/auth/login")
                .then().extract().body().path("accessToken");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token2.replaceFirst("Bearer ", ""))
                .body("{\"authorization\": \"token2\"}")
                .when()
                .delete("/api/auth/user")
                .then().statusCode(202);
    }

    @Test
    public void changeName() {
        String token =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().extract().body().path("accessToken");
        Response response=given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ", ""))
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"3iratest\"}")
                .when()
                .patch("/api/auth/user");
        response.then().assertThat().body("user.name", equalTo("3iratest"))
                .and()
                .statusCode(200);
    }
    @Test
    public void changeNotAuthorization() {
        given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().extract().body().path("accessToken");
        Response response=given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"3iratest\"}")
                .when()
                .patch("/api/auth/user");
        response.then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
    @After
    public void delete() {

        String token = given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .post("/api/auth/login")
                .then().extract().body().path("accessToken");
        if (token != null)
        {
           given()
                     .header("Content-type", "application/json")
                    .auth().oauth2(token.replaceFirst("Bearer ", ""))
                    .body("{\"authorization\": \"token\"}")
                    .when()
                    .delete("/api/auth/user")
                    .then().statusCode(202);}

    }
}
