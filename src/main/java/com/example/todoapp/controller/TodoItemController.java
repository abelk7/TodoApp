package com.example.todoapp.controller;

import com.example.todoapp.service.ITodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class TodoItemController {

    private final ITodoItemService todoItemService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("todos", todoItemService.findAll());
        return "index";
    }
}
