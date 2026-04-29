package com.adsboard.repository;

import com.adsboard.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentIsNull();

    List<Category> findByParentId(Long parentId);

    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.parent")
    List<Category> findAllWithChildren();

    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.parent WHERE c.parent IS NULL ")
    List<Category> findRootWithChildren();
}