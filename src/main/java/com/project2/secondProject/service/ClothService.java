package com.project2.secondProject.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project2.secondProject.dto.ClothDTO;
import com.project2.secondProject.entity.Cloth;
import com.project2.secondProject.repo.ClothRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClothService {

    @Autowired
    private ClothRepository clothRepository;

    @Autowired
    private Cloudinary cloudinary;

    // ✅ Save new cloth with image upload
    @Transactional
    public Cloth saveCloth(ClothDTO dto) throws IOException {
        if (dto.getImage() == null || dto.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // ✅ Upload image to Cloudinary folder
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                dto.getImage().getBytes(),
                ObjectUtils.asMap("folder", "mahinda_trade_center")
        );

        // ✅ Create entity and map fields
        Cloth cloth = new Cloth();
        cloth.setClothName(dto.getClothName());
        cloth.setPrice(dto.getPrice());
        cloth.setSizes(dto.getSizes());
        cloth.setClothType(dto.getClothType());

        // ✅ Ensure numeric stock values
        Map<String, Integer> validStock = new HashMap<>();
        if (dto.getStockCount() != null) {
            dto.getStockCount().forEach((size, count) -> {
                if (count != null && count >= 0) {
                    validStock.put(size, count);
                }
            });
        }
        cloth.setStockCount(validStock);

        // ✅ Set uploaded image URL
        cloth.setImageUrl(uploadResult.get("secure_url").toString());

        // ✅ Save to DB
        return clothRepository.save(cloth);
    }

    // ✅ Get all clothes
    public List<Cloth> getAllClothes() {
        return clothRepository.findAll();
    }

    // ✅ Get clothes by type
    public List<Cloth> getByType(String type) {
        return clothRepository.findByClothType(type);
    }

    // ✅ Get single cloth by ID
    public Cloth getById(Long id) {
        return clothRepository.findById(id).orElse(null);
    }

    // ✅ Update existing cloth
    @Transactional
    public Cloth updateCloth(Long id, String clothName, double price, List<String> sizes,
                             String clothType, Map<String, Integer> stockCount,
                             MultipartFile image) throws IOException {

        Cloth existing = clothRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        existing.setClothName(clothName);
        existing.setPrice(price);
        existing.setSizes(sizes);
        existing.setClothType(clothType);
        existing.setStockCount(stockCount);

        // ✅ If new image uploaded, replace old one
        if (image != null && !image.isEmpty()) {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("folder", "mahinda_trade_center")
            );
            existing.setImageUrl(uploadResult.get("secure_url").toString());
        }

        return clothRepository.save(existing);
    }

    // ✅ Delete cloth by ID
    public boolean deleteCloth(Long id) {
        if (clothRepository.existsById(id)) {
            clothRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
