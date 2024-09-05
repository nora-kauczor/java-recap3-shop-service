import lombok.With;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.List;

public record Order(
        String id,
        List<Product> products,
        @With
        OrderStatus status,
        ZonedDateTime ordered
) {
}
