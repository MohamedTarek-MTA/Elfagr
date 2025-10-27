package com.example.Elfagr.Product.Controller;

import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Service.ProductInventoryService;
import com.example.Elfagr.User.Entity.User;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ProductInventoryDTO> changeProductInventory(@AuthenticationPrincipal User userDetails,
                                                                      @PathVariable Long productId,
                                                                      @PathVariable Long oldInventoryId,
                                                                      @PathVariable Long newInventoryId,
                                                                      @RequestParam(defaultValue = "OTHER") TransactionReason reason){
        return ResponseEntity.ok(productInventoryService.changeProductInventory(userDetails.getId(),productId,oldInventoryId,newInventoryId,reason));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/product/{productId}/inventory/{inventoryId}/quantity/type/reason")
    public ResponseEntity<ProductInventoryDTO> changeProductInventoryStockQuantity(@AuthenticationPrincipal User userDetails,
                                                                                   @PathVariable Long productId,
                                                                                   @PathVariable Long inventoryId,
                                                                                   @RequestParam Integer quantity,
                                                                                   @RequestParam TransactionType type,
                                                                                   @RequestParam(defaultValue = "OTHER") TransactionReason reason){
        return ResponseEntity.ok(productInventoryService.changeProductStockQuantity(userDetails.getId(),productId,inventoryId,quantity,type,reason));
    }

}
