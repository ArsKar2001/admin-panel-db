package com.karmanchik.adminpaneldb.service;

import com.karmanchik.adminpaneldb.model.Group;

import java.util.List;

public interface GroupService {

    Group save(Group group);

    void delete(Group group);

    List<Group> findAll();
}
