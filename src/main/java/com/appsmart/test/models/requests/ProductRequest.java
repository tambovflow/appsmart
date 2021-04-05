package com.appsmart.test.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("price")
    private BigDecimal price;

    public static class Builder {

        private String title;
        private String description;
        private BigDecimal price;

        public Builder(double val) {
            price = BigDecimal.valueOf(val);
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public ProductRequest build() {
            return new ProductRequest(this);
        }
    }

    private ProductRequest(Builder builder) {
        this.price = builder.price;
        this.title = builder.title;
        this.description = builder.description;
    }
}
