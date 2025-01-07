package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    // forward request to user service
    public SignupResponseDTO processSignupRequest(CreateUserRequest request) {
        return userService.createUser(request);
    }

    // load users

    public <T> LoadUsersResponseDTO loadEntites(
            String file,
            TypeReference<List<T>> typeReference
    ) {

        // Load entities from the JSON file
        List<T> entities = loadEntities(file, typeReference);


        // Returns here only when the file is not found
        if (entities == null) {
            LoadUsersResponseDTO response = new LoadUsersResponseDTO();
            response.setStatus(CreateUserStatus.FAIL);
            response.setMessage("Unable to find the file. Check the path to file '" + file + "'");
            return response;
        }

        try {
            // Loop through and save each entity
            for (T entity : entities) {
                switch (file) {


                    case "users.json":
                        CreateUserRequest user = new CreateUserRequest();
                        BeanUtils.copyProperties(entity, user);

                        LoadUsersResponseDTO response = new LoadUsersResponseDTO();
                        SignupResponseDTO singupResponse = processSignupRequest(user);
                        BeanUtils.copyProperties(singupResponse, response);

                        if (response.getStatus() != CreateUserStatus.SUCCESS) {
                            response.setMessage(singupResponse.getMessage() + "(REF: Username: " + user.getUsername() + "/ Civil ID: " + user.getCivilId() + ')');
                            return response;
                        } else {
                            continue;
                        }


                    case "anotherFile.json":
                        break;

                    default:
                        LoadUsersResponseDTO responseDefault = new LoadUsersResponseDTO();
                        responseDefault.setStatus(CreateUserStatus.FAIL);
                        responseDefault.setMessage("Unable to find the file.");
                        return responseDefault;
                }
            }
            LoadUsersResponseDTO response = new LoadUsersResponseDTO();
            response.setStatus(CreateUserStatus.SUCCESS);
            response.setMessage("Added all users successfully.");
            return response;

        } catch (Exception e) {
            LoadUsersResponseDTO response = new LoadUsersResponseDTO();
            response.setStatus(CreateUserStatus.FAIL);
            response.setMessage("Unable to add data to database. Ensure they are in the expected JSON structure");

            // Return an error string if there is an issue saving to the database
            return response;
        }
    }

    public static <T> List<T> loadEntities(String fileName, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Load the file from the resources directory
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("predefinedData/" + fileName)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (Exception e) {
            return null; // Handle the exception or return an appropriate response
        }
    }

}
