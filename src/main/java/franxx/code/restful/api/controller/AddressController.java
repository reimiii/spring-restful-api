package franxx.code.restful.api.controller;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.CreateAddressRequest;
import franxx.code.restful.api.model.response.AddressResponse;
import franxx.code.restful.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {

  @Autowired
  private AddressService addressService;

  @PostMapping(
      path = "/api/contacts/{contactId}/addresses"
  )
  public WebResponse<AddressResponse> create(
      User user,
      @RequestBody CreateAddressRequest request,
      @PathVariable("contactId") String contactId
  ) {
    request.setContactId(contactId);
    AddressResponse response = addressService.create(user, request);
    return WebResponse.<AddressResponse>builder().data(response).build();
  }
}
