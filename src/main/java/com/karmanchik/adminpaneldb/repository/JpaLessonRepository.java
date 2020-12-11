package com.karmanchik.adminpaneldb.repository;

import com.karmanchik.adminpaneldb.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaLessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByGroupId(@NotNull Integer groupId);
    Iterable<Lesson> findAllBy();

    Optional<Lesson> getLessonByGroupIdAndNumberAndDay(@NotNull Integer groupId, @NotNull String number, @NotNull Integer day);

    boolean existsByDay(@NotNull Integer day);
    boolean existsByNumber(@NotNull String number);
    boolean existsByGroupId(@NotNull Integer groupId);
    boolean existsByGroupIdAndNumberAndDay(@NotNull Integer groupId, @NotNull String number, @NotNull Integer day);
}
