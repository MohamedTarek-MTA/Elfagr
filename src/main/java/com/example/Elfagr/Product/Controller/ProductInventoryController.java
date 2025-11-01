package com.example.Elfagr.Product.Controller;

import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Service.ProductInventoryService;
import com.example.Elfagr.Security.Service.CustomUserDetails;
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

@RestController
@RequestMapping("/api/product-inventories")
@RequiredArgsConstructor
public class ProductInventoryController {
    private final ProductInventoryService productInventoryService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/product/{productId}/from/{oldInventoryId}/to/{newInventoryId}/reason")
    public ResponseEntity<ProductInventoryDTO> changeProductInventory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable Long productId,
                                                                      @PathVariable Long oldInventoryId,
                                                                      @PathVariable Long newInventoryId,
                                                                      @RequestParam(defaultValue = "OTHER") TransactionReason reason){
        return ResponseEntity.ok(productInventoryService.changeProductInventory(userDetails.getId(),productId,oldInventoryId,newInventoryId,reason));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/product/{productId}/inventory/{inventoryId}/quantity/type/reason")
    public ResponseEntity<ProductInventoryDTO> changeProductInventoryStockQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PathVariable Long productId,
                                                                                   @PathVariable Long inventoryId,
                                                                                   @RequestParam Integer quantity,
                                                                                   @RequestParam TransactionType type,
                                                                                   @RequestParam(defaultValue = "OTHER") TransactionReason reason){
        return ResponseEntity.ok(productInventoryService.changeProductStockQuantity(userDetails.getId(),productId,inventoryId,quantity,type,reason));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}")
    public ResponseEntity<Page<ProductInventoryDTO>> getProductsByInventoryId(@PathVariable Long id,
                                                                     @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                     @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                     @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productInventoryService.getProductsByInventoryId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<ProductInventoryDTO>> getInventoriesWithProducts(
                                                                              @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                              @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                              @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productInventoryService.getAllProductInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/available")
    public ResponseEntity<Page<ProductInventoryDTO>> findAvailableProductInventory( @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productInventoryService.findAvailableProductInventory(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/un-available")
    public ResponseEntity<Page<ProductInventoryDTO>> findUnAvailableProductInventory( @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(productInventoryService.findUnAvailableProductInventory(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/un-available/product-inventory/{id}")
    public ResponseEntity<ProductInventoryDTO> markAsUnavailable(Long id){
        return ResponseEntity.ok(productInventoryService.markAsUnAvailable(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/available/product-inventory/{id}")
    public ResponseEntity<ProductInventoryDTO> markAsAvailable(Long id){
        return ResponseEntity.ok(productInventoryService.markAsAvailable(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/product/{id}")
    public ResponseEntity<Page<ProductInventoryDTO>> getInventoriesByProductId(@PathVariable Long id,
                                                                               @RequestParam(defaultValue = "0")@Min(0)int page,
                                                                               @RequestParam(defaultValue = "10")@Min(1)int size,
                                                                               @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                               @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);

        return ResponseEntity.ok(productInventoryService.getInventoriesByProductId(id,pageable));
    }
}
