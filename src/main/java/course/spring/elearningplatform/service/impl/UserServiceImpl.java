package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicateEmailException;
import course.spring.elearningplatform.exception.DuplicateUsernameException;
import course.spring.elearningplatform.repository.UserRepository;
import course.spring.elearningplatform.service.ImageService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateUsernameException(userDto.getUsername());
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEmailException(userDto.getEmail());
        }

        User userForCreate = buildUser(userDto);
        return userRepository.save(userForCreate);
    }

    @Override
    public User updateUser(UserDto user) {
        return null;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.valueOf(id)));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllUsersByRole(Role role) {
        return null;
    }

    private User buildUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Set.of(Role.STUDENT.getDescription()));
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        ImageDto imageDto = userDto.getProfilePicture();
        if (imageDto != null) {
            Image savedImage = imageService.createImage(imageDto);
            user.setProfilePicture(savedImage);
        }

        return user;
    }

    @Override
    public User updateUserDetails(Long id, String detail, Object value) {
        User existingUser = getUserById(id);
        switch (detail) {
            case "username":
                existingUser.setUsername((String) value);
                break;
            case "email":
                existingUser.setEmail((String) value);
                break;
            case "name":
                String name = (String) value;
                String[] names = name.split(" ");
                if (names.length != 2) {
                    throw new IllegalArgumentException("Invalid full name: " + value);
                }
                existingUser.setFirstName(names[0]);
                existingUser.setLastName(names[1]);
                break;
            case "role":
                existingUser.setRoles(new HashSet<>(List.of((String) value)));
                break;
            case "profilePicture":
                ImageDto imageDto = (ImageDto) value;
                Image savedImage = imageService.createImage(imageDto);
                existingUser.setProfilePicture(savedImage);
                break;
            default:
                throw new IllegalArgumentException("Invalid user detail: " + detail);
        }
        save(existingUser);
        return existingUser;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
