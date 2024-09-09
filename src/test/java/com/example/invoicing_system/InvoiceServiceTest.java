package com.example.invoicing_system;

import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceRepository;
import com.example.invoicing_system.model.InvoiceStatus;
import com.example.invoicing_system.service.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import out_of_scope_services.order_management_system.Order;
import out_of_scope_services.order_management_system.OrderItem;
import shared_lib.api_clients.OrderServiceClient;
import shared_lib.models.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @Mock
    private OrderServiceClient orderServiceClient;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceService = new InvoiceServiceImpl(invoiceRepository, orderServiceClient);
    }

    @Test
    public void testCreateInvoice_OrderNotFound() {
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(null));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(1L, List.of(1L, 2L));
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testCreateInvoice_NoItemsSelected() {
        Order mockOrder = new Order(1L, 1L, 1L, List.of());
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(mockOrder));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(1L, new ArrayList<>());
        });

        assertEquals("No items selected", exception.getMessage());
    }

    @Test
    public void testCreateInvoice_ItemsNotPartOfOrder() {
        OrderItem orderItem = new OrderItem("Item1", new Money("10000"));
        orderItem.setId(1L);
        Order mockOrder = new Order(1L, 1L, 1L, List.of(orderItem));
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(mockOrder));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(1L, List.of(2L)); // Item 2 not in order
        });

        assertEquals("Selected item(s) not part of the order", exception.getMessage());
    }

    @Test
    public void testCreateInvoice_ItemAlreadyInInvoice() {
        OrderItem orderItem = new OrderItem("Item1", new Money("10000"));
        orderItem.setId(1L);
        Order mockOrder = new Order(1L, 1L, 1L, List.of(orderItem));
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(mockOrder));

        Invoice existingInvoice = new Invoice(1L, List.of(1L), new Money("10000"));
        existingInvoice.setStatus(InvoiceStatus.PENDING);
        when(invoiceRepository.findByOrderIdAndStatusIn(eq(1L), anyList())).thenReturn(List.of(existingInvoice));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            invoiceService.createInvoice(1L, List.of(1L));
        });

        assertEquals("Selected item(s) already part of an existing pending/paid invoice", exception.getMessage());
    }

    @Test
    public void testCreateInvoice_Successful() {
        OrderItem orderItem1 = new OrderItem("Item1", new Money("10000"));
        orderItem1.setId(1L);
        OrderItem orderItem2 = new OrderItem("Item2", new Money("20000"));
        orderItem2.setId(2L);
        Order mockOrder = new Order(1L, 1L, 1L, List.of(orderItem1, orderItem2));
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(mockOrder));

        when(invoiceRepository.findByOrderIdAndStatusIn(eq(1L), anyList())).thenReturn(new ArrayList<>());

        Invoice mockInvoice = new Invoice(1L, List.of(1L, 2L), new Money("30000"));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(mockInvoice);

        Invoice result = invoiceService.createInvoice(1L, List.of(1L, 2L));

        assertNotNull(result);
        assertEquals(new Money("30000"), result.getTotalAmount());
        assertEquals(List.of(1L, 2L), result.getItems());
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    public void testCreateInvoice_PartialSelectionSuccess() {
        OrderItem orderItem1 = new OrderItem("Item1", new Money("10000"));
        orderItem1.setId(1L);
        OrderItem orderItem2 = new OrderItem("Item2", new Money("20000"));
        orderItem2.setId(2L);
        Order mockOrder = new Order(1L, 1L, 1L, List.of(orderItem1, orderItem2));
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(mockOrder));

        when(invoiceRepository.findByOrderIdAndStatusIn(eq(1L), anyList())).thenReturn(new ArrayList<>());

        Invoice mockInvoice = new Invoice(1L, List.of(1L), new Money("10000"));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(mockInvoice);

        Invoice result = invoiceService.createInvoice(1L, List.of(1L)); // Only selecting item 1

        assertNotNull(result);
        assertEquals(new Money("10000"), result.getTotalAmount());
        assertEquals(List.of(1L), result.getItems());
        verify(invoiceRepository).save(any(Invoice.class));
    }
}