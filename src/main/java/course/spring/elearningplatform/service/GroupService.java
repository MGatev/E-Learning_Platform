package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;

import java.util.List;

public interface GroupService {
  List<Group> getAllGroups();
  Group getGroupById(Long id);
  Group createGroup(GroupDto groupDto);
  Group deleteGroup(Long id);
  Group addMember(Long id, User user);
  Group removeMember(Long id, User user);
}
