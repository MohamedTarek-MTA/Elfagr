package com.example.Elfagr.Inventory.Controller;

import com.example.Elfagr.Inventory.DTO.InventoryTransactionDTO;
import com.example.Elfagr.Inventory.Service.InventoryTransactionService;
import com.example.Elfagr.Shared.Service.PageableService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {
    private final InventoryTransactionService inventoryTransactionService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<InventoryTransactionDTO> getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(inventoryTransactionService.getTransactionById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/inventory/{id}")
    public ResponseEntity<Page<InventoryTransactionDTO>> getTransactionsByInventoryId(@PathVariable Long id,
                                                                                      @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                      @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                                                      @RequestParam(defaultValue = "asc") String direction
                                                                                      ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getTransactionsByInventoryId(id,pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/product/{id}")
    public ResponseEntity<Page<InventoryTransactionDTO>> getTransactionsByProductId(@PathVariable Long id,
                                                                                      @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                      @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                                                      @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getTransactionsByProductId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<InventoryTransactionDTO>> getTransactionsByEmployeeId(@PathVariable Long id,
                                                                                      @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                      @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                                                      @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getTransactionsMadeByEmployee(id,pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/in")
    public ResponseEntity<Page<InventoryTransactionDTO>> getInTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                           @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                           @RequestParam(defaultValue = "name") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getInTransactions(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/out")
    public ResponseEntity<Page<InventoryTransactionDTO>> getOutTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                           @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                           @RequestParam(defaultValue = "name") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getOutTransactions(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/sold")
    public ResponseEntity<Page<InventoryTransactionDTO>> getSoldTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                           @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                           @RequestParam(defaultValue = "name") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getSoldTransactions(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/damaged")
    public ResponseEntity<Page<InventoryTransactionDTO>> getDamagedTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                             @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                             @RequestParam(defaultValue = "name") String sortBy,
                                                                             @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getDamagedTransactions(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/returned")
    public ResponseEntity<Page<InventoryTransactionDTO>> getReturnedTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                @RequestParam(defaultValue = "name") String sortBy,
                                                                                @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getReturnedTransactions(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/restocked")
    public ResponseEntity<Page<InventoryTransactionDTO>> getRestockedTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                 @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                 @RequestParam(defaultValue = "name") String sortBy,
                                                                                 @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getRestockedTransactions(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<InventoryTransactionDTO>> getAllTransactions(@RequestParam(defaultValue = "0")@Min(0) int page,
                                                                                  @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                                  @RequestParam(defaultValue = "name") String sortBy,
                                                                                  @RequestParam(defaultValue = "asc") String direction
    ){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(inventoryTransactionService.getAllTransactions(pageable));
    }
}
