package franxx.code.restful.api.service;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.request.RegisterUserRequest;
import franxx.code.restful.api.model.request.UpdateUserRequest;
import franxx.code.restful.api.model.response.UserResponse;
import franxx.code.restful.api.repository.UserRepository;
import franxx.code.restful.api.securty.BCrypt;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Set;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationService validator;

  @Transactional
  public void register(RegisterUserRequest request) {
    validator.setValidator(request);

    if (userRepository.existsById(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already taken");
    }

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    user.setName(request.getName());

    userRepository.save(user);

  }

  public UserResponse get(User user) {
    return UserResponse.builder()
          .username(user.getUsername())
          .name(user.getName())
          .build();
  }

  @Transactional
  public UserResponse update(User user, UpdateUserRequest request) {
    validator.setValidator(request);

    if (Objects.nonNull(request.getName())) {
      user.setName(request.getName());
    }

    if (Objects.nonNull(request.getPassword())) {
      user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    }

    userRepository.save(user);

    return UserResponse.builder()
          .name(user.getName())
          .username(user.getUsername())
          .build();
  }
}
