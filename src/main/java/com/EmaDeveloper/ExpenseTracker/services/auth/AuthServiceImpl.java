package com.EmaDeveloper.ExpenseTracker.services.auth;

import com.EmaDeveloper.ExpenseTracker.Jwt.JwtService;
import com.EmaDeveloper.ExpenseTracker.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.dto.UserResponseDTO;
import com.EmaDeveloper.ExpenseTracker.entities.Role;
import com.EmaDeveloper.ExpenseTracker.entities.User;
import com.EmaDeveloper.ExpenseTracker.repository.RoleRepository;
import com.EmaDeveloper.ExpenseTracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponseDTO registerUser(UserRegistrationRequest request) {
        Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());
        Optional<User> userByUsername = userRepository.findByUsername(request.getUsername());

        if(userByEmail.isPresent()){
            throw new RuntimeException("Email already exists");
        }

        if (userByUsername.isPresent()){
            throw new RuntimeException("Username already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default role to the user
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);

        String jwt = jwtService.generateTokenExtraClaims(user);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames
        );

        return new AuthResponseDTO(jwt, "Bearer", userResponse);
    }


    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO request) {
        return null;
    }
}
