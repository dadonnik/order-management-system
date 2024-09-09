package com.example.receipt_system.model;

import com.example.invoicing_system.model.Invoice;
import com.example.payment_system.model.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import out_of_scope_services.order_management_system.Order;
import out_of_scope_services.order_management_system.OrderItem;
import out_of_scope_services.student_management_system.Student;
import out_of_scope_services.tenant_management_system.Tenant;
import out_of_scope_services.user_management_system.User;
import shared_lib.models.Money;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Receipt {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;

    private Long orderId;

    private String tenantName;
    private String tenantAddress;
    private String tenantLogo;

    private String userEmail;

    private String studentName;
    private Integer studentGrade;
    private String studentAvatar;

    @Embedded
    private Money paymentAmount;
    private String paymentProvider;
    private String paymentCardSchema;
    private String paymentTransactionReference;
    private LocalDateTime createdAt;

    @Lob
    private String items;

    public Receipt(Tenant tenant, User user, Student student, Payment payment, Order order, Invoice invoice) throws JsonProcessingException {
        setInvoiceId(invoice.getId());
        setOrderId(order.getId());

        setTenantName(tenant.getName());
        setTenantAddress(tenant.getAddress());
        setTenantLogo(tenant.getLogoUrl());

        setUserEmail(user.getEmail());

        setStudentName(student.getName());
        setStudentGrade(student.getGrade());
        setStudentAvatar(student.getAvatarUrl());

        setPaymentAmount(payment.getAmount());
        setPaymentProvider(payment.getPaymentProvider().toString());
        setPaymentCardSchema(payment.getCardSchema());
        setPaymentTransactionReference(payment.getTransactionReference());

        List<OrderItem> selected = order.getItems().stream().filter(item -> invoice.getItems().contains(item.getId())).toList();
        setItems(selected);
    }

    public Receipt() {

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantAddress() {
        return tenantAddress;
    }

    public void setTenantAddress(String tenantAddress) {
        this.tenantAddress = tenantAddress;
    }

    public String getTenantLogo() {
        return tenantLogo;
    }

    public void setTenantLogo(String tenantLogo) {
        this.tenantLogo = tenantLogo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(Integer studentGrade) {
        this.studentGrade = studentGrade;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }

    public Money getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Money paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public String getPaymentCardSchema() {
        return paymentCardSchema;
    }

    public void setPaymentCardSchema(String paymentCardSchema) {
        this.paymentCardSchema = paymentCardSchema;
    }

    public String getPaymentTransactionReference() {
        return paymentTransactionReference;
    }

    public void setPaymentTransactionReference(String paymentTransactionReference) {
        this.paymentTransactionReference = paymentTransactionReference;
    }

    public List<OrderItem> getItems() throws JsonProcessingException {
        return objectMapper.readValue(this.items, new TypeReference<>() {
        });
    }

    public void setItems(List<OrderItem> items) throws JsonProcessingException {
        this.items = objectMapper.writeValueAsString(items);
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}