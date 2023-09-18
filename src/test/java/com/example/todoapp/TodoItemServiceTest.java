package com.example.todoapp;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TodoItemServiceTest {
    private ITodoItemService todoItemService;
    @Mock
    private TodoItemRepository todoItemRepository;

    @BeforeEach
    private void setup() {
        todoItemService = new TodoItemService(todoItemRepository);
    }

    @Order(1)
    @Test
    void test_getAllTodos() {
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

        when(todoItemRepository.findAll(any(Sort.class))).thenReturn(todos);

        List<TodoItem> todoItemListResult = todoItemService.findAll();

        assertThat(todoItemListResult).isNotNull();
        assertThat(todoItemListResult).isNotEmpty();
        assertThat(todoItemListResult).hasSize(todos.size());
    }

    @Order(2)
    @Test
    void test_findById() {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setCompleted(false);

        when(todoItemRepository.findById(anyLong())).thenReturn(Optional.of(todoItem1));

        TodoItem todoResult = todoItemService.findById(1L);

        assertThat(todoResult).isNotNull();
        assertThat(todoResult.getId()).isEqualTo(todoItem1.getId());
    }

    @Order(3)
    @Test
    void test_save() {
        TodoItem todoItem1 = new TodoItem();
        todoItem1.setId(1L);
        todoItem1.setTitle("TodoTest1");
        todoItem1.setCompleted(false);

        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItem1);

        TodoItem todoItemResult = todoItemService.save(todoItem1);

        assertThat(todoItemResult).isNotNull();
        assertThat(todoItemResult).isEqualTo(todoItem1);
    }
}
