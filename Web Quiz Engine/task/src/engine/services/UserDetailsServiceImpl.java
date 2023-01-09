package engine.services;

import engine.security.UserDetailsImpl;
import engine.entities.User;
import engine.exceptions.NotFoundException;
import engine.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws NotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new NotFoundException();
        }
        return new UserDetailsImpl(user);
    }


}
