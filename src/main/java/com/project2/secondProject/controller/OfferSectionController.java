package com.project2.secondProject.controller;

import com.project2.secondProject.dto.OfferSectionDTO;
import com.project2.secondProject.entity.OfferSection;
import com.project2.secondProject.service.OfferSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "http://localhost:3000")
public class OfferSectionController {

    @Autowired
    private OfferSectionService service;

    // ---------------------- ADD OFFER ----------------------
    @PostMapping("/add")
    public ResponseEntity<?> addOffer(
            @RequestParam("title1") String title1,
            @RequestParam("title2") String title2,
            @RequestParam("image") MultipartFile image) {

        OfferSectionDTO dto = new OfferSectionDTO();
        dto.setTitle1(title1);
        dto.setTitle2(title2);
        dto.setImage(image);

        try {
            OfferSection offer = service.addOffer(dto);
            return ResponseEntity.ok(offer);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload failed!");
        }
    }

    // ---------------------- GET ALL OFFERS ----------------------
    @GetMapping("/all")
    public List<OfferSection> getAllOffers() {
        return service.getAllOffers();
    }

    // ---------------------- GET BY ID ----------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getOfferById(@PathVariable Long id) {
        OfferSection offer = service.getById(id);
        if (offer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Offer not found");
        }
        return ResponseEntity.ok(offer);
    }

    // ---------------------- DELETE OFFER ----------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long id) {
        boolean deleted = service.deleteOffer(id);
        if (deleted) {
            return ResponseEntity.ok("Offer deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Offer not found");
    }

    // ---------------------- UPDATE OFFER ----------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOffer(
            @PathVariable Long id,
            @RequestParam("title1") String title1,
            @RequestParam("title2") String title2,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            OfferSection updated = service.updateOffer(id, title1, title2, image);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Offer not found");
            }
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload failed during update!");
        }
    }
}
