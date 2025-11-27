package com.pragma.powerup.ordermicroservice.infrastructure.input.rest;

import com.pragma.powerup.ordermicroservice.application.dto.request.AssignChefRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.CreateOrderRequest;
import com.pragma.powerup.ordermicroservice.application.dto.request.UpdateOrderStatusRequest;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderPageResponse;
import com.pragma.powerup.ordermicroservice.application.dto.response.OrderResponse;
import com.pragma.powerup.ordermicroservice.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order operations")
@Validated
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Create order", responses = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid payload")
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderHandler.createOrder(request));
    }

    @Operation(summary = "Assign chef", responses = {
            @ApiResponse(responseCode = "200", description = "Chef assigned"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/chef")
    public ResponseEntity<OrderResponse> assignChef(@PathVariable String orderId,
                                                    @Valid @RequestBody AssignChefRequest request) {
        return ResponseEntity.ok(orderHandler.assignChef(orderId, request));
    }

    @Operation(summary = "Update status", responses = {
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable String orderId,
                                                      @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderHandler.updateStatus(orderId, request));
    }

    @Operation(summary = "Get order by id", responses = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderHandler.getById(orderId));
    }

    @Operation(summary = "List orders by restaurant and status", responses = {
            @ApiResponse(responseCode = "200", description = "Orders found")
    })
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<OrderPageResponse> getAllOrdersByStatus(
            @RequestParam(defaultValue = "PENDIENTE") String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(orderHandler.getAllOrdersByStatus(page, size, status));
    }

    @Operation(summary = "Assign order to employee", responses = {
            @ApiResponse(responseCode = "200", description = "Order assigned"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Order belongs to another restaurant")
    })
    @PatchMapping("/{orderId}/assign")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<OrderResponse> assignOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderHandler.assignOrder(orderId));
    }

    @Operation(summary = "Mark order as ready", responses = {
            @ApiResponse(responseCode = "200", description = "Order marked as ready"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Order belongs to another restaurant")
    })
    @PatchMapping("/{orderId}/ready")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<OrderResponse> markOrderReady(@PathVariable String orderId) {
        return ResponseEntity.ok(orderHandler.markOrderReady(orderId));
    }

    @Operation(summary = "Deliver order", responses = {
            @ApiResponse(responseCode = "204", description = "Order delivered"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Order not ready or PIN mismatch")
    })
    @PostMapping("/deliver/{orderId}/{pin}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> deliverOrder(@PathVariable String orderId, @PathVariable String pin) {
        orderHandler.deliverOrder(orderId, pin);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel order", responses = {
            @ApiResponse(responseCode = "204", description = "Order cancelled"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "Order cannot be cancelled"),
            @ApiResponse(responseCode = "403", description = "Order does not belong to user")
    })
    @PatchMapping("/cancel/{orderId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        orderHandler.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
