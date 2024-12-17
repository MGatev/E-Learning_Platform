package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.GroupRepository;
import course.spring.elearningplatform.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Group group = EntityMapper.mapCreateDtoToEntity(groupDto, Group.class);
        if (groupRepository.existsByName(groupDto.getName())) {
            throw new DuplicatedEntityException(String.format("Group with name=%s already exists", group.getName()));
        }
        return groupRepository.save(group);
    }

    @Override
    public Group deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Group with id=%s not found", id)));
        groupRepository.deleteById(id);
        return group;
    }
}
