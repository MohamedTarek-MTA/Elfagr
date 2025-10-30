package com.example.Elfagr.User.Service;


import com.example.Elfagr.Shared.Service.UploadImageService;
import com.example.Elfagr.User.DTO.UserDTO;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.Enum.Role;
import com.example.Elfagr.User.Enum.Status;
import com.example.Elfagr.User.Mapper.UserMapper;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UploadImageService uploadImageService;

    @Cacheable(value = "usersByEmail", key = "#email")
    public UserDTO getUserByEmail(String email) {
        return UserMapper.toDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!")));
    }

    @Cacheable(value = "usersByPhone", key = "#phone")
    public UserDTO getUserByPhone(String phone) {
        return UserMapper.toDTO(userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!")));
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }
    @Async
    public void updateLastLoginDate(Long id){
         userRepository.findById(id).ifPresent(user -> {
             user.setLastLoginDate(LocalDateTime.now());
             userRepository.save(user);
         });
    }
    @Transactional
    @CachePut(value = "usersById", key = "#id")
    public UserDTO updateUserStatus(Long id, Status status, Boolean isDeleted, Boolean isEnabled) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        user.setStatus(status);
        user.setIsDeleted(isDeleted != null ? isDeleted : user.getIsDeleted());
        user.setIsEnabled(isEnabled != null ? isEnabled : user.getIsEnabled());

        if (Boolean.TRUE.equals(isDeleted)) {
            user.setDeletedAt(LocalDateTime.now());
        }

        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    public UserDTO activeUser(Long id) {
        return updateUserStatus(id, Status.ACTIVE, false, true);
    }

    public UserDTO inActiveUser(Long id) {
        return updateUserStatus(id, Status.INACTIVE, false, false);
    }

    public UserDTO banUser(Long id) {
        return updateUserStatus(id, Status.BANNED, false, false);
    }

    @CacheEvict(value = "usersById",key = "#id")
    public UserDTO deleteUser(Long id) {
        return updateUserStatus(id, Status.INACTIVE, true, false);
    }
    @Transactional
    @CachePut(value = "usersById", key = "#id")
    public UserDTO updateUser(Long id, UserDTO userDto, MultipartFile image) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));

        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(userDto.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(userDto.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(userDto.getBirthdate()).ifPresent(user::setBirthdate);

        if (image != null && !image.isEmpty()) {
            String url = uploadImageService.uploadMultipartFile(image);
            user.setImageUrl(url);
        }
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }
    @Cacheable(value = "usersById",key = "#id")
    public UserDTO getById(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));
        return UserMapper.toDTO(user);
    }
    public Page<UserDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }
    public Page<UserDTO> searchByName(String name , Pageable pageable){
        var users = userRepository.findByNameContainingIgnoreCase(name,pageable);
        return users.map(UserMapper::toDTO);
    }
    public UserDTO changeRole(Long id , Role role){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found!"));
         user.setRole(role);
         userRepository.save(user);
         return UserMapper.toDTO(user);
    }
    public UserDTO toAdmin(Long id){
        return changeRole(id , Role.ADMIN);
    }
    public ResponseEntity<?> checkAccess(Long id, User userDetails) {
        if (!id.equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied !");
        }
        return null;
    }

}
