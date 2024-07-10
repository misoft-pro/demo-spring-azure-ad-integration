package pro.misoft.demospringazuread.infra.web;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.*;

public class RestClient {

    public static <B, R> R put(String url, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(PUT, url, body, responseStatus, responseClass);
    }

    public static <B, R> R put(String url, Map<String, String> headers, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(PUT, url, headers, body, responseStatus, responseClass);
    }

    public static <R> R post(String url, HttpStatus responseStatus, Class<R> responseClass) {
        return post(url, "", responseStatus, responseClass);
    }

    public static <B, R> R post(String url, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return post(url, Map.of(), body, responseStatus, responseClass);
    }

    public static <B, R> R post(String url, Map<String, String> headers, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(POST, url, headers, body, responseStatus, responseClass);
    }

    public static void put(String url, Map<String, String> headers, File file, String controlName, HttpStatus responseStatus) {
        executeFormData(PUT, url, headers, file, controlName, responseStatus);
    }

    public static <B, R> R delete(String url, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(DELETE, url, body, responseStatus, responseClass);
    }

    public static <B, R> R patch(String url, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(PATCH, url, body, responseStatus, responseClass);
    }

    public static <B, R> R patch(String url, Map<String, String> headers, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(PATCH, url, headers, body, responseStatus, responseClass);
    }

    public static <R> R get(String url, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(GET, url, Map.of(), "", responseStatus, responseClass);
    }

    public static <R> R get(String url, Map<String, String> headers, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(GET, url, headers, "", responseStatus, responseClass);
    }

    private static <B, R> R execute(Method method, String url, B body, HttpStatus responseStatus, Class<R> responseClass) {
        return execute(method, url, Map.of(), body, responseStatus, responseClass);
    }

    private static <B, R> R execute(Method method, String url, Map<String, String> headers, B body, HttpStatus expectedStatus, Class<R> responseClass) {
        var requestSpecification = body != null ? given().body(body) : given();
        ExtractableResponse<Response> extract = requestSpecification.
                when().
                headers(headers).
                request(method, url).
                then().
                log().all().
                assertThat().
                statusCode(expectedStatus.value()).
                extract();
        if (responseClass == Void.TYPE) {
            return null;
        }
        return extract.as(responseClass);
    }

    public static void executeFormData(Method method, String url, Map<String, String> headers, File file, String controlName, HttpStatus expectedStatus) {
        given().
                multiPart(controlName, file, ContentType.MULTIPART.name()).
                when().
                headers(headers).
                request(method, url).
                then().
                log().all().
                assertThat().
                statusCode(expectedStatus.value()).
                extract();

    }
}
