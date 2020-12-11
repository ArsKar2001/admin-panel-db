package com.karmanchik.adminpaneldb.service;

import com.karmanchik.adminpaneldb.model.Group;
import com.karmanchik.adminpaneldb.repository.JpaGroupRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Log4j
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final JpaGroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group save(Group group) {
        return this.groupRepository.save(group);
    }

    @Override
    public void delete(Group group) {
        this.groupRepository.delete(group);
    }

    @Override
    public List<Group> findAll() {
        return this.groupRepository.findAll();
    }
}
