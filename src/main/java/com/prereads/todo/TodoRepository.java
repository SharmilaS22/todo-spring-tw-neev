package com.prereads.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Modifying
    @Query("UPDATE Todo t SET t.name = ?2 WHERE t.id = ?1")
    void updateNameById(Long id, String name);
}
