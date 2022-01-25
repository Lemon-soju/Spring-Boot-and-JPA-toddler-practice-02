package lemonsoju_group.lemonsoju_artifact.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // Primary Key 이름 설정
    private Long id;

    private String name;

    @Embedded // 내장 타입을 포함
    private Address address;

    @OneToMany(mappedBy = "member") // 자신이 1 상대가 n, mappedBy -> 자신은 거울 상대는 주인
    // 1:n에서 n을 연관관계의 주인으로 정한다. 주인에 foreign 키를 설정한다.
    private List<Order> orders = new ArrayList<>();


}
