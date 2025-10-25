package com.example.Elfagr.Product.Controller;

import com.example.Elfagr.Product.DTO.CategoryDTO;
import com.example.Elfagr.Product.Service.CategoryService;
import com.example.Elfagr.Shared.Service.PageableService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/available")
    public ResponseEntity<Page<CategoryDTO>> getAvailableCategories(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(categoryService.getAvailableCategories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/not-available")
    public ResponseEntity<Page<CategoryDTO>> getNotAvailableCategories(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(categoryService.getNotAvailableCategories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/category/name")
    public ResponseEntity<Page<CategoryDTO>> getCategoryByName(@RequestParam String name,
                                                               @RequestParam(defaultValue = "0")@Min(0) int page,
                                                               @RequestParam(defaultValue = "10")@Min(1) int size,
                                                               @RequestParam(defaultValue = "name") String sortBy,
                                                               @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);
        return ResponseEntity.ok(categoryService.getByName(name,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(
                                                               @RequestParam(defaultValue = "0")@Min(0) int page,
                                                               @RequestParam(defaultValue = "10")@Min(1) int size,
                                                               @RequestParam(defaultValue = "name") String sortBy,
                                                               @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);
        return ResponseEntity.ok(categoryService.getAllCategories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/available/category/{id}")
    public ResponseEntity<CategoryDTO> setCategoryAsAvailable(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.setAsAvailableCategory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/not-available/category/{id}")
    public ResponseEntity<CategoryDTO> setCategoryAsNotAvailable(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.setAsNotAvailableCategory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,@RequestBody CategoryDTO dto){
        return ResponseEntity.ok(categoryService.updateCategory(id,dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto){
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }
}
