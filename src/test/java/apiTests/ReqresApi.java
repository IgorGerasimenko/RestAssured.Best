package apiTests;

import apiTests.pojoClasses.User;
import utils.Counter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class ReqresApi {

    private final static String URL = "https://reqres.in/";
    private final int pageCount = getPageCount();

    private int getPageCount() {
        int pageCount = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "api/users?page=1")
                .then()
                .log().all()
                .extract().body().jsonPath().getInt("total_pages");
        return pageCount;
    }

    public User findUser(String firstName, String lastName, int pageNumber) {
        List<User> searchableUser = given()
                .contentType(ContentType.JSON)
                .when()
                .get("https://reqres.in/api/users?page=" + pageNumber)
                .then()
                .log().all()
                .extract().jsonPath().getList("data", User.class);

        User user = searchableUser.stream()
                .filter(p ->
                        firstName.equals(p.getFirst_name()) &
                                lastName.equals(p.getLast_name()))
                .findAny()
                .orElse(null);
        return user;
    }



    @Test
    public void checkEmailPatternForAllUsersOnThisPage() throws IOException {
        Counter counter = new Counter("fdsfsd");
        counter.writeIncrement();
        List<User> users = given()
                        .contentType(ContentType.JSON)
                        .when()
                        .get(URL+"api/users?page=2")
                        .then()
                        .log().all()
                        .extract().body().jsonPath().getList("data",User.class);
        assertNotNull(users.stream().findFirst().get().getFirst_name());
        assertTrue(users.stream().allMatch(x->x.getEmail().startsWith(x.getFirst_name().toLowerCase() + "." + x.getLast_name().toLowerCase())));
    }

    @Test
    public void checkEmailPatternForAllUsersOnAllPages(){

        for(int i =1; i<=pageCount; i++){
            List<User> users = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(URL+"api/users?page="+i)
                    .then()
                    .log().all()
                    .extract().body().jsonPath().getList("data",User.class);
            assertNotNull(users.stream().findFirst().get().getFirst_name());
            assertTrue(users.stream().allMatch(x->x.getEmail().startsWith(x.getFirst_name().toLowerCase() + "." + x.getLast_name().toLowerCase())));
        }

    }

    @Test
    public void checkEmailPatternForGeorgeEdwards() throws IOException {

        for(int i =0; i<pageCount; i++){

            for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
                User user = findUser("George", "Edwards", pageNumber);

                if (user == null) {
                    System.out.println("На этой странице не найдено такого пользователя, поищу на следующей");

                } else {
                    System.out.println("Пользователь найден, проверяю его email");
                    assertNotNull(user.getLast_name(), user.getFirst_name());
                    assertEquals(user.getEmail(), user.getFirst_name().toLowerCase() + "." + user.getLast_name().toLowerCase() + "@reqres.in");
                    return;
                }
            }
            Assertions.fail("что-то пошло не так, скорее всего пользователь не был найден");
        }

    }












/*    @Test
    void checkUserNameByGroovy() {

        given()
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("data.findAll{it.email}.email.flatten().",
                        hasItem("emma.wong@reqres.in"));
    }

    @Test
    void createUser() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"kotlomoy\"\n" +
                        "}")
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .body("name", is("morpheus"))
                .body("job", is("kotlomoy"));
    }

    @Test
    void deleteUser() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    void checkLoginUnSuccessful() {
        given()
                .spec(requestSpec)
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
    public String generateStringFromResource(String path) throws IOException {

        return new String(Files.readAllBytes(Paths.get(path)));

    }

    @Test
    void checkLoginSuccessful() throws IOException {
        String jsonBody = generateStringFromResource("src/main/resources/223.json");
        given()
                .spec(requestSpec)
                .body(jsonBody)
                .when()
                .post("/register")
                .then()
                .spec(responseSpec)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void checkLoginSuccessfulWithModels() {
        AuthData data =
                given()
                        .spec(requestSpec)
                        .body("{\n" +
                                "    \"email\": \"eve.holt@reqres.in\",\n" +
                                "    \"password\": \"cityslicka\"\n" +
                                "}")
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpec)
                        .extract().as(AuthData.class);

        assertEquals("QpwL5tke4Pnpja7X4", data.getToken());
    }*/

}