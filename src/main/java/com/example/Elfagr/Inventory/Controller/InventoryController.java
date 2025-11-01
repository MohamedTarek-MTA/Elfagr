package com.example.Elfagr.Inventory.Controller;

import com.example.Elfagr.Inventory.DTO.InventoryDTO;
import com.example.Elfagr.Inventory.Service.InventoryService;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Shared.Service.PageableService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("inventory/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<InventoryDTO>> getAllInventories(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page, size, sortBy, direction);

        return ResponseEntity.ok(inventoryService.getAllInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/name")
    public ResponseEntity<Page<InventoryDTO>> getInventoryByName(@RequestParam String name,
                                                           @RequestParam(defaultValue = "0") @Min(0) int page,
                                                           @RequestParam(defaultValue = "10") @Min(1) int size,
                                                           @RequestParam(defaultValue = "name") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String direction)
    {
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryService.getByName(name,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/storage")
    public ResponseEntity<Page<InventoryDTO>> getStorageInventories(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                    @RequestParam(defaultValue = "10") @Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryService.getStorageInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/maintenance")
    public ResponseEntity<Page<InventoryDTO>> getMaintenanceInventories(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                    @RequestParam(defaultValue = "10") @Min(1) int size,
                                                                    @RequestParam(defaultValue = "name") String sortBy,
                                                                    @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryService.getMaintenanceInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/active")
    public ResponseEntity<Page<InventoryDTO>> getActiveInventories(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                   @RequestParam(defaultValue = "10") @Min(1) int size,
                                                                   @RequestParam(defaultValue = "name") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryService.getActiveInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/in-active")
    public ResponseEntity<Page<InventoryDTO>> getInActiveInventories(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                                   @RequestParam(defaultValue = "10") @Min(1) int size,
                                                                   @RequestParam(defaultValue = "name") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryService.getInActiveInventories(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/active/inventory/{id}")
    public ResponseEntity<InventoryDTO> activeInventoryById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.activeInventory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/in-active/inventory/{id}")
    public ResponseEntity<InventoryDTO> inActiveInventoryById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.inActiveInventory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<InventoryDTO> deleteInventoryById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/inventory")
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO dto){
        return ResponseEntity.ok(inventoryService.createInventory(dto));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PutMapping("/inventory/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id,@RequestBody InventoryDTO dto){
        return ResponseEntity.ok(inventoryService.updateInventory(id,dto));
    }
}
