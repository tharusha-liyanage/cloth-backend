package com.project2.secondProject.repo;

import com.project2.secondProject.entity.Cloth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClothRepository extends JpaRepository<Cloth, Long> {

    List<Cloth> findByClothType(String clothType);

    // 🔥 New: Get all clothes sorted newest first
    List<Cloth> findAllByOrderByIdDesc();

    // (Optional) If you filter by type and also want newest first:
    List<Cloth> findByClothTypeOrderByIdDesc(String clothType);
}
