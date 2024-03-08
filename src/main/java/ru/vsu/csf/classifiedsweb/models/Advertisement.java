package ru.vsu.csf.classifiedsweb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "advertisements")
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "advertisement")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "advertisement")
    private List<Reaction> reactions = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addImageToAdvertisement(Image image) {
        image.setAdvertisement(this);
        images.add(image);
    }

//    public void deleteImages() {
//        for (Image image : images) {
//            image.setAdvertisement(null);
//        }
//    }
}
