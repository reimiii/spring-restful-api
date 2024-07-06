package franxx.code.restful.api.controller;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.WebResponse;
import franxx.code.restful.api.model.request.RegisterUserRequest;
import franxx.code.restful.api.model.response.UserResponse;
import franxx.code.restful.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(
        path = "/api/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    userService.register(request);

    return WebResponse.<String>builder().data("OK").build();
  }

  @GetMapping(
        path = "/api/users/current",
        produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<UserResponse> get(User user) {
    UserResponse userResponse = userService.get(user);

    return WebResponse.<UserResponse>builder()
          .data(userResponse)
          .build();
  }
}
