package com.shopme.common.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Brand")
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(
        name = "brands",
        uniqueConstraints =
        @UniqueConstraint(name = "unique_brand", columnNames = "name"))
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 128)
    private String logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "brands_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories=new HashSet<>();

    public Brand(String name) {
        this.name = name;
        this.logo="brand-logo.png";
    }

    public Brand(String name, String logo){
        this.name=name;
        this.logo=logo;
    }
    public Brand(Integer id, String name){
        this.id=id;
        this.name=name;
    }

    @Transient
    public String getLogoPath() {
        if (this.id == null)
            return "/images/image-thumbnail.png";
        return "/brand-logos/" + this.id + "/" + this.logo;
    }

}
