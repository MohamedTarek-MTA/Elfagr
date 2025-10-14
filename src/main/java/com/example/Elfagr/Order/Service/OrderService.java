package com.example.Elfagr.Order.Service;

import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Inventory.Mapper.InventoryTransactionMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Inventory.Service.InventoryTransactionService;
import com.example.Elfagr.Order.DTO.OrderDTO;
import com.example.Elfagr.Order.DTO.OrderItemDTO;
import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.Order.Entity.OrderItem;
import com.example.Elfagr.Order.Enum.OrderStatus;
import com.example.Elfagr.Order.Mapper.OrderItemMapper;
import com.example.Elfagr.Order.Mapper.OrderMapper;
import com.example.Elfagr.Order.Repository.OrderItemRepository;
import com.example.Elfagr.Order.Repository.OrderRepository;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.Return.Enum.ReturnReason;
import com.example.Elfagr.Return.Mapper.ReturnItemMapper;
import com.example.Elfagr.Return.Repository.ReturnItemRepository;
import com.example.Elfagr.Return.Repository.ReturnRepository;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ReturnRepository returnRepository;
    private final UserRepository userRepository;
    private final InventoryTransactionService inventoryTransactionService;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ReturnItemRepository returnItemRepository;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        var user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Sorry, this employee not found!"));

        Order order = Order.builder()
                .user(user)
                .customerName(orderDTO.getCustomerName())
                .customerPhone(orderDTO.getCustomerPhone())
                .customerInfo(orderDTO.getCustomerInfo())
                .orderStatus(OrderStatus.PENDING)
                .paymentMethod(orderDTO.getPaymentMethod())
                .notes(orderDTO.getNotes())
                .createdAt(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            var product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found!"));
            var inventory = inventoryRepository.findById(itemDTO.getInventoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found!"));
            var productInventory = productInventoryRepository
                    .findByProductIdAndInventoryId(product.getId(), inventory.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found in this inventory!"));

            if (!product.getIsAvailable() || !inventory.getStatus().equals(Status.ACTIVE))
                throw new IllegalArgumentException("Product or inventory not available!");

            if (itemDTO.getQuantity() > productInventory.getQuantity())
                throw new IllegalArgumentException("Not enough stock!");

            BigDecimal subTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .inventory(inventory)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .subTotal(subTotal)
                    .createdAt(LocalDateTime.now())
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);
            total = total.add(subTotal);

            // Update stock
            productInventory.setQuantity(productInventory.getQuantity() - itemDTO.getQuantity());
            productInventory.setUpdatedAt(LocalDateTime.now());
            productInventoryRepository.save(productInventory);

            // Log transaction
            inventoryTransactionService.createInventoryTransaction(
                    user.getId(),
                    InventoryTransactionMapper.toDTO(
                            InventoryTransaction.builder()
                                    .user(user)
                                    .product(product)
                                    .inventory(inventory)
                                    .quantityChange(itemDTO.getQuantity())
                                    .type(TransactionType.OUT)
                                    .reason(TransactionReason.SOLD)
                                    .build()
                    )
            );
        }

        BigDecimal taxAmount = orderDTO.getTaxAmount() != null ? orderDTO.getTaxAmount() : BigDecimal.ZERO;
        BigDecimal discountAmount = orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO;

        BigDecimal finalTotal = total.subtract(discountAmount).add(taxAmount);

        order.setTaxAmount(taxAmount);
        order.setDiscountAmount(discountAmount);
        order.setTotalPrice(finalTotal);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());
        return OrderMapper.toDTO(order);
    }
    @Cacheable(value = "ordersById",key = "#orderId")
    public OrderDTO getOrderById(Long orderId){
        return OrderMapper.toDTO(orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("Order Not Found !")));
    }
    public Page<OrderDTO> getOrdersByCustomerName(String customerName, Pageable pageable){
        var orders = orderRepository.findByCustomerNameIgnoreCase(customerName,pageable);
        return orders.map(OrderMapper::toDTO);
    }

    public Page<OrderDTO> getOrdersByCustomerPhone(String customerPhone,Pageable pageable){
        var orders = orderRepository.findByCustomerPhone(customerPhone,pageable);
        return orders.map(OrderMapper::toDTO);
    }

    public Page<OrderDTO> getOrdersByCreationDate(LocalDate creationDate, Pageable pageable){
        var startOfDay = creationDate.atStartOfDay();
        var endOfDay = creationDate.atTime(LocalTime.MAX);
        var orders = orderRepository.findByCreatedAtBetween(startOfDay,endOfDay,pageable);
        return orders.map(OrderMapper::toDTO);
    }

    public Page<OrderDTO> getOrdersByEmployeeId(Long employeeId,Pageable pageable){
        var orders = orderRepository.findByUser_Id(employeeId,pageable);
        return orders.map(OrderMapper::toDTO);
    }
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status,Pageable pageable){
        var orders = orderRepository.findByOrderStatus(status,pageable);
        return orders.map(OrderMapper::toDTO);
    }
    public Page<OrderDTO> getPendingOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.PENDING,pageable);
    }
    public Page<OrderDTO> getProcessedOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.PROCESSING,pageable);
    }
    public Page<OrderDTO> getShippedOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.SHIPPED,pageable);
    }
    public Page<OrderDTO> getCanceledOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.CANCELED,pageable);
    }
    public Page<OrderDTO> getDeliveredOrders(Pageable pageable){
        return getOrdersByStatus(OrderStatus.DELIVERED,pageable);
    }

    public OrderDTO changeOrderStatusById(Long orderId,OrderStatus status){
        var order = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("Order Not Found !"));
        order.setOrderStatus(status);
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }
    @CachePut(value = "ordersById",key = "#orderId")
    public OrderDTO setOrderAsDelivered(Long orderId){
        return changeOrderStatusById(orderId,OrderStatus.DELIVERED);
    }
    @CachePut(value = "ordersById",key = "#orderId")
    public OrderDTO setOrderAsProcessing(Long orderId){
        return changeOrderStatusById(orderId,OrderStatus.PROCESSING);
    }
    @CachePut(value = "ordersById",key = "#orderId")
    public OrderDTO setOrderAsShipped(Long orderId){
        return changeOrderStatusById(orderId,OrderStatus.SHIPPED);
    }
    @CachePut(value = "ordersById",key = "#orderId")
    public OrderDTO setOrderAsPending(Long orderId){
        return changeOrderStatusById(orderId,OrderStatus.PENDING);
    }
    @Transactional
    @CachePut(value = "ordersById",key = "#orderId")
    public OrderDTO setOrderAsCanceled(Long orderId,Long employeeId){
        var order = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("Order Not Found !"));
        if(!order.getOrderStatus().equals(OrderStatus.PENDING) && !order.getOrderStatus().equals(OrderStatus.PROCESSING))
            throw new IllegalArgumentException("Sorry We Could Not Cancel This Order Because It's Already "+order.getOrderStatus().name()+" !");
        var user = userRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("Sorry This Employee Not Found !"));
        if(returnRepository.existsByOrder_Id(orderId))
            throw new IllegalArgumentException("This Order Already Canceled !");
        Return aReturn =  Return.builder()
                .order(order)
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerInfo(order.getCustomerInfo())
                .reason(ReturnReason.CUSTOMER_REQUEST)
                .returnItems(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .totalAmount(order.getTotalPrice())
                .user(user)
                .build();

        for(var orderItem:order.getOrderItems()){
            var product = orderItem.getProduct();
            var inventory = orderItem.getInventory();
            var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventory.getId()).orElseThrow(()->new IllegalArgumentException("This Product Not Stored In This Inventory ! "));
            productInventory.setQuantity(orderItem.getQuantity()+productInventory.getQuantity());
            productInventory.setUpdatedAt(LocalDateTime.now());
            productInventoryRepository.save(productInventory);
            inventoryTransactionService.createInventoryTransaction(
                    user.getId(),InventoryTransactionMapper.toDTO(
                            InventoryTransaction.builder()
                                    .inventory(inventory)
                                    .product(product)
                                    .reason(TransactionReason.RETURNED)
                                    .type(TransactionType.IN)
                                    .quantityChange(orderItem.getQuantity())
                                    .user(user)
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    )
            );
        }
    var returnItems = order.getOrderItems().stream().map(orderItem -> ReturnItemMapper.toReturnItem(orderItem,aReturn)).toList();
        aReturn.setReturnItems(returnItems);
        returnRepository.save(aReturn);
        returnItemRepository.saveAll(returnItems);
        order.setOrderStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }
}
