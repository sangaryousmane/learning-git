package com.shopme.common.entity;
import lombok.*;
import javax.persistence.*;
import java.util.*;


@Entity(name = "Product")
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 256)
    private String name;

    @Column(unique = true, nullable = false, length = 256)
    private String alias;

    @Column(nullable = false, length = 512, name = "short_description")
    private String shortDescription;

    @Column(nullable = false, length = 4095, name = "full_description")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;
    private boolean enabled;

    @Column(name = "in_stock")
    private boolean InStock;

    private double price;
    private double cost;

    @Column(name = "discount_percent")
    private double discountPercent;

    private double length;
    private double width;
    private double height;
    private double weight;

    @Column(nullable = false, name = "main_image")
    private String mainImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductDetail> productDetails = new ArrayList<>();

    public void addExtraImage(String image) {
        this.images.add(new ProductImage(image, this));
    }

    public void addProductDetails(String name, String value) {
        this.productDetails.add(new ProductDetail(name, value, this));
    }

    public void addProductDetails(Integer id, String name, String value){
        this.productDetails.add(new ProductDetail(id, name, value, this));
    }

    @Transient
    public String getMainImagePath() {
        if (id == null || mainImage == null)
            return "/images/image-thumbnail.png";
        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", enabled=" + enabled +
                ", InStock=" + InStock +
                ", price=" + price +
                ", cost=" + cost +
                ", discountPercent=" + discountPercent +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", mainImage='" + mainImage + '\'' +
                ", category=" + category +
                ", brand=" + brand +
                ", images=" + images +
                ", productDetails=" + productDetails +
                '}';
    }

    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator = images.iterator();
        while (iterator.hasNext()){
            ProductImage image=iterator.next();
            if (image.getName().equals(imageName)){
                return true;
            }
        }
        return false;
    }

    @Transient
    public String getShortName(){
        if (this.length >70){
            return name.substring(0, 70).concat("..");
        }
        return name;
    }
}
