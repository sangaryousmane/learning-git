package com.shopme.common.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity(name = "ProductDetail")
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;


    @ManyToOne
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "product_details_fk")
    )
    private Product product;

    public ProductDetail(String name, String value,
                         Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }

    public ProductDetail(Integer id, String name,
                         String value, Product product) {
        this.id=id;
        this.name=name;
        this.value=value;
        this.product=product;
    }
}
