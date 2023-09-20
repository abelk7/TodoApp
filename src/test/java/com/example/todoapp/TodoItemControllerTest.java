package com.example.todoapp;

import com.example.todoapp.controller.TodoItemController;
import com.example.todoapp.exception.TodoItemNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

        this.mockMvc.perform(post("/" + todoItem1.getId())
                        .flashAttr("item", todoItem1))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().isFound());
    }

    @Order(3)
    @Test
    void test_getTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setDescription("Testest description");
        todoItem1.setCompleted(false);

        when(todoItemService.findById(anyLong())).thenReturn(todoItem1);

        this.mockMvc
                .perform(get("/edit/" + todoItem1.getId()))
                .andExpect(view().name("edit"))
                .andDo(print())
                .andExpect(model().attribute("todo", todoItem1))
                .andExpect(status().isOk());
    }

    @Order(4)
    @Test
    void test_getTodoItem_shoud_thows_TodoItemNotFoundException() throws Exception {

        when(todoItemService.findById(anyLong())).thenReturn(null);

        this.mockMvc
                .perform(get("/edit/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TodoItemNotFoundException))
                .andExpect(result -> assertEquals("The todo item with id 1 was not found", result.getResolvedException().getMessage()));
    }

    @Order(5)
    @Test
    void test_editTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setDescription("Test description 1");
        todoItem1.setCompleted(false);

        when(todoItemService.findById(anyLong())).thenReturn(todoItem1);

        this.mockMvc.perform(post("/edit/")
                        .flashAttr("todo", todoItem1))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().isFound());
    }

    @Order(6)
    @Test
    void test_addTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("TodoTest1");

        when(todoItemService.save(any(TodoItem.class))).thenReturn(todoItem1);

        this.mockMvc.perform(post("/")
                        .flashAttr("todo", todoItem1))
                .andExpect(view().name("index"))
                .andExpect(status().isCreated());
    }

    @Order(7)
    @Test
    void test_addTodoItem_with_EmptyTitle() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setTitle("");

        this.mockMvc.perform(post("/")
                        .flashAttr("todo", todoItem1))
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }
}
