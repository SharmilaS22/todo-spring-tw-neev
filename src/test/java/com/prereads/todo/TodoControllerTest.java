package com.prereads.todo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TodoService todoService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnStatusOkWhenAllTheTodosAreRetrieved() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnListOfTodosAsResponseWhenGetAllTheTodosAreCalled() throws Exception {
        String content = mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(content, is(equalTo("[]")));
    }

    @Test
    public void shouldCallTheAddMethodOfTodoServiceWhenGetAllTheTodoApiIsCalled() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(status().isOk());
        verify(todoService, times(1)).getAll();
    }

    @Test
    public void shouldReturnStatusCreatedWhenAddANewTodoApiIsCalled() throws Exception {
        Todo todo = new Todo("A new task");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
        verify(todoService, times(1)).add(any());
    }

    @Test
    public void shouldReturnStatusNoContentWhenDeleteAllTodosApiIsCalled() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo"))
                .andExpect(status().isNoContent());
        verify(todoService, times(1)).deleteAll();
    }

    @Test
    public void shouldReturnStatusOkAndTodoObjectAsResponseWhenGetTodoApiIsCalled() throws Exception {
        Todo todo = new Todo("A new task");
        given(todoService.getTodo(1L)).willReturn(todo);

        String content = mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Todo actualTodo = objectMapper.readValue(content, Todo.class);
        assertThat(actualTodo, is(equalTo(todo)));
    }

    @Test
    public void shouldReturnStatusNoContentWhenDeleteTodoApiIsCalled() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/" + id))
                .andExpect(status().isNoContent());
        verify(todoService, times(1)).delete(id);
    }

    @Test
    public void shouldReturnStatusNoContentWhenUpdateTodoIsCalled() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/" + id))
                .andExpect(status().isNoContent());
        verify(todoService, times(1)).update(id, "Updated task");
    }
}
