package franxx.code.restful.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import franxx.code.restful.api.entity.Contact;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateAddressRequest;
import franxx.code.restful.api.model.response.AddressResponse;
import franxx.code.restful.api.repository.AddressRepository;
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
class AddressControllerTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private AddressRepository addressRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    addressRepository.deleteAll();
    contactRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("test");
    user.setPassword(BCrypt.hashpw("test", BCrypt.gensalt()));
    user.setName("Test");
    user.setToken("test");
    user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
    userRepository.save(user);

    Contact contact = new Contact();
    contact.setId("test");
    contact.setUser(user);
    contact.setFirstName("Eko");
    contact.setLastName("Khanedy");
    contact.setEmail("eko@example.com");
    contact.setPhone("9238423432");
    contactRepository.save(contact);
  }

  @Test
  void createAddressBadRequest() throws Exception {
    CreateAddressRequest request = new CreateAddressRequest();
    request.setCountry("");

    mockMvc.perform(
        post("/api/contacts/test/addresses")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNotNull(response.getErrors());
      System.out.println(
          objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
              .writeValueAsString(response)
      );
    });
  }

  @Test
  void createAddressSuccess() throws Exception {
    CreateAddressRequest request = new CreateAddressRequest();
    request.setStreet("Jalan");
    request.setCity("Jakarta");
    request.setProvince("DKI");
    request.setCountry("Indonesia");
    request.setPostalCode("123123");

    mockMvc.perform(
        post("/api/contacts/test/addresses")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("X-API-TOKEN", "test")
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(request.getStreet(), response.getData().getStreet());
      assertEquals(request.getCity(), response.getData().getCity());
      assertEquals(request.getProvince(), response.getData().getProvince());
      assertEquals(request.getCountry(), response.getData().getCountry());
      assertEquals(request.getPostalCode(), response.getData().getPostalCode());

      assertTrue(addressRepository.existsById(response.getData().getId()));
      System.out.println(
          objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
              .writeValueAsString(response)
      );
    });
  }
}