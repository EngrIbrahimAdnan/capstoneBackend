package com.fullstackbootcamp.capstoneBackend.chat.controller;

import com.fullstackbootcamp.capstoneBackend.chat.bo.CreateChatRequest;
import com.fullstackbootcamp.capstoneBackend.chat.bo.SendMessageRequest;
import com.fullstackbootcamp.capstoneBackend.chat.dto.BusinessDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.MessageDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.chat.entity.MessageEntity;
import com.fullstackbootcamp.capstoneBackend.chat.repository.ChatRepository;
import com.fullstackbootcamp.capstoneBackend.chat.repository.MessageRepository;
import com.fullstackbootcamp.capstoneBackend.chat.service.ChatService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<ChatDTO> createChat(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateChatRequest request
    ) {
        return ResponseEntity.ok(chatService.createChat(authHeader, request.getChatTargetId()));
    }

    @GetMapping("/businesses")
    public ResponseEntity<List<BusinessDTO>> getBusinessesToChatWith(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(chatService.getBusinessesToChatWith(authHeader));
    }

    @PostMapping("/{chatId}/message")
    public ResponseEntity<Void> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long chatId,
            @Valid @RequestBody SendMessageRequest request
    ) {
        chatService.sendMessage(chatId, authHeader, request.getContent());
        // 201 status code
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatMessages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChatMessages(authHeader, chatId));
    }

    @ControllerAdvice
    public class CustomValidationExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid input format. You are likely either passing a null value" +
                    " or an invalid value, such as a string instead of a number.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<Map<String, String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", String.format("Invalid type for parameter '%s'. Expected type: %s",
                    ex.getName(), ex.getRequiredType().getSimpleName()));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage()); // Send the exception message to the frontend
            return ResponseEntity.badRequest().body(errorResponse); // Return as a 400 Bad Request
        }
    }

}