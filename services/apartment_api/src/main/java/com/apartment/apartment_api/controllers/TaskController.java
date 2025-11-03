package com.apartment.apartment_api.controllers;

import com.apartment.apartment_api.core.builders.AdvancedSpecificationBuilder;
import com.apartment.apartment_api.core.constants.ApiConstants;
import com.apartment.apartment_api.core.mappers.TaskMapper;
import com.apartment.apartment_api.core.models.dtos.ErrorMessage;
import com.apartment.apartment_api.core.models.dtos.TaskCreateDto;
import com.apartment.apartment_api.core.models.dtos.TaskDto;
import com.apartment.apartment_api.core.models.entities.Apartment;
import com.apartment.apartment_api.core.models.entities.Task;
import com.apartment.apartment_api.services.ApartmentService;
import com.apartment.apartment_api.services.TaskService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX_V1)
@Tag(name = ApiConstants.Task.API_TASK_CONTROLLER_NAME, description = "Operations related to task management")
@RequiredArgsConstructor
public class TaskController {

    private final AdvancedSpecificationBuilder<Apartment> specificationBuilder;
    private final ApartmentService apartmentService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Создание новой задачи
     */
    @PostMapping(ApiConstants.Task.API_TASK_CREATE_UPDATE)
    @Operation(
            summary = "Create or update a task",
            description = "Creates a new task or updates an existing one based on the provided TaskCreateDto.",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Task creation DTO",
                    content = @Content(
                            schema = @Schema(implementation = TaskCreateDto.class),
                            examples = @ExampleObject(
                                    name = "Example Task",
                                    value = "{ \"name\": \"Fix plumbing\", \"predicate\": \"address.city.name = 'Минск'\", \"chat_id\": \"@ChannelName or chatId\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation or business error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<TaskDto> create(@RequestBody @Valid TaskCreateDto task) {
        specificationBuilder.validate(task.predicate(), Apartment.class);
        var response = taskService.createTask(taskMapper.dtoToEntity(task), taskMapper::entityToDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Получение списка задач
     */
    @GetMapping(ApiConstants.Task.API_TASK_LIST)
    @Operation(
            summary = "Get list of tasks",
            description = "Retrieves a list of all tasks, optionally filtered by chat_id (issuerId).",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "chat_id",
                            description = "Optional chat identifier to filter tasks by issuerId",
                            example = "550e8400-e29b-41d4-a716-446655440000"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of tasks successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid filter parameter",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<List<TaskDto>> getTasks(@RequestParam(value = "chat_id", required = false) UUID chatId) {
        Specification<Task> spec = Specification.allOf();

        if (chatId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("issuerId"), chatId));
        }
        var list = taskService.getTasks(spec, taskMapper::entityToDto);
        return ResponseEntity.ok(list);
    }

    /**
     * Удаление задачи по ID
     */
    @DeleteMapping(ApiConstants.Task.API_TASK_BY_ID)
    @Operation(
            summary = "Delete task by ID",
            description = "Deletes a task by its unique identifier (UUID).",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            required = true,
                            description = "Unique task identifier",
                            example = "550e8400-e29b-41d4-a716-446655440000"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Task successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID or task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> delete(@RequestParam("id") UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
