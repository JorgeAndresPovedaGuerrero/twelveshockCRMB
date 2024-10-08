package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
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
@MongoEntity(collection = "OrderEntity")
public class OrderEntity extends PanacheMongoEntity {
    @JsonProperty("id")
    public long id;
    @JsonProperty("status")
    public String status;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("date_created")
    public LocalDateTime dateCreated;

    @JsonProperty("date_modified")
    public Date dateModified;

    @JsonProperty("total")
    public String total;

    @JsonProperty("total_tax")
    public String totalTax;

    @JsonProperty("billing")
    public Billing billing;

    @JsonProperty("shipping")
    public Shipping shipping;

    @JsonProperty("line_items")
    public List<LineItem> lineItems;

    @JsonProperty("balance")
    public String balance;

    @JsonProperty("date_balance")
    public Date dateBalance;

    @JsonProperty("means_of_payment_1")
    public String meansOfPayment1;

    @JsonProperty("means_of_payment_2")
    public String meansOfPayment2;

    @JsonProperty("down_payment")
    public String downPayment;

}