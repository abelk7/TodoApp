package com.example.todoapp.controller;

import com.example.todoapp.exception.TodoItemNotFoundException;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.service.ITodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class TodoItemController {

    private final ITodoItemService todoItemService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("todo", new TodoItem());
        model.addAttribute("todos", todoItemService.findAll());
        return "index";
    }

    @PostMapping(value = "/{id}")
    public String changeStateOfTodoItem(@PathVariable Long id, @ModelAttribute("item") TodoItem todo) {
        TodoItem todoItem = todoItemService.findById(id);
        todoItem.setLastUpdated(LocalDateTime.now());
        todoItem.setCompleted(!todoItem.isCompleted());
        todoItemService.save(todoItem);
        return "redirect:/";
    }

    @GetMapping(value = "/edit/{id}")
    public String getTodoItem( @PathVariable Long id, Model model) {
        TodoItem todoItem = todoItemService.findById(id);
        if(todoItem == null) {
            throw new TodoItemNotFoundException("The todo item with id " + id + " was not found");
        }
        model.addAttribute("todo", todoItem);
        return "edit";
    }

    @PostMapping(value = "/edit/")
    public String editTodoItem(@Valid @ModelAttribute("todo") TodoItem todo) {
        TodoItem todoItem = todoItemService.findById(todo.getId());
        todoItem.setTitle(todo.getTitle());
        todoItem.setDescription(todo.getDescription());
        todoItem.setLastUpdated(LocalDateTime.now());
        todoItemService.save(todoItem);
        return "redirect:/";
    }

    @PostMapping(value = "/")
    public ModelAndView addTodoItem(@Valid @ModelAttribute("todo") TodoItem todo, BindingResult result, ModelMap model) {
        ModelAndView mav = new ModelAndView("index", model);

        if(result.hasErrors()) {
            model.addAttribute("todo", todo);
            model.addAttribute("todos", todoItemService.findAll());
            mav.setStatus(HttpStatus.OK);
            return mav;
        }
        todo.setCompleted(false);
        todo.setLastUpdated(LocalDateTime.now());
        todoItemService.save(todo);
        mav.setStatus(HttpStatus.CREATED);

        model.addAttribute("todo", new TodoItem());
        model.addAttribute("todos", todoItemService.findAll());
        return mav;
    }
}
