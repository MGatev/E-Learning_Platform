package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicateEmailException;
import course.spring.elearningplatform.exception.DuplicateUsernameException;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.repository.UserRepository;
import course.spring.elearningplatform.service.ImageService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ImageService imageService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository,
                           BCryptPasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
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
    public User updateUser(User user) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with ID = '%d' not found", user.getId())));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        User deletedUser = userRepository.findByUsername("deletedUser")
                .orElseThrow(() -> new IllegalStateException("'Deleted User' not found. Please seed it."));

        List<Course> courses = courseRepository.findAllByCreatedBy(user);
        for (Course course : courses) {
            course.setCreatedBy(deletedUser);
        }

        courseRepository.saveAll(courses);

        userRepository.delete(user);
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
    public List<User> getAllUsersExcept(List<String> users) {
        return userRepository.findAllByUsernameNotIn(users);
    }

    @Override
    public List<User> getAllUsersByRole(Role role) {
        return null;
    }

    private User buildUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.STUDENT.getDescription()));
        } else {
            user.setRoles(userDto.getRoles());
        }

        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        ImageDto imageDto = userDto.getProfilePicture();
        if (imageDto != null && imageDto.getImage() != null && !imageDto.getImage().isEmpty()) {
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
    public User addStartedCourse(User user, Course course) {
        user.addStartedCourse(course);
        return userRepository.save(user);
    }

    @Override
    public User addCompletedCourse(User user, Course course) {
        user.addCompletedCourse(course);
        return userRepository.save(user);
    }

    @Override
    public User findByIdWithCompletedLessons(Long id) {
        return userRepository.findByIdWithCompletedLessons(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Page<User> getAllUsers(int page, int size, String loggedInUsername) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.findAllByUsernameNotIn(List.of("deletedUser", loggedInUsername, "admin"), pageable);
    }

    @Override
    public Page<User> searchUsers(String searchQuery, int page, int size, String loggedInUsername) {
        Pageable pageable = PageRequest.of(page - 1, size);

        if (searchQuery.contains(" ")) {
            String[] nameParts = searchQuery.split("\\s+", 2);
            String firstNameQuery = nameParts[0];
            String lastNameQuery = nameParts[1];

            return userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndUsernameNotIn(
                    firstNameQuery, lastNameQuery, List.of(loggedInUsername, "admin"), pageable);
        }

        return userRepository.searchUsersExcluding(searchQuery, List.of(loggedInUsername, "deletedUser", "admin"), pageable);
    }
}
