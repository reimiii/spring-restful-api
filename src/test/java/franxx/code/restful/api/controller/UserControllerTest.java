package franxx.code.restful.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.RegisterUserRequest;
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
class UserControllerTest {

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
  void createSuccess() throws Exception {

    RegisterUserRequest userRequest = RegisterUserRequest.of(
          "test",
          "password",
          "Mee Mee"
    );

    mockMvc.perform(
          post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isOk()
    ).andDo(result -> {
      WebResponse<String> stringWebResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
      });

      assertEquals("OK", stringWebResponse.getData());
    });
  }

  @Test
  void createBadRequest() throws Exception {

    RegisterUserRequest userRequest = RegisterUserRequest.of(
          "",
          "",
          ""
    );

    mockMvc.perform(
          post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> stringWebResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
      });

      assertNotNull(stringWebResponse.getErrors());
    });
  }

  @Test
  void createDuplicate() throws Exception {
    User user = new User();
    user.setName("Me");
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
    userRepository.save(user);

    RegisterUserRequest userRequest = RegisterUserRequest.of(
          "test",
          "password",
          "Mee Mee"
    );

    mockMvc.perform(
          post("/api/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
          status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> stringWebResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
      });

      assertNotNull(stringWebResponse.getErrors());
    });
  }

}