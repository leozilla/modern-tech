package com.leonhart.borred;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonhart.borred.dto.CreateUserRequest;
import com.wix.mysql.EmbeddedMysql;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URL;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PropertySource("application.properties")
public class IndexControllerIntegrationTest {
   private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   private static EmbeddedMysql MYSQLD = anEmbeddedMysql(v5_7_latest)
           .addSchema("integration_test", classPathScript("db/integration_test_setup.sql"))
           .start();

   @LocalServerPort
   private int port;

   private URL base;

   @Autowired
   private TestRestTemplate template;

   @Before
   public void before() throws Exception {
      this.base = new URL("http://localhost:" + port + "/");
   }

   @After
   public void after() {
      MYSQLD.reloadSchema("integration_test");
   }

   @AfterClass
   public static void afterClass() {
      MYSQLD.stop();
   }

   @Test
   public void getHello() throws Exception {
      ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
      assertThat(response.getBody(), equalTo("Greetings from Spring Boot!"));
   }

   @Test
   public void postUser() throws Exception {
      URI url = URI.create(base.toString() + "user");
      CreateUserRequest request = new CreateUserRequest("John");
      ResponseEntity<String> response = postAsJson(url, request);

      assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
      assertThat(response.getHeaders().get("Location"), contains(url.toString() + "/1"));
   }

   @Test
   public void postAndGetUser() throws Exception {
      ResponseEntity<String> postUserResponse = postUser("David");

      URI getUserUrl = getFirstLocation(postUserResponse);
      ResponseEntity<String> getUserResponse = getAsString(getUserUrl);

      User user = readAsJson(getUserResponse, User.class);
      assertThat(getUserResponse.getStatusCode(), equalTo(HttpStatus.OK));
      assertThat(user.getName(), equalTo("David"));
   }

   @Test
   public void postAndGetAllUsers() throws Exception {
      postUser("David");
      postUser("Franz");
      postUser("Josef");

      URI url = URI.create(base.toString() + "user");
      ResponseEntity<String> getUsersResponse = template.getForEntity(url, String.class);

      assertThat(getUsersResponse.getStatusCode(), equalTo(HttpStatus.OK));

      User[] users = readAsJson(getUsersResponse, User[].class);
      assertThat(users, arrayWithSize(3));
   }

   private ResponseEntity<String> postUser(final String name) throws JsonProcessingException {
      URI url = URI.create(base.toString() + "user");
      CreateUserRequest request = new CreateUserRequest(name);
      return postAsJson(url, request);
   }

   private URI getFirstLocation(final ResponseEntity<String> response) {
      return URI.create(response.getHeaders().get("Location").iterator().next());
   }

   private ResponseEntity<String> getAsString(final URI getUserUrl) {
      return template.getForEntity(getUserUrl.toString(), String.class);
   }

   private ResponseEntity<String> postAsJson(final URI url, final Object entity) throws JsonProcessingException {
      return template.exchange(url, HttpMethod.POST, jsonHttpEntity(entity), String.class);
   }

   private <T> T readAsJson(final ResponseEntity<String> response, final Class<T> clazz) throws java.io.IOException {
      return OBJECT_MAPPER.readValue(response.getBody(), clazz);
   }

   private HttpEntity<String> jsonHttpEntity(final Object request) throws JsonProcessingException {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      return new HttpEntity<>(OBJECT_MAPPER.writeValueAsString(request), headers);
   }
}