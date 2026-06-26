package com.theld.tasks.resource;

import com.theld.tasks.domain.Task;
import com.theld.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity<List<Task>> findTask() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        var taskSaved = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskSaved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Task> upadeteTask(@PathVariable Long id, @RequestBody Task newTask) {
        Task taskOld = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        BeanUtils.copyProperties(newTask, taskOld, "id", "createDate");

        taskRepository.save(taskOld);

        return ResponseEntity.ok(taskOld);

    }

    @GetMapping("{id}")
    public  ResponseEntity<Task> getTaskId(@PathVariable Long id) {
        Task taskSeach = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        return ResponseEntity.ok(taskSeach);
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task não encontrada"));

        taskRepository.delete(task);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
