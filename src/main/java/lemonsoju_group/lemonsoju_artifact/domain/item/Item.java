package lemonsoju_group.lemonsoju_artifact.domain.item;

import lemonsoju_group.lemonsoju_artifact.domain.Category;
import lemonsoju_group.lemonsoju_artifact.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 테이블에 모든 속성을 넣어서 관리
@DiscriminatorColumn(name = "dtype") // 구별하기
@Getter @Setter
public abstract class Item { // 구현체를 가지고 할거라서 추상 클래스로 만듬
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//

    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /** 주석 Ctrl + Shift + Enter
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}