package franxx.code.restful.api.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class UpdateUserRequest {

  @Size(max = 100)
  private String name;

  @Size(max = 100)
  private String password;
}
