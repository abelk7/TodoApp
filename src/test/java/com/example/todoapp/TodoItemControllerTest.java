package com.example.todoapp;

import com.example.todoapp.controller.TodoItemController;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import com.example.todoapp.service.ITodoItemService;
import com.example.todoapp.service.impl.TodoItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoItemController.class)
public class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ITodoItemService todoItemService;

    @Order(1)
    @Test
    void test_getAllTodos() throws Exception {

        List<TodoItem> todos = new ArrayList<>();

        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setCompleted(false);

        TodoItem todoItem2 = new TodoItem();
        todoItem2.setId(2L);
        todoItem2.setTitle("TodoTest2");
        todoItem2.setCompleted(true);

        todos.add(todoItem1);
        todos.add(todoItem2);

        when(todoItemService.findAll()).thenReturn(todos);

        this.mockMvc
                .perform(get("/"))
                .andExpect(view().name("index"))
                .andDo(print())
                .andExpect(model().attribute("todos", todos))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void test_changeStateOfTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setCompleted(false);

        when(todoItemService.findById(anyLong())).thenReturn(todoItem1);

        this.mockMvc.perform(post("/1")
                        .flashAttr("item", todoItem1))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().isFound());
    }
}
