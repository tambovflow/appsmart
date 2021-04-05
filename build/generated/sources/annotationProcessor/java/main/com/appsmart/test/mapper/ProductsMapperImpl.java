package com.appsmart.test.mapper;

import com.appsmart.test.domain.ProductEntity;
import com.appsmart.test.dto.ProductDto;
import com.appsmart.test.models.requests.ProductRequest;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-05T16:36:26+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.3.jar, environment: Java 1.8.0_212 (Oracle Corporation)"
)
@Component
public class ProductsMapperImpl implements ProductsMapper {

    @Override
    public ProductEntity productRequestToEntity(ProductRequest productRequest) {
        if ( productRequest == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setTitle( productRequest.getTitle() );
        productEntity.setDescription( productRequest.getDescription() );
        productEntity.setPrice( productRequest.getPrice() );

        return productEntity;
    }

    @Override
    public ProductDto productEntityToDto(ProductEntity productEntity) {
        if ( productEntity == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( productEntity.getId() );
        productDto.setTitle( productEntity.getTitle() );
        productDto.setDescription( productEntity.getDescription() );
        productDto.setPrice( productEntity.getPrice() );
        productDto.setIsDeleted( productEntity.getIsDeleted() );
        productDto.setCreatedAt( productEntity.getCreatedAt() );
        productDto.setModifiedAt( productEntity.getModifiedAt() );

        return productDto;
    }

    @Override
    public void updateProductFromRequest(ProductRequest request, ProductEntity productEntity) {
        if ( request == null ) {
            return;
        }

        productEntity.setTitle( request.getTitle() );
        productEntity.setDescription( request.getDescription() );
        productEntity.setPrice( request.getPrice() );
    }
}
