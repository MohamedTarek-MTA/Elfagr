package com.example.Elfagr.User.Controller;

import com.example.Elfagr.Shared.Service.PageableService;
import com.example.Elfagr.User.DTO.UserDTO;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.Mapper.UserMapper;
import com.example.Elfagr.User.Service.UserService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PutMapping("user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestPart(value = "user",required = false) UserDTO dto, @RequestPart(value = "image",required = false) MultipartFile image, @AuthenticationPrincipal User userDetails){
        var isAccessible = userService.checkAccess(id, userDetails);

        if(isAccessible == null)
            return ResponseEntity.ok(userService.updateUser(id,dto,image));

        return isAccessible;
    }

    @PatchMapping("/user/delete-account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id,@AuthenticationPrincipal User userDetails){
        var isAccessible = userService.checkAccess(id, userDetails);

        if(isAccessible == null)
            return ResponseEntity.ok(userService.deleteUser(id));

        return isAccessible;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/active-account/{id}")
    public ResponseEntity<UserDTO> activeAccount(@PathVariable Long id){
        return ResponseEntity.ok(userService.activeUser(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/inactive-account/{id}")
    public ResponseEntity<UserDTO> inActiveAccount(@PathVariable Long id){
        return ResponseEntity.ok(userService.inActiveUser(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/ban-account/{id}")
    public ResponseEntity<UserDTO> banAccount(@PathVariable Long id){
        return ResponseEntity.ok(userService.banUser(id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }
    @GetMapping("/user/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email){
        var user = userService.getUserByEmail(email);

            return ResponseEntity.ok(UserMapper.toDTO(user));

    }
    @GetMapping("/user/phone")
    public ResponseEntity<?> getUserByPhone(@RequestParam String phone){
        var user = userService.getUserByPhone(phone);

        return ResponseEntity.ok(UserMapper.toDTO(user)); }
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam(defaultValue = "0") @Min(0) int page ,
                                                     @RequestParam(defaultValue = "10") @Min(1) int size ,
                                                     @RequestParam(defaultValue = "name") String sortBy ,
                                                     @RequestParam(defaultValue = "asc") String direction)
    {
       Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);

        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/user/to-admin/{id}")
    public ResponseEntity<UserDTO> userToAdmin(@PathVariable Long id){
        return ResponseEntity.ok(userService.toAdmin(id));
    }
    @GetMapping("/search/name")
    public ResponseEntity<Page<UserDTO>> searchByName(@RequestParam String name,
                                                      @RequestParam(defaultValue = "0") @Min(0) int page,
                                                      @RequestParam(defaultValue = "10") @Min(1) int size,
                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String direction)
    {
        Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);

        return ResponseEntity.ok(userService.searchByName(name,pageable));
    }

}
