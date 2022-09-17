package web.fiiit.userservice.controller;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.fiiit.userservice.config.DtoConfiguration;
import web.fiiit.userservice.dto.user.UserLogin;
import web.fiiit.userservice.dto.user.UserRegister;
import web.fiiit.userservice.dto.user.UserUpdate;
import web.fiiit.userservice.exception.JwtAuthenticationException;
import web.fiiit.userservice.model.Token;
import web.fiiit.userservice.model.User;
import web.fiiit.userservice.security.JwtTokenProvider;
import web.fiiit.userservice.service.DataServiceClient;
import web.fiiit.userservice.service.TokenService;
import web.fiiit.userservice.service.UserService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "Users' operations")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final DataServiceClient dataServiceClient;

    @Autowired
    public UserController(
            UserService userService,
            JwtTokenProvider jwtTokenProvider,
            TokenService tokenService,
            DataServiceClient dataServiceClient) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenService = tokenService;
        this.dataServiceClient = dataServiceClient;
    }


    @PostMapping("/register")
    @Operation(
            summary = "Register",
            description = "Register user"
    )
    public ResponseEntity<User> register(
            @Valid @RequestBody UserRegister userRegister
    ) throws EntityExistsException {
        return new ResponseEntity<>(
                userService.create(userRegister),
                HttpStatus.CREATED
        );
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login user"
    )
    public ResponseEntity<UserLogin> login(
            @Validated({DtoConfiguration.OnRequest.class})
            @RequestBody
                    UserLogin userLogin
    ) throws JwtException, AuthenticationException, EntityExistsException {

        UserDetails user = userService.loadUserByUsername(userLogin.getLogin());

        Token token = jwtTokenProvider.createToken(
                userLogin.getLogin(),
                user.getAuthorities()
        );
        token.setOwner((User) user);

        dataServiceClient.add(tokenService.create(token));

        return ResponseEntity.ok(
                UserLogin.builder()
                        .login(userLogin.getLogin())
                        .token(token.getValue())
                        .build()
        );
    }
    // TODO: create token refreshing endpoint
//    @GetMapping("refresh")
//    @Operation(
//            summary = "Refresh",
//            description = "Refresh user's token"
//    )
//    public ResponseEntity<>

    @GetMapping("")
    @Operation(
            summary = "Retrieve user",
            description = "Retrieve information about authenticated user"
    )
    public ResponseEntity<User> retrieve(
            Authentication authentication
    ) throws JwtAuthenticationException {
        if (authentication == null) {
            throw new JwtAuthenticationException(
                    "Not authenticated!",
                    "Authorization"
            );
        }
        return ResponseEntity.ok(
                userService.findByUsername(authentication.getName())
        );
    }

    @PutMapping("")
    @Operation(
            summary = "Update user",
            description = "Update information about authenticated user"
    )
    public ResponseEntity<User> update(
            @Valid
            @RequestBody
                    UserUpdate userUpdate,
            Authentication authentication
    ) throws JwtAuthenticationException {
        if (authentication == null) {
            throw new JwtAuthenticationException("Not authenticated!", "Authorization");
        }

        User principal = userService.findByUsername(authentication.getName());

        return ResponseEntity.ok(
                userService.update(userUpdate, principal.getId())
        );
    }

    @DeleteMapping("")
    @Operation(
            summary = "Delete user",
            description = "Delete authenticated user"
    )
    public ResponseEntity<String> delete(
            Authentication authentication
    ) throws JwtAuthenticationException, EntityNotFoundException {
        if (authentication == null) {
            throw new JwtAuthenticationException("Not authenticated!", "Authorization");
        }
        tokenService.deleteAllUserTokens(authentication.getName());
        Long id = userService.delete(authentication.getName());
        dataServiceClient.delete(id, true);
        return new ResponseEntity<>(
                "User " + authentication.getName() + "is deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
