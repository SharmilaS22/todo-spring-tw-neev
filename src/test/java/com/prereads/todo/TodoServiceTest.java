package com.prereads.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void shouldReturnAListOfTodosWhenGetAllIsCalled() {
        List<Todo> todos = todoService.getAll();
        when(todoRepository.findAll()).thenReturn(List.of());

        assertThat(todos, is(equalTo(List.of())));
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void shouldCallSaveMethodInTodoRepositoryWhenAddMethodIsInvoked() {
        todoService.add("A new task");
        verify(todoRepository, times(1)).save(any());
    }
}
