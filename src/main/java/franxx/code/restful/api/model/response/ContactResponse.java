package franxx.code.restful.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class ContactResponse {
  private String id;

  private String firstName;

  private String lastName;

  private String email;

  private String phone;
}
