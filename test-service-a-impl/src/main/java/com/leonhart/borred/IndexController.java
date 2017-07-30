package com.leonhart.borred;

import com.leonhart.borred.dto.CreateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class IndexController {
   private final UserRepository userRepository;

   public IndexController(final UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @RequestMapping("/")
   public String index() {
      return "Greetings from Spring Boot!";
   }

   @PostMapping("/user")
   public @ResponseBody
   ResponseEntity<?> postUser(@RequestBody final CreateUserRequest request, final UriComponentsBuilder uriBuilder) {
      User user = new User();
      user.setName(request.getName());
      User savedUser = userRepository.save(user);

      URI uri = uriBuilder.path("/user/{id}").buildAndExpand(savedUser.getId()).toUri();

      return ResponseEntity.created(uri).build();
   }

   @GetMapping("/user/{id}")
   public @ResponseBody
   User getUser(@PathVariable final Integer id) {
      return userRepository.findOne(id);
   }

   @GetMapping("/user")
   public @ResponseBody
   Iterable<User> getUsers() {
      return userRepository.findAll();
   }
}
