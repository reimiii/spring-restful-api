package franxx.code.restful.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import franxx.code.restful.api.entity.Contact;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateContactRequest;
import franxx.code.restful.api.model.request.UpdateContactRequest;
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

import java.util.UUID;

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

  @Test
  void getContactNotFound() throws Exception {
    mockMvc.perform(
        get("/api/contacts/23123123123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isNotFound()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
      });
      assertNotNull(response.getErrors());
    });
  }

  @Test
  void getContactSuccess() throws Exception {
    User user = userRepository.findById("test").orElseThrow();

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setUser(user);
    contact.setFirstName("Kang Eko");
    contact.setLastName("Khanedy");
    contact.setEmail("eko@example.com");
    contact.setPhone("9238423432");
    contactRepository.save(contact);

    mockMvc.perform(
        get("/api/contacts/" + contact.getId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());

      assertEquals(contact.getId(), response.getData().getId());
      assertEquals(contact.getFirstName(), response.getData().getFirstName());
      assertEquals(contact.getLastName(), response.getData().getLastName());
      assertEquals(contact.getEmail(), response.getData().getEmail());
      assertEquals(contact.getPhone(), response.getData().getPhone());
    });
  }

  @Test
  void updateContactBadRequest() throws Exception {
    UpdateContactRequest request = new UpdateContactRequest();
    request.setFirstName("");
    request.setEmail("salah");

    mockMvc.perform(
        put("/api/contacts/123")
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
  void updateContactSuccess() throws Exception {
    User user = userRepository.findById("test").orElseThrow();

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setUser(user);
    contact.setFirstName("Kang Eko");
    contact.setLastName("Khanedy");
    contact.setEmail("eko@example.com");
    contact.setPhone("9238423432");
    contactRepository.save(contact);

    UpdateContactRequest request = new UpdateContactRequest();
    request.setFirstName("Hilmi");
    request.setLastName("AM");
    request.setEmail("me@meez.com");
    request.setPhone("002");
    System.out.println(objectMapper.writeValueAsString(request));

    mockMvc.perform(
        put("/api/contacts/" + contact.getId())
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
      assertEquals(request.getFirstName(), response.getData().getFirstName());
      assertEquals(request.getLastName(), response.getData().getLastName());
      assertEquals(request.getEmail(), response.getData().getEmail());
      assertEquals(request.getPhone(), response.getData().getPhone());

      assertTrue(contactRepository.existsById(response.getData().getId()));
      System.out.println(objectMapper.writeValueAsString(response));
    });

  }
}