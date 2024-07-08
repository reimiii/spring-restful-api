package franxx.code.restful.api.service;

import franxx.code.restful.api.entity.Contact;
import franxx.code.restful.api.entity.User;
import franxx.code.restful.api.model.request.CreateContactRequest;
import franxx.code.restful.api.model.request.SearchContactRequest;
import franxx.code.restful.api.model.request.UpdateContactRequest;
import franxx.code.restful.api.model.response.ContactResponse;
import franxx.code.restful.api.repository.ContactRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContactService {

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private ValidationService validationService;

  @Transactional
  public ContactResponse create(User user, CreateContactRequest request) {
    validationService.setValidator(request);

    Contact contact = new Contact();
    contact.setId(UUID.randomUUID().toString());
    contact.setFirstName(request.getFirstName());
    contact.setLastName(request.getLastName());
    contact.setEmail(request.getEmail());
    contact.setPhone(request.getPhone());
    contact.setUser(user);

    contactRepository.save(contact);

    return toContactResponse(contact);
  }

  @Transactional(readOnly = true)
  public ContactResponse get(User user, String id) {
    Contact contact = contactRepository.findFirstByUserAndId(user, id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

    return toContactResponse(contact);
  }

  @Transactional
  public ContactResponse update(User user, UpdateContactRequest request) {
    validationService.setValidator(request);

    Contact contact = contactRepository.findFirstByUserAndId(user, request.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

    contact.setFirstName(request.getFirstName());
    contact.setLastName(request.getLastName());
    contact.setEmail(request.getEmail());
    contact.setPhone(request.getPhone());

    contactRepository.save(contact);

    return toContactResponse(contact);
  }

  @Transactional
  public void delete(User user, String contactId) {
    Contact contact = contactRepository.findFirstByUserAndId(user, contactId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found"));

    contactRepository.delete(contact);
  }

  @Transactional(readOnly = true)
  public Page<ContactResponse> search(User user, SearchContactRequest request) {
    Specification<Contact> specification = (root, query, builder) -> {
      Collection<Predicate> predicates = new ArrayList<>();
      predicates.add(builder.equal(root.get("user"), user));

      if (Objects.nonNull(request.getName())) {
        predicates.add(builder.or(
            builder.like(root.get("firstName"), "%" + request.getName() + "%"),
            builder.like(root.get("lastName"), "%" + request.getName() + "%")
        ));
      }

      if (Objects.nonNull(request.getEmail())) {
        predicates.add(builder.or(
            builder.like(root.get("email"), "%" + request.getEmail() + "%")
        ));
      }

      if (Objects.nonNull(request.getPhone())) {
        predicates.add(builder.or(
            builder.like(root.get("phone"), "%" + request.getPhone() + "%")
        ));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

    Page<Contact> contacts = contactRepository.findAll(specification, pageable);
    List<ContactResponse> contactResponses = contacts.getContent()
        .stream()
        .map(this::toContactResponse)
        .toList();

    return new PageImpl<>(contactResponses, pageable, contacts.getTotalElements());
  }

  private ContactResponse toContactResponse(Contact contact) {
    return ContactResponse.builder()
        .id(contact.getId())
        .firstName(contact.getFirstName())
        .lastName(contact.getLastName())
        .email(contact.getEmail())
        .phone(contact.getPhone())
        .build();
  }
}