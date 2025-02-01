package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstackbootcamp.capstoneBackend.auth.config.SecurityConfig;
import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.dto.TokenResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenServiceStatus;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.bo.LoginRequest;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtDecoder jwtDecoder;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, JwtDecoder jwtDecoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtDecoder = jwtDecoder;
    }

    // forward request to user service
    public SignupResponseDTO processSignupRequest(CreateUserRequest request) {

        // encode the password before passing the information to the user service
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        SignupResponseDTO response = userService.createUser(request);

        // generate tokens if the creation is successful
        if (response.getStatus().equals(CreateUserStatus.SUCCESS)){

            // obtain user entity to generate refresh and access tokens
            Optional<UserEntity> user = userService.getUserByCivilId(request.getCivilId());

            String accessToken = jwtUtil.generateAccessToken(user.get());
            String refreshToken = jwtUtil.generateRefreshToken(user.get());
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
        }

        return response;
    }

    // load users from predefined file
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

    public TokenResponseDTO refreshAccessToken(String refreshToken) {

        // new responseDTO to return to controller
        TokenResponseDTO response = new TokenResponseDTO();

        // decode the refresh token passed to ensure its type is in-fact REFRESH and not ACCESS
        Jwt jwt = jwtDecoder.decode(refreshToken);

        // ensure the endpoint only accepts refresh tokens, NOT access tokens (per the name)
        if (jwt.getClaims().get("type").equals(TokenTypes.ACCESS.name())) {
            response.setStatus(TokenServiceStatus.TOKEN_INVALID);
            response.setMessage("Refresh token is required when Access token is provided");
            return response;

        }

        // Validate the refresh token
        Claims claims = jwtUtil.validateToken(refreshToken);

        // Extract the username and check its existence in database
        String username = claims.getSubject();
        Optional<UserEntity> user = userService.getUserByUsername(username);


        // true if user does not exist in database
        if (user.isEmpty()) {
            // only status and message fields are assigned.
            // refresh and access tokens are skipped
            response.setStatus(TokenServiceStatus.UNAUTHORIZED);
            response.setMessage("The user is not found in the database ");

            return response;
        }


        // Optional: Check token revocation (if using a refresh token repository)
        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            response.setStatus(TokenServiceStatus.INVALID_REQUEST);
            response.setMessage("Invalid or expired refresh token. ensure it is a refresh token and not access token");
            return response;
        }

        // Generate a new access token
        String accessToken = jwtUtil.generateAccessToken(user.get());
        response.setStatus(TokenServiceStatus.SUCCESS);
        response.setMessage("The access token has been successfully refreshed");

        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;

    }

    public TokenResponseDTO login(LoginRequest loginRequest) {
        // new responseDTO to return to controller
        TokenResponseDTO response = new TokenResponseDTO();

        Optional<UserEntity> user = userService.getUserByCivilId(loginRequest.getCivilId());

        // Handle user not found or incorrect password with a generic message
        if (user.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            response.setStatus(TokenServiceStatus.UNAUTHORIZED);
            response.setMessage("The civil id or password is incorrect");
            return response;
        }

        // generate tokens
        String accessToken = jwtUtil.generateAccessToken(user.get());
        String refreshToken = jwtUtil.generateRefreshToken(user.get());

        response.setStatus(TokenServiceStatus.SUCCESS);
        response.setMessage("successfully logged in");
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

}
