package franxx.code.restful.api.resolver;

import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return User.class.equals(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();
    String headerToken = servletRequest.getHeader("X-API-TOKEN");

    if (headerToken == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    }

    User user = userRepository.findFirstByToken(headerToken)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized"));

    if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthorized");
    }

    return user;
  }
}
