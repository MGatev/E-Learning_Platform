package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.GroupRepository;
import course.spring.elearningplatform.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public Group getGroupById(Long id) {
      return groupRepository.findById(id)
              .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id=%s not found", id)));
    }

    @Override
    public Group createGroup(GroupDto groupDto) {
        Group groupForCreate = buildGroup(groupDto);
        if (groupRepository.existsByName(groupForCreate.getName())) {
            throw new DuplicatedEntityException(String.format("Group with name=%s already exists", groupForCreate.getName()));
        }
        return groupRepository.save(groupForCreate);
    }

    @Override
    public Group deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id=%s not found", id)));
        groupRepository.deleteById(id);
        return group;
    }

    private Group buildGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());
        group.setMembers(groupDto.getMembers());
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
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id=%s not found", id)));
        group.addMember(user);
        return groupRepository.save(group);
    }
}
