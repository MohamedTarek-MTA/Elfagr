package com.example.Elfagr.Product.Controller;

import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.Service.ProductService;
import com.example.Elfagr.Shared.Service.PageableService;
import com.example.Elfagr.User.Entity.User;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/category/{id}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategoryId(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productService.getProductsByCategoryId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}")
    public ResponseEntity<Page<ProductDTO>> getProductsByInventoryId(@PathVariable Long id,
                                                                    @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productService.getProductsByInventoryId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}/new-product/sku")
    public ResponseEntity<ProductDTO> getNewProductBySkuAndInventoryId(@PathVariable Long id,@RequestParam String sku){
        return ResponseEntity.ok(productService.getNewProductBySkuAndInventoryId(sku,id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}/imported-product/sku")
    public ResponseEntity<ProductDTO> getImportedProductBySkuAndInventoryId(@PathVariable Long id,@RequestParam String sku){
        return ResponseEntity.ok(productService.getImportedProductBySkuAndInventoryId(sku,id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}/new-product/barcode")
    public ResponseEntity<ProductDTO> getNewProductByBarcodeAndInventoryId(@PathVariable Long id,@RequestParam String barcode){
        return ResponseEntity.ok(productService.getNewProductByBarcodeAndInventoryId(barcode,id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}/imported-product/barcode")
    public ResponseEntity<ProductDTO> getImportedProductByBarcodeAndInventoryId(@PathVariable Long id,@RequestParam String barcode){
        return ResponseEntity.ok(productService.getImportedProductByBarcodeAndInventoryId(barcode,id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/product/name")
    public ResponseEntity<Page<ProductDTO>> getProductByName(@RequestParam String name,
                                                                     @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                     @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                     @RequestParam(defaultValue = "name") String sortBy,
                                                                     @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productService.getProductByName(name,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/category/name")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategoryName(@RequestParam String name,
                                                             @RequestParam(defaultValue = "0")@Min(0) int page,
                                                             @RequestParam(defaultValue = "10")@Min(1) int size,
                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                             @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productService.getProductsByCategoryName(name,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
                                                                      @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                      @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                                      @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/product/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDTO> changeProductCategory(@PathVariable Long productId,@PathVariable Long categoryId){
        return ResponseEntity.ok(productService.changeProductCategory(categoryId,productId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/available/product/{id}")
    public ResponseEntity<ProductDTO> markProductAsAvailable(@PathVariable Long id){
        return ResponseEntity.ok(productService.markProductAsAvailable(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/in-available/product/{id}")
    public ResponseEntity<ProductDTO> markProductAsInAvailable(@PathVariable Long id){
        return ResponseEntity.ok(productService.markProductAsInAvailable(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/new/product/{id}")
    public ResponseEntity<ProductDTO> markProductAsNew(@PathVariable Long id){
        return ResponseEntity.ok(productService.markProductAsNew(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/imported/product/{id}")
    public ResponseEntity<ProductDTO> markProductAsImported(@PathVariable Long id){
        return ResponseEntity.ok(productService.markProductAsImported(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<ProductDTO> markProductAsDeleted(@PathVariable Long id){
        return ResponseEntity.ok(productService.markProductAsDeleted(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long id,
                                                        @RequestPart(required = false) ProductDTO dto,
                                                        @RequestPart(required = false) MultipartFile image){
        return ResponseEntity.ok(productService.updateProduct(id,dto,image));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@AuthenticationPrincipal User userDetails,
                                                    @RequestPart(required = false) ProductDTO dto,
                                                    @RequestPart(required = false) MultipartFile image){
        Long userId = userDetails.getId();
        return ResponseEntity.ok(productService.createProduct(userId,dto,image));
    }
}
