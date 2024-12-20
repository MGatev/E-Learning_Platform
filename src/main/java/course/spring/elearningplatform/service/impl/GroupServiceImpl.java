package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.GroupRepository;
import course.spring.elearningplatform.service.GroupService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group getGroupById(Long id) {
      return groupRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %s not found", id), "redirect:/groups"));
    }

    @Override
    public Group createGroup(GroupDto groupDto) {
        Group groupForCreate = buildGroup(groupDto);
        if (groupRepository.existsByName(groupForCreate.getName())) {
            throw new DuplicatedEntityException(String.format("Group with name %s already exists", groupForCreate.getName()));
        }
        return groupRepository.save(groupForCreate);
    }

    @Override
    public Group deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %s not found", id), "redirect:/groups"));
        groupRepository.deleteById(id);
        return group;
    }

    private Group buildGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        groupDto.getMembers().removeAll(List.of(""));
        Set<User> members = groupDto.getMembers().stream().map(userService::getUserByUsername).collect(Collectors.toCollection(HashSet::new));
        group.setMembers(members);
        group.setArticles(groupDto.getArticles());
        group.setDescription(groupDto.getDescription());
        MultipartFile image = groupDto.getImage();
        if (!image.isEmpty()) {
            try {
                group.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image!", e);
            }
        }

        return group;
    }

    @Override
    public Group addMember(Long id, User user) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %s not found", id), "redirect:/groups/" + id));
        group.addMember(user);
        return groupRepository.save(group);
    }

    @Override
    public Group removeMember(Long id, User user) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id %s not found", id), "redirect:/groups/" + id));
        group.removeMember(user);
        return groupRepository.save(group);
    }
}
