package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shipping {
    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("identification")
    public String identification;

    @JsonProperty("address_1")
    public String address1;

    @JsonProperty("address_2")
    public String address2;

    @JsonProperty("city")
    public String city;

    @JsonProperty("state")
    public String state;

    @JsonProperty("postcode")
    public String postcode;

    @JsonProperty("country")
    public String country;

    @JsonProperty("price_shipping")
    public Long priceShipping = 0L;

    @JsonProperty("paymentType")
    public String paymentType;
}
