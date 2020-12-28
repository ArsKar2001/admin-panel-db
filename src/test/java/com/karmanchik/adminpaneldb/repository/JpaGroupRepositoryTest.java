package com.karmanchik.adminpaneldb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class JpaGroupRepositoryTest {
    private JpaGroupRepository groupRepository;

    JpaGroupRepositoryTest(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Test
    void testListGroupName() {
        List<String> groups = groupRepository.getListGroupName();
        groups.size();
    }
}