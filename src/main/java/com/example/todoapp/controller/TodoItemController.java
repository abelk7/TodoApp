package com.example.todoapp.controller;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.service.ITodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class TodoItemController {

    private final ITodoItemService todoItemService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("todos", todoItemService.findAll());
        return "index";
    }

    @PostMapping(value = "/{id}")
    public String changeStateOfTodoItem(@PathVariable Long id, @Valid @ModelAttribute("item") TodoItem todo) {
        TodoItem todoItem = todoItemService.findById(id);
        todoItem.setLastUpdated(LocalDateTime.now());
        todoItem.setCompleted(!todoItem.isCompleted());
        todoItemService.save(todoItem);
        return "redirect:/";
    }
}
