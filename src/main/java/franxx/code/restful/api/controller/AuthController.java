package franxx.code.restful.api.controller;

import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.LoginUserRequest;
import franxx.code.restful.api.model.response.TokenResponse;
import franxx.code.restful.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping(
        path = "/api/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
  )
  private WebResponse<TokenResponse> login(
        @RequestBody LoginUserRequest request
  ) {
    TokenResponse tokenResponse = authService.login(request);
    return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
  }
}
