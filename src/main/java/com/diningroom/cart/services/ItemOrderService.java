package com.diningroom.cart.services;


import com.diningroom.cart.dto.ItemOrderDTO;
import com.diningroom.cart.dto.OrdersDTO;
import com.diningroom.cart.entities.ItemOrder;
import com.diningroom.cart.entities.Orders;
import com.diningroom.cart.feignclients.ClientFeignClient;
import com.diningroom.cart.feignclients.ProductFeignClient;
import com.diningroom.cart.feignclients.WarehouseFeignClient;
import com.diningroom.cart.repository.ItemOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemOrderService {

    @Autowired
    private ItemOrderRepository repository;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ClientFeignClient clientFeignClient;
    @Autowired
    private WarehouseFeignClient warehouseFeignClient;

    public ResponseEntity<ItemOrderDTO> findById(Long id) {
        Optional<ItemOrder> optionalItemOrder = repository.findById(id);

        if (optionalItemOrder.isPresent()) {
            ItemOrder entity = optionalItemOrder.get();
            ItemOrderDTO dto = new ItemOrderDTO(entity);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public List<ItemOrderDTO>  listAll() {
        List<ItemOrder> itemOrders = this.repository.findAll();
        return itemOrders.stream()
                .map(this::itemOrderToItemOrderDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> mountNewItemOrder(ItemOrderDTO itemOrderDTOparam) {

        if(!productFeignClient.exists(itemOrderDTOparam.getProductId()).getBody()) {
            return new ResponseEntity<>("Produto de ID " + itemOrderDTOparam.getProductId() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(!clientFeignClient.exists(itemOrderDTOparam.getClientId()).getBody()) {
            return new ResponseEntity<>("Cliente de ID " + itemOrderDTOparam.getClientId() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        try{
            warehouseFeignClient.updateStockByProduct(itemOrderDTOparam.getProductId(), itemOrderDTOparam.getQuantity());
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        ItemOrder newItemOrder = new ItemOrder();
        newItemOrder.setQuantity(itemOrderDTOparam.getQuantity());
        newItemOrder.setProductId(itemOrderDTOparam.getProductId());
        newItemOrder.setUnitPrice(productFeignClient.getPrice(itemOrderDTOparam.getProductId()).getBody());
        newItemOrder.calculateSubTotalPrice();
        newItemOrder.setClientId(itemOrderDTOparam.getClientId());

        Orders order = this.createOrderIfNotExists(itemOrderDTOparam, newItemOrder);

        newItemOrder.setOrders(order);

        this.create(newItemOrder);

        return new ResponseEntity<>("Item Pedido criado com sucesso.", HttpStatus.CREATED);
    }

    @Transactional
    public void create(ItemOrder itemOrder) {
        repository.save(itemOrder);
    }

    @Transactional
    public ResponseEntity<String> update(Long id, ItemOrderDTO itemOrderDTO) {
        if(!this.repository.existsById(id)) {
            return new ResponseEntity<>(" Pedido " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(itemOrderDTO.getProductId() != null && !productFeignClient.exists(itemOrderDTO.getProductId()).getBody()) {
            return new ResponseEntity<>("Produto de ID " + itemOrderDTO.getProductId() + " não encontrado.", HttpStatus.NOT_FOUND);
        }

        if(itemOrderDTO.getSubTotalPrice() != null) {
            return new ResponseEntity<>("Informe o preço unitário ou a quantidade para mudar o preço subtotal!", HttpStatus.FORBIDDEN);
        }

        if(itemOrderDTO.getUnitPrice() != null) {
            return new ResponseEntity<>("Preço unitário é indicado no Produto!", HttpStatus.FORBIDDEN);
        }

        ItemOrder itemOrderToUpdate = this.repository.findById(id).get();

        try{
            warehouseFeignClient.updateStockByProduct(itemOrderToUpdate.getProductId(), itemOrderDTO.getQuantity());
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        BeanUtils.copyProperties(itemOrderDTO, itemOrderToUpdate, this.getNullPropertyNames(itemOrderDTO));

        if(itemOrderDTO.getProductId() != null) {
            itemOrderToUpdate.setProductId(itemOrderDTO.getProductId());
            itemOrderToUpdate.setUnitPrice(productFeignClient.getPrice(itemOrderDTO.getProductId()).getBody());

            if(itemOrderDTO.getQuantity() == 0) {
                itemOrderToUpdate.setQuantity(1);
            }
        }
        itemOrderToUpdate.calculateSubTotalPrice();

        ordersService.updateItemOrderInOrder(itemOrderToUpdate);

        this.repository.save(itemOrderToUpdate);

        return new ResponseEntity<>("Item do Pedido atualizado com sucesso.", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> delete(Long id) {
        Optional<ItemOrder> optionalItemOrder = this.repository.findById(id);

        if (optionalItemOrder.isPresent()) {
            ItemOrder itemOrder = optionalItemOrder.get();

            ResponseEntity<String> responseOrder = ordersService.deleteItemOrderFromOrder(itemOrder);

            if (responseOrder.getStatusCode() == HttpStatus.NOT_FOUND) {
                return responseOrder;
            }

            this.repository.deleteById(id);
            return new ResponseEntity<>("Item do Pedido deletado com sucesso.", HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Item do Pedido com ID " + id + " não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Orders createOrderIfNotExists(ItemOrderDTO itemOrderDTOparam, ItemOrder newItemOrder) {
        OrdersDTO orderDTO = ordersService.findByClientIdAndPaid(itemOrderDTOparam.getClientId());

        if(orderDTO == null) {
            OrdersDTO newOrderDTO = new OrdersDTO();
            newOrderDTO.setPaid(false);
            newOrderDTO.setDateTime(LocalDateTime.now());
            newOrderDTO.setClientId(itemOrderDTOparam.getClientId());
            newOrderDTO.setTotalPrice(newItemOrder.getSubTotalPrice());

            ItemOrderDTO newItemOrderDTO = new ItemOrderDTO();
            newItemOrderDTO.setProductId(newItemOrderDTO.getProductId());
            newItemOrderDTO.setQuantity(newItemOrder.getQuantity());
            newItemOrderDTO.setUnitPrice(newItemOrder.getUnitPrice());
            newItemOrderDTO.setSubTotalPrice(newItemOrder.getSubTotalPrice());
            newItemOrderDTO.setClientId(newItemOrder.getClientId());

            List<ItemOrderDTO> newList = new ArrayList<>();
            newList.add(newItemOrderDTO);
            newOrderDTO.setItemsOrder(newList);

            Long newOrderId = ordersService.create(newOrderDTO);

            Orders newOrder = ordersService.findEntityById(newOrderId);
            orderDTO = ordersService.entityToDTO(newOrder);
        } else {
            ordersService.addItemOrderinOrder(orderDTO.getId(), newItemOrder);
        }

        return ordersService.findEntityById(orderDTO.getId());
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private ItemOrderDTO itemOrderToItemOrderDTO(ItemOrder itemOrder) {
        return new ItemOrderDTO(itemOrder);
    }
}
