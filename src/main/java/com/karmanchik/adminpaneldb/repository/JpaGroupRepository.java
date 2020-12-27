package com.karmanchik.adminpaneldb.repository;

import com.karmanchik.adminpaneldb.model.Group;
import org.json.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> getGroupByGroupName(@NotNull String groupName);

    @Modifying
    @Query(value = "insert into groups(id, group_name, timetable) values (nextval('global_seq'),:group_name, :timetable)",
            nativeQuery = true)
    void insert(@Param("group_name") String group_name, @Param("timetable") JSONArray timetable);
}
