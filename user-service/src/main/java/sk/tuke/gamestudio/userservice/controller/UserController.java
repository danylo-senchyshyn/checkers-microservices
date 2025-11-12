package sk.tuke.gamestudio.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.userservice.entity.User;
import sk.tuke.gamestudio.userservice.service.UserException;
import sk.tuke.gamestudio.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    /**
     * Create or update a player
     */
    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody User request) {
        try {
            User player = userService.createOrUpdateUser(
                    request.getNickname(),
                    request.getAvatarUrl()
            );
            return ResponseEntity.ok(player);
        } catch (UserException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get player by nickname
     */
    @GetMapping("/{nickname}")
    public ResponseEntity<User> getUser(@PathVariable String nickname) {
        try {
            User player = userService.getUser(nickname);
            return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
        } catch (UserException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all players
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (UserException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Reset player table
     */
    @DeleteMapping
    public ResponseEntity<Void> resetUsers() {
        try {
            userService.reset();
            return ResponseEntity.noContent().build();
        } catch (UserException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}