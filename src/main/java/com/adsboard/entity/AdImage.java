package com.adsboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая изображение, прикрепленное к объявлению
 * Класс отображается на таблицу "ad_images" в БД и хранит метаинформацию
 * об изображениях: путь к файлу, размер, MIME-тип, порядок отображения и т.д.
 * - Поддерживает определение главного изображения (isMain)
 * - Позволяет управлять порядком отображения через поле displayOrder
 * - Автоматически устанавливает дату загрузки при создании
 */
@Entity
@Table(name = "ad_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdImage {

    /**
     * Уникальный идентификатор изображения
     * Генерируется автоматически с использованием последовательности "ad_image_id_seq"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ad_images_id_seq")
    @SequenceGenerator(name = "ad_images_id_seq", sequenceName = "ad_images_id_seq", allocationSize = 1)
    private Long id;

    /**
     * Объявление, к которому относится изображение
     * - Связь многие-к-одному с сущностью Ad
     * - Ленивая загрузка для оптимизации производительности
     * - Не может быть null
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    /**
     * Путь к файлу изображения в файловой системе или хранилище
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 500 символов
     */
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    /**
     * Оригинальное имя файла изображения
     * - Обязательное поле, не может быть null
     * - Максимальная длина - 255 символов
     */
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    /**
     * Размер файла изображения в байтах
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * MIME-тип изображения (напр-р, "image/jpeg", "image/png")
     * - Максимальная длина - 100 символов
     */
    @Column(name = "mime_type", length = 100)
    private String mimeType;

    /**
     * Порядок отображения изображений в галерее
     * - Используется для сортировки изображений при отображении
     * - По умолчанию устанавливается 0
     */
    @Column(name = "display_order")
    private Integer displayOrder = 0;

    /**
     * Флаг, указывающий, яв-ся ли изображение главным для объявления
     * - По умолчанию устанаваливается false
     */
    @Column(name = "is_main")
    private Boolean isMain = false;

    /**
     * Дата и время загрузки изображения
     * - Устанаваливается автоматически перед сохранением onCreate()
     */
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    /**
     * JPA-колбэк, выполняется перед сохранением нового изображения
     * - Устанавливает поля uploadedAt текущей датой и временем
     */
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