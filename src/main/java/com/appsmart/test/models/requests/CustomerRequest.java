package com.appsmart.test.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRequest {

    @JsonProperty("title")
    private String title;

    public static class Builder {
        private String title;

        public Builder title(String val) {
            title = val;
            return this;
        }

        public CustomerRequest build() {
            return new CustomerRequest(this);
        }
    }

    private CustomerRequest(Builder builder) {
        this.title = builder.title;
    }

}
