package by.karpovich.shop.mapping;

import by.karpovich.shop.api.dto.product.ProductDtoForFindAll;
import by.karpovich.shop.api.dto.product.ProductDtoForSave;
import by.karpovich.shop.api.dto.product.ProductDtoOut;
import by.karpovich.shop.jpa.entity.CommentEntity;
import by.karpovich.shop.jpa.entity.ProductEntity;
import by.karpovich.shop.service.CharacteristicService;
import by.karpovich.shop.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CharacteristicMapper characteristicMapper;
    private final CharacteristicService characteristicService;
    private final OrganizationService organizationService;
    private final DiscountMapper discountMapper;
    private final CommentMapper commentMapper;

    public ProductEntity mapEntityFromDto(ProductDtoForSave dto) {
        if (dto == null) {
            return null;
        }

        return ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .organization(organizationService.findOrgByNameWhichWillReturnModel(dto.getOrganizationName()))
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .keywords(dto.getKeywords())
                .characteristic(characteristicService.findCharacterByIdWhichWillReturnModel(dto.getCharacteristicId()))
                .build();
    }

    public ProductDtoOut mapDtoOutFromEntity(ProductEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProductDtoOut.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .organizationName(entity.getOrganization().getName())
                .price(entity.getPrice())
                .discount(discountMapper.mapDtoFromEntity(entity.getDiscount()))
                .quantity(entity.getQuantity())
                .averageRating(getSumRatingFromProductComments(entity))
                .keywords(entity.getKeywords())
                .characteristic(characteristicMapper.mapDtoFromEntity(entity.getCharacteristic()))
                .comments(commentMapper.mapListDtoFromListEntity(entity.getComments()))
                .build();
    }

    public List<ProductDtoForFindAll> mapListDtoForFindAllFromListEntity(List<ProductEntity> entities) {
        if (entities == null) {
            return null;
        }

        List<ProductDtoForFindAll> dtos = new ArrayList<>();

        for (ProductEntity entity : entities) {
            ProductDtoForFindAll dto = new ProductDtoForFindAll();
            dto.setName(entity.getName());
            dto.setOrganizationName(entity.getOrganization().getName());
            dto.setPrice(entity.getPrice());

            dtos.add(dto);
        }

        return dtos;
    }

    public String getSumRatingFromProductComments(ProductEntity entity) {
        int size = entity.getComments().size();
        Integer sum = null;
        if (size > 0) {
            sum = entity.getComments().stream()
                    .map(CommentEntity::getRating)
                    .reduce(0, Integer::sum);

            return String.valueOf(sum / size);
        }
        return "No ratings";
    }
}
