package com.appsmart.test.mapper;

import com.appsmart.test.domain.ProductEntity;
import com.appsmart.test.dto.ProductDto;
import com.appsmart.test.models.requests.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductsMapper {

    ProductEntity productRequestToEntity(ProductRequest productRequest);

    ProductDto productEntityToDto(ProductEntity productEntity);

    void updateProductFromRequest(ProductRequest request, @MappingTarget ProductEntity productEntity);
}
