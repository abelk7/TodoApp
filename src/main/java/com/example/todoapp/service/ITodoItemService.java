package com.example.todoapp.service;

import com.example.todoapp.model.TodoItem;

import java.util.List;

public interface ITodoItemService {
    List<TodoItem> findAll();
    TodoItem findById(Long id);
    TodoItem save(TodoItem todoItem);
}
