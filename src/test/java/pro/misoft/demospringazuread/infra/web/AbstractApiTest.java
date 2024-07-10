package pro.misoft.demospringazuread.infra.web;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

public abstract class AbstractApiTest {

    @Autowired
    private ServletWebServerApplicationContext ctx;

    @BeforeEach
    void setUpAll() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBasePath("/api")
                .setPort(ctx.getWebServer().getPort())
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().log(LogDetail.ALL).build();
    }
}