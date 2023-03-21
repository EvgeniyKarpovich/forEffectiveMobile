package by.karpovich.shop.api.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDto {

    private String name;

    private int discountPercentage;

    private Instant startDiscount;

    private Instant finishDiscount;
}
