package com.example.Elfagr.Inventory.Service;

import com.example.Elfagr.Inventory.DTO.InventoryDTO;
import com.example.Elfagr.Inventory.Entity.Inventory;
import com.example.Elfagr.Inventory.Enum.InventoryType;
import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Inventory.Mapper.InventoryMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Mapper.ProductInventoryMapper;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final ProductInventoryRepository productInventoryRepository;

    @Cacheable(value = "inventories",key = "#id")
    public InventoryDTO getInventoryById(Long id){
        var inventory = inventoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Inventory Not Found"));
        return InventoryMapper.toDTO(inventory);
    }

    public Page<InventoryDTO> getAllInventories(Pageable pageable){
        return inventoryRepository.findAll(pageable).map(InventoryMapper::toDTO);
    }
    @Transactional
    private InventoryDTO updateInventoryStatus(Long id , Status status, Boolean isDeleted){
        var inventory = inventoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Inventory Not Found"));

        inventory.setStatus(status);
        inventory.setIsDeleted(isDeleted != null ? isDeleted : inventory.getIsDeleted());

        if(Boolean.TRUE.equals(isDeleted)){
            inventory.setDeletedAt(LocalDateTime.now());
        }
        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);
        return InventoryMapper.toDTO(inventory);
    }
    @CachePut(value = "inventories",key = "#id")
    public InventoryDTO activeInventory(Long id){
        return updateInventoryStatus(id,Status.ACTIVE,false);
    }
    @CachePut(value = "inventories",key = "#id")
    public InventoryDTO inActiveInventory(Long id){
        return updateInventoryStatus(id,Status.INACTIVE,false);
    }
    @CacheEvict(value = "inventories",key = "#id")
    public InventoryDTO deleteInventory(Long id){
        return updateInventoryStatus(id,Status.INACTIVE,true);
    }
    @Transactional
    @CachePut(value = "inventories",key = "#id")
    public InventoryDTO updateInventory(Long id , InventoryDTO dto){
        var inventory = inventoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Inventory Not Found"));

        Optional.ofNullable(dto.getName()).ifPresent(inventory::setName);
        Optional.ofNullable(dto.getInventoryType()).ifPresent(inventory::setType);
        Optional.ofNullable(dto.getAddress()).ifPresent(inventory::setAddress);
        Optional.ofNullable(dto.getContactInfo()).ifPresent(inventory::setContactInfo);
        Optional.ofNullable(dto.getDescription()).ifPresent(inventory::setDescription);
        Optional.ofNullable(dto.getStatus()).ifPresent(inventory::setStatus);

        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        return InventoryMapper.toDTO(inventory);
    }
    @Transactional
    public InventoryDTO createInventory(InventoryDTO dto){

        var inventory = Inventory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .contactInfo(dto.getContactInfo())
                .type(dto.getInventoryType())
                .address(dto.getAddress())
                .status(dto.getStatus())
                .createdAt(LocalDateTime.now())
                .isDeleted(null)
                .deletedAt(null)
                .build();
        inventoryRepository.save(inventory);

        return InventoryMapper.toDTO(inventory);
    }

    public Page<InventoryDTO> getByName(String name,Pageable pageable){
        var inventories = inventoryRepository.findByNameContainingIgnoreCase(name,pageable);

        return inventories.map(InventoryMapper::toDTO);
    }

    public Page<InventoryDTO> getStorageInventories(Pageable pageable){
        var inventories = inventoryRepository.findByType(InventoryType.STORAGE,pageable);

        return inventories.map(InventoryMapper::toDTO);
    }

    public Page<InventoryDTO> getMaintenanceInventories(Pageable pageable){
        var inventories = inventoryRepository.findByType(InventoryType.MAINTENANCE,pageable);

        return inventories.map(InventoryMapper::toDTO);
    }

    public Page<InventoryDTO> getActiveInventories(Pageable pageable){
        var inventories = inventoryRepository.findByStatus(Status.ACTIVE,pageable);

        return inventories.map(InventoryMapper::toDTO);
    }

    public Page<InventoryDTO> getInActiveInventories(Pageable pageable){
        var inventories = inventoryRepository.findByStatus(Status.INACTIVE,pageable);

        return inventories.map(InventoryMapper::toDTO);
    }

}
