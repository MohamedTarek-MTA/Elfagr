package com.example.Elfagr.Order.Controller;

import com.example.Elfagr.Order.DTO.OrderDTO;
import com.example.Elfagr.Order.Service.OrderService;
import com.example.Elfagr.Shared.Service.PageableService;
import com.example.Elfagr.User.Entity.User;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/customerName")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCustomerName(@RequestParam String customerName,
                                                                  @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                  @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                  @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                  @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getOrdersByCustomerName(customerName,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/customerPhone")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCustomerPhone(@RequestParam String customerPhone,
                                                                  @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                  @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                  @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                  @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getOrdersByCustomerPhone(customerPhone,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/date")
    public ResponseEntity<Page<OrderDTO>> getOrdersByCreationDate(@RequestParam LocalDate date,
                                                                   @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                   @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                   @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                   @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getOrdersByCreationDate(date,pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/employee/{id}")
    public ResponseEntity<Page<OrderDTO>> getOrdersByEmployeeId(@RequestParam Long id,
                                                                  @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                  @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                  @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                  @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getOrdersByEmployeeId(id,pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/pending")
    public ResponseEntity<Page<OrderDTO>> getPendingOrders(
                                                                @RequestParam(defaultValue = "0")@Min(0) int page,
                                                                @RequestParam(defaultValue = "10")@Min(1) int size,
                                                                @RequestParam(defaultValue = "createdAt")String sortBy,
                                                                @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getPendingOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/processed")
    public ResponseEntity<Page<OrderDTO>> getProcessedOrders(
                                    @RequestParam(defaultValue = "0")@Min(0) int page,
                                    @RequestParam(defaultValue = "10")@Min(1) int size,
                                    @RequestParam(defaultValue = "createdAt")String sortBy,
                                    @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getProcessedOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/shipped")
    public ResponseEntity<Page<OrderDTO>> getShippedOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getShippedOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/canceled")
    public ResponseEntity<Page<OrderDTO>> getCanceledOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getCanceledOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/delivered")
    public ResponseEntity<Page<OrderDTO>> getDeliveredOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getDeliveredOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/cash")
    public ResponseEntity<Page<OrderDTO>> getCashPayedOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getCashPayed(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/e-wallet")
    public ResponseEntity<Page<OrderDTO>> getE_WalletPayedOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getEWalletPayed(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/credit-card")
    public ResponseEntity<Page<OrderDTO>> getCreditCardPayedOrders(
            @RequestParam(defaultValue = "0")@Min(0) int page,
            @RequestParam(defaultValue = "10")@Min(1) int size,
            @RequestParam(defaultValue = "createdAt")String sortBy,
            @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getCreditCardPayed(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<Page<OrderDTO>> getAllOrders( @RequestParam(defaultValue = "0")@Min(0) int page,
                                                        @RequestParam(defaultValue = "10")@Min(1) int size,
                                                        @RequestParam(defaultValue = "createdAt") String sortBy,
                                                        @RequestParam(defaultValue = "asc")String direction){
        Pageable pageable = PageableService.pageHandler(page,size,sortBy,direction);
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/pending/order/{id}")
    public ResponseEntity<OrderDTO> setOrderAsPending(@PathVariable Long id){
        return ResponseEntity.ok(orderService.setOrderAsPending(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/processed/order/{id}")
    public ResponseEntity<OrderDTO> setOrderAsProcessing(@PathVariable Long id){
        return ResponseEntity.ok(orderService.setOrderAsProcessing(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/shipped/order/{id}")
    public ResponseEntity<OrderDTO> setOrderAsShipping(@PathVariable Long id){
        return ResponseEntity.ok(orderService.setOrderAsShipping(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/delivered/order/{id}")
    public ResponseEntity<OrderDTO> setOrderAsDelivering(@PathVariable Long id){
        return ResponseEntity.ok(orderService.setOrderAsDelivering(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PatchMapping("/canceled/order/{id}")
    public ResponseEntity<OrderDTO> setOrderAsCanceled(@AuthenticationPrincipal User userDetails, @PathVariable Long id){
        return ResponseEntity.ok(orderService.setOrderAsCanceled(userDetails.getId(),id));
    }
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/order")
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal User userDetails,@RequestBody OrderDTO dto){
        return ResponseEntity.ok(orderService.createOrder(userDetails.getId(),dto));
    }

}
