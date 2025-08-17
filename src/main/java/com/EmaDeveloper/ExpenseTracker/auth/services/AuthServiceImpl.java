package com.EmaDeveloper.ExpenseTracker.auth.services;

import com.EmaDeveloper.ExpenseTracker.jwt.JwtService;
import com.EmaDeveloper.ExpenseTracker.auth.dto.AuthResponseDTO;
import com.EmaDeveloper.ExpenseTracker.auth.dto.LoginRequestDTO;
import com.EmaDeveloper.ExpenseTracker.auth.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.auth.dto.UserResponseDTO;
import com.EmaDeveloper.ExpenseTracker.roles.entities.Role;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import com.EmaDeveloper.ExpenseTracker.roles.repository.RoleRepository;
import com.EmaDeveloper.ExpenseTracker.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
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
    private final AuthenticationManager authenticationManager;

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
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Assign default role to the user
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                roleNames
        );

        return new AuthResponseDTO(jwt, "Bearer", userResponse);
    }


    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password ", e);
        }

        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .or(() -> userRepository.findByEmail(request.getUsernameOrEmail()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT with extra claims (claims are optional, and they can be used to include additional information in the token)
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getRoles().stream().map(Role::getName).toList());
        // If ID needed in the token, you can add it as well
        String jwt = jwtService.generateToken(extraClaims, user);

        // 4. Create response DTO
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        UserResponseDTO userResponse = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                roleNames
        );
        return new AuthResponseDTO(jwt, "Bearer", userResponse);
    }

    @Override
    public User getCurrentUser(){
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameOrEmail(currentUsername, currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + currentUsername));

    }
}
