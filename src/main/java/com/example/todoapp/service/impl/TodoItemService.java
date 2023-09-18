package com.example.todoapp.service.impl;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import com.example.todoapp.service.ITodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoItemService implements ITodoItemService {

    private final TodoItemRepository todoItemRepository;

    @Override
    public List<TodoItem> findAll() {
        return todoItemRepository.findAll();
    }
}
