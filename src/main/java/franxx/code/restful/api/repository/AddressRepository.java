package franxx.code.restful.api.repository;

import franxx.code.restful.api.entity.Address;
import franxx.code.restful.api.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
  Optional<Address> findFirstByContactAndId(Contact contact, String id);

  List<Address> findAllByContact(Contact contact);
}
