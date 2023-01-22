import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class AuthorizationTest {
    @Before
    public void setUp() {
                RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void Authorization() {
        given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().statusCode(200);
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .post("/api/auth/login");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void AuthorizationNotPassword() {

        given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().statusCode(200);
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234555\"}")
                .when()
                .post("/api/auth/login");
        response.then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Test
    public void AuthorizationNotEmail() {

        given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().statusCode(200);
        Response response =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"12iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .post("/api/auth/login");
        response.then().assertThat().body("message", equalTo("email or password are incorrect"))
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



