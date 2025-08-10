package com.EmaDeveloper.ExpenseTracker.users.services;

import com.EmaDeveloper.ExpenseTracker.auth.dto.UserRegistrationRequest;
import com.EmaDeveloper.ExpenseTracker.auth.dto.UserResponseDTO;
import com.EmaDeveloper.ExpenseTracker.roles.entities.Role;
import com.EmaDeveloper.ExpenseTracker.users.entities.User;
import com.EmaDeveloper.ExpenseTracker.roles.repository.RoleRepository;
import com.EmaDeveloper.ExpenseTracker.users.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO getUserById(Long id) {
                User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id : " + id + " not found"));
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));
        return convertToUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, @Valid UserRegistrationRequest updatedUserRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingUser.setUsername(updatedUserRequest.getUsername());
        existingUser.setEmail(updatedUserRequest.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return convertToUserResponseDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );
    }
}

