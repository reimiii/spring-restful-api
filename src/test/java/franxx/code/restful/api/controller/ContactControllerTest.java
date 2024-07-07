package franxx.code.restful.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateContactRequest;
import franxx.code.restful.api.model.response.ContactResponse;
import franxx.code.restful.api.repository.ContactRepository;
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
class ContactControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    contactRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
    user.setName("Test");
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
    userRepository.save(user);
  }

  @Test
  void createContactBadRequest() throws Exception {
    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("");
    request.setEmail("salah");

    mockMvc.perform(
          post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "test")
    ).andExpectAll(
          status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void createContactSuccess() throws Exception {
    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("me");
    request.setLastName("oo");
    request.setEmail("me@example.com");
    request.setPhone("42342342344");
    System.out.println(objectMapper.writeValueAsString(request));

    mockMvc.perform(
          post("/api/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "test")
    ).andExpectAll(
          status().isOk()
    ).andDo(result -> {
      WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals("me", response.getData().getFirstName());
      assertEquals("oo", response.getData().getLastName());
      assertEquals("me@example.com", response.getData().getEmail());
      assertEquals("42342342344", response.getData().getPhone());

      assertTrue(contactRepository.existsById(response.getData().getId()));
      System.out.println(objectMapper.writeValueAsString(response));
    });

  }
}