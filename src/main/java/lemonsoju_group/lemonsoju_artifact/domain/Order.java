package lemonsoju_group.lemonsoju_artifact.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders") // 테이블 이름 설정 -> 안하면 order가 되는데 sql이랑 헷갈려서 뒤에 s를 붙임
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch =  LAZY) // 자신이 n 상대가 1
    @JoinColumn(name = "member_id") // reference 하는 속성
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // OrderItem에 있는 order 필드에 의해서 레퍼런스 됨
    // cascade 옵션은 orderItems를 따로 persist 안해도 Order를 persist 할 때 자동으로 같이 persist 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch =  LAZY, cascade = CascadeType.ALL) // 1:1에서는 연관관계의 주인은 아무곳이나 해도 되지만 많이 사용하는 부분을 주인으로 설정
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING) // 열거
    private OrderStatus status; // 주문상태 [ORDER, CANCLE]

    //==연관관계 편의 메서드==// // 위치는 핵심적으로 컨트롤 하는 쪽이 더 편함
    public void setMember(Member member){ // 양방향 엔티티에서 하나를 빼먹을 수 있으므로 원자적으로 묶어서 실수를 줄일 수 있다.
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems ){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){ // 이미 배송 완료 상태라면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){ // 여러개 주문하고 취소할 때 각각 cancel 하기 위해
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){ // Alt + Enter -> 람다식 축소
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
