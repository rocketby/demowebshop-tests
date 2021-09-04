import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;

import static io.restassured.RestAssured.given;

public class Demowebshoptests {
    String baseUrl = "http://demowebshop.tricentis.com";
    String logoUrl = "http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png";

    String cookieName = "Nop.customer";
    String cookieValue = "745993f8-bbee-4264-ac59-5278bc8df5e7";
    Cookie generatedCookie = new Cookie(cookieName, cookieValue);

    String expectedResponseMessage = "The product has been added to your <a href=\"/wishlist\">wishlist</a>";

    @Test
    public void addToWishListTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .cookie(cookieName, cookieValue)
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/14/2")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is(expectedResponseMessage))
                        .extract().response();

        String countInWishlist = response.path("updatetopwishlistsectionhtml");

        open(logoUrl);
        getWebDriver().manage().addCookie(generatedCookie);
        open(baseUrl);
        assertThat($(".wishlist-qty").getText()).isEqualTo(countInWishlist);
    }
}
