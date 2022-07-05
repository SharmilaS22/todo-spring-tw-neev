package com.prereads.todo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;
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

    @Test
    public void shouldCallDeleteAllMethodInTodoRepositoryWhenDeleteMethodIsInvoked() {
        todoService.deleteAll();
        verify(todoRepository, times(1)).deleteAll();
    }

    @Test
    public void shouldReturnTodoElementWhenGetTodoIsCalled() {
        Long id = 1L;
        Todo expectedTodo = new Todo("A new task");
        given(todoRepository.findById(id)).willReturn(Optional.of(expectedTodo));

        Todo todo = todoService.getTodo(id);
        assertThat(todo, is(equalTo(expectedTodo)));
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    public void shouldThrowExceptionWhenGetTodoIsCalledForNonExistentTodo() {
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.empty());
        assertThrows(
                IllegalStateException.class,
                () -> todoService.getTodo(id)
        );
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    public void shouldCallDeleteMethodInTodoRepositoryWhenDeleteMethodIsInvoked() {
        Long id = 1L;
        todoService.delete(id);

        verify(todoRepository, times(1)).deleteById(id);
    }

    @Test
    public void shouldThrowExceptionWhenTodoWithGivenIdDoesNotExistForDeleteMethod() {
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> todoService.delete(id));
    }

    @Test
    public void shouldCallUpdateByIdMethodInTodoRepositoryWhenUpdateMethodIsInvoked() {
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.of(new Todo()));

        todoService.update(id, "Updated Task");

        verify(todoRepository, times(1))
                .updateNameById(id, "Updated Task");
    }

    @Test
    public void shouldThrowExceptionWhenIdDoesNotExistWhenUpdatingTodoObject() {
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> todoService.update(id, "Updated Task"));
    }
}
