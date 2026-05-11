package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ad_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_images_id_seq")
    @SequenceGenerator(name = "ad_images_id_seq", sequenceName = "ad_images_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "is_main")
    private Boolean isMain = false;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdImage)) return false;
        AdImage adImage = (AdImage) o;
        return id != null && id.equals(adImage.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}