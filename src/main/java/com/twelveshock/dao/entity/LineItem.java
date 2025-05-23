package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {
    @JsonProperty("name")
    public String name;
    @JsonProperty("product_id")
    public Integer productId;
    @JsonProperty("quantity")
    public Integer quantity;
    @JsonProperty("subtotal")
    public String subtotal;
    @JsonProperty("total")
    public String total;
    @JsonProperty("codigoProveedor")
    public String codigoProveedor;
    @JsonProperty("imagen")
    public String imagen;
}