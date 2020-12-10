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
    List<Lesson> findAllByGroup(@NotNull String group);
    Iterable<Lesson> findAllBy();

    Optional<Lesson> getLessonByGroupAndNumberAndDay(@NotNull String group, @NotNull String number, @NotNull Integer day);

    boolean existsByDay(@NotNull Integer day);
    boolean existsByNumber(@NotNull String number);
    boolean existsByGroup(@NotNull String group);
    boolean existsByGroupAndNumberAndDay(@NotNull String group, @NotNull String number, @NotNull Integer day);
}
