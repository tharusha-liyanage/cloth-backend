package com.project2.secondProject.service;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project2.secondProject.dto.HomeCarouselSlideDTO;
import com.project2.secondProject.entity.HomeCarouselSlide;
import com.project2.secondProject.repo.HomeCarouselSlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class HomeCarouselSlideService {

    @Autowired
    private HomeCarouselSlideRepository repo;

    @Autowired
    private Cloudinary cloudinary;

    public HomeCarouselSlide addSlide(HomeCarouselSlideDTO dto) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(dto.getImage().getBytes(),
                ObjectUtils.asMap("folder", "carousel"));

        HomeCarouselSlide slide = new HomeCarouselSlide();
        slide.setTitle(dto.getTitle());
        slide.setSubtitle(dto.getSubtitle());
        slide.setImageUrl(uploadResult.get("secure_url").toString());

        return repo.save(slide);
    }

    public List<HomeCarouselSlide> getAllSlides() {
        return repo.findAll();
    }

    public boolean deleteSlide(Long id) {
        if(repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public HomeCarouselSlide updateSlide(Long id, String title,
                                     String subtitle,
                                     MultipartFile image) throws IOException {
        HomeCarouselSlide slide = repo.findById(id).orElse(null);
        if(slide == null) return null;

        slide.setTitle(title);
        slide.setSubtitle(subtitle);

        if(image != null && !image.isEmpty()){
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "carousel"));
            slide.setImageUrl(uploadResult.get("secure_url").toString());
        }

        return repo.save(slide);
    }

    public HomeCarouselSlide getById(Long id) {
        return repo.findById(id).orElse(null);
    }

}
