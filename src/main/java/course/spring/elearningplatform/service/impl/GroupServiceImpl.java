package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Override
    public List<Group> getAllGroups() {
        return List.of();
    }

    @Override
    public Group getGroupById(Long id) {
        return null;
    }

    @Override
    public Group createGroup(GroupDto groupDto) {
        return null;
    }
}
