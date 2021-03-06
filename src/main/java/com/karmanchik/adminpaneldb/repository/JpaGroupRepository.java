package com.karmanchik.adminpaneldb.repository;

import com.karmanchik.adminpaneldb.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT s FROM Group s WHERE s.groupName = :groupName")
    Optional<Group> getByGroupName(@Param("groupName") @NotNull String groupName);
}
