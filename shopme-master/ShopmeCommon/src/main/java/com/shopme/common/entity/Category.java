package com.shopme.common.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Category")
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, unique = true, nullable = false)
    private String name;

    @Column(length = 64, nullable = false)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;
    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private Set<Category> children = new HashSet<>();

    @Column(name = "all_parents_id", length = 256)
    private String allParentIDs;

    @Transient
    private boolean hasChildren;

    public void setChildren(Set<Category> children) {
        this.children = children;
    }


    public Category(String name) {
        this.image = "default.png";
        this.alias = name;
        this.name = name;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category(Integer id) {
        this.id = id;
    }

    public static Category copyIdAndName(Category category) {
        Category category1 = new Category();
        category1.setId(category.getId());
        category1.setName(category.getName());

        return category1;
    }

    public static Category copyIdAndName(Integer id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);

        return category;
    }

    public static Category copyFull(Category category) {
        Category category1 = new Category();
        category1.setId(category.getId());
        category1.setName(category.getName());
        category1.setImage(category.getImage());
        category1.setAlias(category.getAlias());
        category1.setEnabled(category.isEnabled());
        category1.setHasChildren(category.getChildren().size() > 0);

        return category1;
    }

    public static Category copyFull(Category category, String name) {
        Category category1 = Category.copyFull(category);
        category1.setName(name);

        return category1;
    }

    @Transient
    public String getImagePath() {
        if (this.id == null)
            return "/images/image-thumbnail.png";
        return "/category-images/" + this.id + "/" + this.image;
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }
}
