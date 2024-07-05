package franxx.code.restful.api.service;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.request.LoginUserRequest;
import franxx.code.restful.api.model.response.TokenResponse;
import franxx.code.restful.api.repository.UserRepository;
import franxx.code.restful.api.securty.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ValidationService validationService;

  @Transactional
  public TokenResponse login(LoginUserRequest request) {
    validationService.setValidator(request);

    User user = userRepository.findById(request.getUsername())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong"));

    if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
      user.setToken(UUID.randomUUID().toString());
      user.setTokenExpiredAt(next30Days());
      userRepository.save(user);

      return TokenResponse.builder()
            .token(user.getToken())
            .expiredAt(user.getTokenExpiredAt())
            .build();
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong");
    }

  }

  private Long next30Days() {
    return System.currentTimeMillis() + (1_000 * 16 * 24 * 30);
  }
}
