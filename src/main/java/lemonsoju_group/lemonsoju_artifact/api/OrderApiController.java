package lemonsoju_group.lemonsoju_artifact.api;

import lemonsoju_group.lemonsoju_artifact.domain.Order;
import lemonsoju_group.lemonsoju_artifact.domain.OrderItem;
import lemonsoju_group.lemonsoju_artifact.repository.OrderRepository;
import lemonsoju_group.lemonsoju_artifact.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) { // iter + Tab
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
}
