package lemonsoju_group.lemonsoju_artifact.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 내장형 타입
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode) { // 생성될 때만 값을 저장하면 됨
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}