import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CustomerOrderTest {
    @Before
    public void setUp() {

        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void CustomerOrder() {

        String token =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().extract().body().path("accessToken");
        List<GroupLayout.Group> id = given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .get("/api/ingredients")
                .then().extract().body().path("data._id");
        Response response =given()

                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ", ""))
                .body("{\"ingredients\": [\""+id.get(0)+"\",\""+id.get(1)+"\"]}")
                .when()
                .post("/api/orders");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        Response response2 =given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ", ""))
                .when()
                .get("/api/orders");
        response2.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void CustomerOrderNotAuthorization() {
        String token =given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\", \"name\": \"iratest\"}")
                .when()
                .post("/api/auth/register")
                .then().extract().body().path("accessToken");
        List<GroupLayout.Group> id = given()
                .header("Content-type", "application/json")
                .body("{\"email\": \"iratest@gmail.com\", \"password\": \"1234\"}")
                .when()
                .get("/api/ingredients")
                .then().extract().body().path("data._id");
        Response response =given()
                .header("Content-type", "application/json")
                .auth().oauth2(token.replaceFirst("Bearer ", ""))
                .body("{\"ingredients\": [\""+id.get(0)+"\",\""+id.get(1)+"\"]}")
                .when()
                .post("/api/orders");
        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        Response response2 =given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
        response2.then().assertThat().body("message", equalTo("You should be authorised"))
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
