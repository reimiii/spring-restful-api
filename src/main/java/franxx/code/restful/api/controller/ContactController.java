package franxx.code.restful.api.controller;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateContactRequest;
import franxx.code.restful.api.model.response.ContactResponse;
import franxx.code.restful.api.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
