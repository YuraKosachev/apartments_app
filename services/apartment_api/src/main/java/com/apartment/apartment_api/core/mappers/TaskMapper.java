package com.apartment.apartment_api.core.mappers;

import com.apartment.apartment_api.core.models.dtos.TaskCreateDto;
import com.apartment.apartment_api.core.models.dtos.TaskDto;
import com.apartment.apartment_api.core.models.entities.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto entityToDto(Task task){
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .issuerId(task.getIssuerId())
                .predicate(task.getPredicate())
                .build();
    }

    public Task dtoToEntity(TaskCreateDto dto){
        return Task.builder()
                .issuerId(dto.chatId())
                .name(dto.name())
                .predicate(dto.predicate())
                .build();
    }
}
