package com.example.todoapp.it;

import com.example.todoapp.exception.TodoItemNotFoundException;
import com.example.todoapp.model.TodoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoItemControllerTestIT {

    @Autowired
    WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Order(1)
    @Test
    void test_getAllTodos() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(status().isOk());
    }

    @Order(2)
    @Test
    void test_changeStateOfTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setCompleted(true);

        this.mockMvc.perform(post("/1")
                        .flashAttr("item", todoItem1))
                .andExpect(view().name("redirect:/"))
                .andExpect(status().isFound());
    }

    @Order(3)
    @Test
    void test_getTodoItem() throws Exception {
        this.mockMvc
                .perform(get("/edit/" + 1))
                .andExpect(view().name("edit"))
                .andDo(print())
                .andExpect(model().attributeExists("todo"))
                .andExpect(status().isOk());
    }

    @Order(4)
    @Test
    void test_getTodoItem_shoud_thows_TodoItemNotFoundException() throws Exception {

        this.mockMvc
                .perform(get("/edit/" + 400))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TodoItemNotFoundException))
                .andExpect(result -> assertEquals("The todo item with id 400 was not found", result.getResolvedException().getMessage()));
    }

    @Order(5)
    @Test
    void test_editTodoItem() throws Exception {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setDescription("Test description 1");
        todoItem1.setCompleted(false);


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
