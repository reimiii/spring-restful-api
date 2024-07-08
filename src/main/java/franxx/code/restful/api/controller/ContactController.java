package franxx.code.restful.api.controller;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateContactRequest;
import franxx.code.restful.api.model.request.UpdateContactRequest;
import franxx.code.restful.api.model.response.ContactResponse;
import franxx.code.restful.api.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

  @Autowired
  private ContactService contactService;

  @PostMapping(
      path = "/api/contacts",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<ContactResponse> create(
      User user,
      @RequestBody CreateContactRequest request
  ) {
    ContactResponse response = contactService.create(user, request);

    return WebResponse.<ContactResponse>builder()
        .data(response).build();
  }

  @GetMapping(
      path = "/api/contacts/{contactId}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<ContactResponse> get(
      User user,
      @PathVariable("contactId") String contactId
  ) {
    ContactResponse contactResponse = contactService.get(user, contactId);
    return WebResponse.<ContactResponse>builder().data(contactResponse).build();
  }

  @PutMapping(
      path = "/api/contacts/{contactId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<ContactResponse> update(
      User user,
      @RequestBody UpdateContactRequest request,
      @PathVariable("contactId") String contactId
  ) {
    request.setId(contactId);
    ContactResponse response = contactService.update(user, request);

    return WebResponse.<ContactResponse>builder()
        .data(response).build();
  }
}
