package com.example.todoapp.service.impl;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import com.example.todoapp.service.ITodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TodoItemService implements ITodoItemService {

    private final TodoItemRepository todoItemRepository;

    @Override
    public List<TodoItem> findAll() {
        List<TodoItem> todos = todoItemRepository.findAll(Sort.by(Sort.Direction.ASC, "lastUpdated"));
        List<TodoItem> todosCompleted = new ArrayList<>();
        List<TodoItem> todosUnCompleted = new ArrayList<>();

        for (TodoItem todo: todos) {
            if(todo.isCompleted()) {
                todosCompleted.add(todo);
            }else {
                todosUnCompleted.add(todo);
            }
        }
        todosCompleted.sort(Comparator.comparing(TodoItem::getLastUpdated));
        todosUnCompleted.addAll(todosCompleted);

        return todosUnCompleted;
    }

    @Override
    public TodoItem findById(Long id) {
        Optional<TodoItem> todo =  todoItemRepository.findById(id);
        return todo.orElse(null);
    }

    @Override
    public TodoItem save(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }
}
