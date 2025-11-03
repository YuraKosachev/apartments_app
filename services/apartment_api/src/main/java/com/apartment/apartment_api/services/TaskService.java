package com.apartment.apartment_api.services;

import com.apartment.apartment_api.core.models.entities.Task;
import com.apartment.apartment_api.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public <T> List<T> getTasks(Specification<Task> specification, Function<Task, T> mapper){
        return taskRepository.findAll(specification).stream().map(mapper).collect(Collectors.toList());
    }

    public void delete(UUID id){
        taskRepository.deleteById(id);
    }

    public <T> T createTask(Task entity, Function<Task, T> mapper){
        return mapper.apply(taskRepository.save(entity));
    }
}
