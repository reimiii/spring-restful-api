package franxx.code.restful.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.LoginUserRequest;
import franxx.code.restful.api.model.response.TokenResponse;
import franxx.code.restful.api.repository.UserRepository;
import franxx.code.restful.api.securty.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void loginFailUserNotFound() throws Exception {
    LoginUserRequest userRequest = new LoginUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("test");

    mockMvc.perform(
          post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void loginFailWrongPassword() throws Exception {
    User user = new User();
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
    user.setName("Mee");
    userRepository.save(user);

    LoginUserRequest userRequest = new LoginUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("wrong");

    mockMvc.perform(
          post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void loginSuccess() throws Exception {
    User user = new User();
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
    user.setName("Mee");
    userRepository.save(user);

    LoginUserRequest userRequest = new LoginUserRequest();
    userRequest.setUsername("test");
    userRequest.setPassword("test");

    mockMvc.perform(
          post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isOk()
    ).andDo(result -> {
      WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNull(response.getErrors());
      assertNotNull(response.getData().getToken());
      assertNotNull(response.getData().getExpiredAt());

      User userInDb = userRepository.findById("test").orElse(null);
      assertNotNull(userInDb);
      assertEquals(userInDb.getToken(), response.getData().getToken());
      assertEquals(userInDb.getTokenExpiredAt(), response.getData().getExpiredAt());
    });
  }

  @Test
  void logoutFailed() throws Exception {
    mockMvc.perform(
          delete("/api/auth/logout")
                .accept(MediaType.APPLICATION_JSON)
    ).andExpectAll(
          status().isUnauthorized()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void logoutSuccess() throws Exception {
    User user = new User();
    user.setName("me");
    user.setUsername("test");
    user.setToken("me");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000000L);
    userRepository.save(user);

    mockMvc.perform(
          delete("/api/auth/logout")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "me")
    ).andExpectAll(
          status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());

      User userDb = userRepository.findById("test").orElse(null);
      assertNotNull(userDb);
      assertNull(userDb.getTokenExpiredAt());
      assertNull(userDb.getToken());
    });
  }
}