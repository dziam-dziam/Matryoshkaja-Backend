package com.matryoshkaja.demo.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    @NotBlank
    private String imageUrl;

    @Column(name = "image_key", nullable = false)
    @NotBlank
    private String imageKey;

    @Column(name = "caption")
    private String caption;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "photo_carousel_images", joinColumns = @JoinColumn(name = "photo_id"))
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<PhotoCarouselImage> carouselImages = new ArrayList<>();

    @Override
    public final boolean equals(Object object){
        if(this == object) return true;
        if(object == null) return false;
        Class<? extends Photo> thisClass = Hibernate.getClass(this);
        Class<?> otherClass = Hibernate.getClass(object);
        if(thisClass != otherClass) return false;
        Photo other = (Photo) object;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public final int hashCode(){
        return Hibernate.getClass(this).hashCode();
    }
}
