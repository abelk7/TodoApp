package com.example.todoapp.it;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.service.ITodoItemService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoItemServiceTestIT {
    @Autowired
    private ITodoItemService todoItemService;

    @Order(1)
    @Test
    void test_getAllTodos() {
        List<TodoItem> list = todoItemService.findAll();
        assertThat(list).isNotNull();
        assertThat(list).isNotEmpty();
    }
}
