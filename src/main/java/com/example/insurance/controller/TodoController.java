package com.example.insurance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.insurance.model.Todo;
import com.example.insurance.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService service;

    @GetMapping
    public List<Todo> getAll() throws Exception {
        return service.getAll();
    }

    @PostMapping
    public Todo create(@RequestBody Map<String, String> body) throws Exception {
        return service.create(body.get("title"));
    }

    @PutMapping("/{id}")
    public void toggle(
        @PathVariable String id,
        @RequestBody Map<String, Boolean> body
    ) throws Exception {
        service.toggle(id, body.get("completed"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
