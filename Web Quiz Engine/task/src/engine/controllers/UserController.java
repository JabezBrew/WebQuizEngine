package engine.controllers;

import engine.exceptions.UserAlreadyExistsException;
import engine.repos.UserRepository;
import engine.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    UserRepository userRepo;
    PasswordEncoder encoder;
    @Autowired
    public UserController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @PostMapping("/api/register")
    public void registerUser(@Valid @RequestBody User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) { //checking if user with this email already exists
            throw new UserAlreadyExistsException();
        } else {
            user.setPassword(encoder.encode(user.getPassword())); //setting password to encoded password
            userRepo.save(user);
            System.out.println("User registered");
        }
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return (List<User>) userRepo.findAll();
    }

    @DeleteMapping("/api/users")
    public String deleteAllUsers() {
        userRepo.deleteAll();
        return "All users deleted";
    }


}
