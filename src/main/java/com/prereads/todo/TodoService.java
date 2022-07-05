package com.prereads.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TodoService {
    @Autowired
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void add(String task) {
        todoRepository.save(new Todo(task));
    }

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public void deleteAll() {
        todoRepository.deleteAll();
    }

    public Todo getTodo(Long id) {
        return todoRepository.findById(id).orElseThrow(() -> new IllegalStateException("Todo Not Available"));
    }

    @Transactional
    public void delete(Long id) {
        if (todoRepository.findById(id).isEmpty() ) throw new IllegalStateException("Id does not exist");
        todoRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, String updatedName) {
        if(todoRepository.findById(id).isEmpty()) throw new IllegalStateException("Id does not exist");
        todoRepository.updateNameById(id, updatedName);
    }
}
