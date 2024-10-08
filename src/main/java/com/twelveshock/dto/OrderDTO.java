package com.twelveshock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.twelveshock.dao.entity.Billing;
import com.twelveshock.dao.entity.LineItem;
import com.twelveshock.dao.entity.Shipping;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDTO {
    private long id;
    private String status;
    private String currency;
    private LocalDateTime date_created;
    private Date date_modified;
    private String total;
    private String total_tax;
    private Billing billing;
    private Shipping shipping;
    private List<LineItem> line_items;
    private String balance;
    private Date date_balance;
    private String down_payment;
    private String means_of_payment_1;
    private String means_of_payment_2;
}
