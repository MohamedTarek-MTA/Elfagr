package com.example.Elfagr.Return.Controller;

import com.example.Elfagr.Return.DTO.ReturnDTO;
import com.example.Elfagr.Return.Service.ReturnService;
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

import java.time.LocalDate;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnController {
    private final ReturnService returnService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/return/{id}")
    public ResponseEntity<ReturnDTO> getReturnById(@PathVariable Long id){
        return ResponseEntity.ok(returnService.getReturnById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/order/{id}")
    public ResponseEntity<Page<ReturnDTO>> getReturnsByOrderId(@PathVariable Long id,
                                                              @RequestParam(defaultValue = "0")@Min(0) int page,
                                                              @RequestParam(defaultValue = "10")@Min(1) int size,
                                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getByOrderId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/employee/{id}")
    public ResponseEntity<Page<ReturnDTO>> getReturnsByEmployeeId(@PathVariable Long id,
                                                              @RequestParam(defaultValue = "0")@Min(0) int page,
                                                              @RequestParam(defaultValue = "10")@Min(1) int size,
                                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getByEmployeeId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/customerName")
    public ResponseEntity<Page<ReturnDTO>> getReturnsByCustomerName(@RequestParam String customerName,
                                                                 @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                 @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                 @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                 @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getByCustomerName(customerName,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/customerPhone")
    public ResponseEntity<Page<ReturnDTO>> getReturnsByCustomerPhone(@RequestParam String customerPhone,
                                                                   @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                   @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                   @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getByCustomerPhone(customerPhone,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/date")
    public ResponseEntity<Page<ReturnDTO>> getReturnsByCreationDate(@RequestParam LocalDate date,
                                                                    @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getByCreationDate(date,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/damaged")
    public ResponseEntity<Page<ReturnDTO>> getDamagedReturns(
                                                                    @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                    @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getDamagedReturns(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/customer-request")
    public ResponseEntity<Page<ReturnDTO>> getCustomerRequestReturns(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getCustomerRequestReturns(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/wrong-item")
    public ResponseEntity<Page<ReturnDTO>> getWrongItemReturns(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getWrongItemReturns(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/expired")
    public ResponseEntity<Page<ReturnDTO>> getExpiredReturns(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getExpiredReturns(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<ReturnDTO>> getAllReturns(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(returnService.getAllReturns(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/return/order/{id}")
    public ResponseEntity<ReturnDTO> createReturn(@AuthenticationPrincipal User userDetails,
                                                  @PathVariable Long id,
                                                  @RequestBody ReturnDTO dto){
        return ResponseEntity.ok(returnService.createReturn(userDetails.getId(), id,dto));
    }
}
