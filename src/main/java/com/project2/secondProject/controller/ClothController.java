package com.project2.secondProject.controller;

import com.project2.secondProject.dto.ClothDTO;
import com.project2.secondProject.entity.Cloth;
import com.project2.secondProject.service.ClothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/clothes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClothController {

    @Autowired
    private ClothService clothService;

    // ✅ Add new cloth
    @PostMapping("/add")
    public Cloth addCloth(
            @RequestParam String clothName,
            @RequestParam double price,
            @RequestParam List<String> sizes,
            @RequestParam String clothType,
            @RequestParam Map<String, String> allParams,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        // ✅ Extract stock counts
        Map<String, Integer> numericStock = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("stockCount[")) {
                String sizeKey = key.replace("stockCount[", "").replace("]", "");
                try {
                    numericStock.put(sizeKey, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid stock number for " + sizeKey);
                }
            }
        });

        // ✅ Create DTO
        ClothDTO dto = new ClothDTO();
        dto.setClothName(clothName);
        dto.setPrice(price);
        dto.setSizes(sizes);
        dto.setClothType(clothType);
        dto.setStockCount(numericStock);
        dto.setImage(image);

        return clothService.saveCloth(dto);
    }

    // ✅ Retrieve all clothes
    @GetMapping("/allcloth")
    public List<Cloth> getAll() {
        return clothService.getAllClothes();
    }

    // ✅ Get by type (avoid conflict with ID by using explicit route)
    @GetMapping("/type/{type}")
    public List<Cloth> getByType(@PathVariable String type) {
        return clothService.getByType(type);
    }

    // ✅ Get by ID
    @GetMapping("/id/{id}")
    public Cloth getClothById(@PathVariable Long id) {
        return clothService.getById(id);
    }

    // ✅ Update existing cloth
    @PutMapping("/update/{id}")
    public Cloth updateCloth(
            @PathVariable Long id,
            @RequestParam String clothName,
            @RequestParam double price,
            @RequestParam List<String> sizes,
            @RequestParam String clothType,
            @RequestParam Map<String, String> allParams,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {

        // ✅ Extract numeric stock values
        Map<String, Integer> numericStock = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (key.startsWith("stockCount[")) {
                String sizeKey = key.replace("stockCount[", "").replace("]", "");
                try {
                    numericStock.put(sizeKey, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid stock number for " + sizeKey);
                }
            }
        });

        return clothService.updateCloth(id, clothName, price, sizes, clothType, numericStock, image);
    }

    // ✅ Delete cloth
    @DeleteMapping("/delete/{id}")
    public String deleteCloth(@PathVariable Long id) {
        boolean deleted = clothService.deleteCloth(id);
        return deleted ? "Deleted Successfully" : "Cloth not found";
    }
}
