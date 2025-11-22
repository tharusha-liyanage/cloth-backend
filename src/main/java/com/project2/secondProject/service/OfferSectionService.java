package com.project2.secondProject.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project2.secondProject.dto.OfferSectionDTO;
import com.project2.secondProject.entity.OfferSection;
import com.project2.secondProject.repo.OfferSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class OfferSectionService {

    @Autowired
    private OfferSectionRepository repo;

    @Autowired
    private Cloudinary cloudinary;

    // -------------------- ADD OFFER --------------------
    public OfferSection addOffer(OfferSectionDTO dto) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(
                dto.getImage().getBytes(),
                ObjectUtils.asMap("folder", "offer-section")
        );

        OfferSection offer = new OfferSection();
        offer.setTitle1(dto.getTitle1());
        offer.setTitle2(dto.getTitle2());
        offer.setImageUrl(uploadResult.get("secure_url").toString());

        return repo.save(offer);
    }

    // -------------------- GET ALL --------------------
    public List<OfferSection> getAllOffers() {
        return repo.findAll();
    }

    // -------------------- GET BY ID --------------------
    public OfferSection getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // -------------------- DELETE --------------------
    public boolean deleteOffer(Long id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    // -------------------- UPDATE OFFER --------------------
    public OfferSection updateOffer(Long id,
                                    String title1,
                                    String title2,
                                    MultipartFile imageFile) throws IOException {

        OfferSection offer = repo.findById(id).orElse(null);
        if (offer == null) return null;

        offer.setTitle1(title1);
        offer.setTitle2(title2);

        if (imageFile != null && !imageFile.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap("folder", "offer-section")
            );

            offer.setImageUrl(uploadResult.get("secure_url").toString());
        }

        return repo.save(offer);
    }
}
