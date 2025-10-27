package com.example.Elfagr.Return.Service;

import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Inventory.Mapper.InventoryTransactionMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Inventory.Service.InventoryTransactionService;
import com.example.Elfagr.Order.Repository.OrderRepository;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.Return.DTO.ReturnDTO;
import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.Return.Entity.ReturnItem;
import com.example.Elfagr.Return.Enum.ReturnReason;
import com.example.Elfagr.Return.Mapper.ReturnMapper;
import com.example.Elfagr.Return.Repository.ReturnItemRepository;
import com.example.Elfagr.Return.Repository.ReturnRepository;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReturnService {
    private final ReturnRepository returnRepository;
    private final ReturnItemRepository returnItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final InventoryTransactionService inventoryTransactionService;
    private final ProductInventoryRepository productInventoryRepository;
    @Transactional
    public ReturnDTO createReturn(Long employeeId,Long orderId,ReturnDTO dto){
        var user = userRepository.findById(employeeId).orElseThrow(()->new IllegalArgumentException("Sorry This Employee Not Found !"));
        var order = orderRepository.findById(orderId).orElseThrow(()->new IllegalArgumentException("Sorry Order Is Not Found !"));
        if(returnRepository.existsByOrder_Id(orderId))
            throw new IllegalArgumentException("This Order Already Returned !");
        Return aReturn = Return.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .notes(dto.getNotes())
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .customerInfo(dto.getCustomerInfo())
                .notes(dto.getNotes())
                .reason(dto.getReason())
                .returnItems(new ArrayList<>())
                .build();
        BigDecimal total = BigDecimal.ZERO;

        for(var returnItemDto : dto.getReturnItems()){
            var product = returnItemDto.getProduct();
            var inventory = returnItemDto.getInventory();
            var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventory.getId()).orElseThrow(()->new IllegalArgumentException("Sorry Product Is Not Found At This Inventory !"));
            if(product.getIsAvailable() || !inventory.getStatus().equals(Status.ACTIVE))
                throw new IllegalArgumentException("Sorry Product Or Inventory Is Not Available !!");
            BigDecimal subTotal = product.getPrice().multiply(BigDecimal.valueOf(returnItemDto.getQuantity()));

            ReturnItem returnItem = ReturnItem.builder()
                    .quantity(returnItemDto.getQuantity())
                    .subTotal(subTotal)
                    .inventory(inventory)
                    .product(product)
                    .aReturn(aReturn)
                    .build();
            aReturn.getReturnItems().add(returnItem);
            total = total.add(subTotal);

            productInventory.setQuantity(productInventory.getQuantity()+returnItemDto.getQuantity());
            productInventory.setUpdatedAt(LocalDateTime.now());
            productInventoryRepository.save(productInventory);

            inventoryTransactionService.createInventoryTransaction(employeeId, InventoryTransactionMapper.toDTO(
                    InventoryTransaction.builder()
                            .createdAt(LocalDateTime.now())
                            .user(user)
                            .quantityChange(returnItem.getQuantity())
                            .type(TransactionType.IN)
                            .reason(TransactionReason.RETURNED)
                            .inventory(inventory)
                            .product(product)
                            .build()
            ));

        }
        aReturn.setTotalAmount(total);
        returnRepository.save(aReturn);
        returnItemRepository.saveAll(aReturn.getReturnItems());
        return ReturnMapper.toDTO(aReturn);
    }
    @Cacheable(value = "returnsById",key = "#returnId")
    public ReturnDTO getReturnById(Long returnId){
        return ReturnMapper.toDTO(returnRepository.findById(returnId).orElseThrow(()->new IllegalArgumentException("Return Not Found !")));
    }
    public Page<ReturnDTO> getByOrderId(Long orderId, Pageable pageable){
        return returnRepository.findByOrder_Id(orderId,pageable).map(ReturnMapper::toDTO);
    }
    public Page<ReturnDTO> getByEmployeeId(Long employeeId,Pageable pageable){
        return returnRepository.findByUser_Id(employeeId,pageable).map(ReturnMapper::toDTO);
    }
    public Page<ReturnDTO> getByCustomerName(String customerName,Pageable pageable){
        return returnRepository.findByCustomerNameContainingIgnoreCase(customerName,pageable).map(ReturnMapper::toDTO);
    }
    public Page<ReturnDTO> getByCustomerPhone(String phone,Pageable pageable){
        return returnRepository.findByCustomerPhone(phone,pageable).map(ReturnMapper::toDTO);
    }
    public Page<ReturnDTO> getByCreationDate(LocalDate creationDate,Pageable pageable){
        var startOfDay = creationDate.atStartOfDay();
        var endOfDay = creationDate.atTime(LocalTime.MAX);
        return returnRepository.findByCreatedAtBetween(startOfDay,endOfDay,pageable).map(ReturnMapper::toDTO);
    }
    private Page<ReturnDTO> getReturnByReason(ReturnReason reason,Pageable pageable){
        return returnRepository.findByReason(reason,pageable).map(ReturnMapper::toDTO);
    }
    public Page<ReturnDTO> getDamagedReturns(Pageable pageable){
        return getReturnByReason(ReturnReason.DAMAGED,pageable);
    }
    public Page<ReturnDTO> getWrongItemReturns(Pageable pageable){
        return getReturnByReason(ReturnReason.WRONG_ITEM,pageable);
    }
    public Page<ReturnDTO> getCustomerRequestReturns(Pageable pageable){
        return getReturnByReason(ReturnReason.CUSTOMER_REQUEST,pageable);
    }
    public Page<ReturnDTO> getExpiredReturns(Pageable pageable){
        return getReturnByReason(ReturnReason.EXPIRED,pageable);
    }
    public Page<ReturnDTO> getAllReturns(Pageable pageable){
        return returnRepository.findAll(pageable).map(ReturnMapper::toDTO);
    }
}
