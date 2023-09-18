package com.example.todoapp.it;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.service.ITodoItemService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
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

    @Order(2)
    @Test
    void test_findById() {
        TodoItem todoItem = todoItemService.findById(1L);

        assertThat(todoItem).isNotNull();
        assertThat(todoItem.getId()).isEqualTo(1L);
    }

    @Order(3)
    @Test
    void test_save() {
        TodoItem todoItem5 = new TodoItem();
        todoItem5.setTitle("TodoTest556465464654654");
        todoItem5.setCompleted(false);
        todoItem5.setLastUpdated(LocalDateTime.now());

        TodoItem todoItem = todoItemService.save(todoItem5);

        assertThat(todoItem).isNotNull();
        assertThat(todoItem.getTitle()).isEqualTo(todoItem5.getTitle() );
    }
}
