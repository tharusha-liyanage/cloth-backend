package com.project2.secondProject.controller;




import com.project2.secondProject.dto.HomeCarouselSlideDTO;
import com.project2.secondProject.entity.HomeCarouselSlide;
import com.project2.secondProject.service.HomeCarouselSlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/homecarousel")
@CrossOrigin(origins = "http://localhost:5173")
public class CarouselSlideController {

    @Autowired
    private HomeCarouselSlideService service;

    @PostMapping("/add")
    public HomeCarouselSlide addSlide(
            @RequestParam("title") String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        HomeCarouselSlideDTO dto = new HomeCarouselSlideDTO();
        dto.setTitle(title);
        dto.setSubtitle(subtitle);
        dto.setImage(image);

        return service.addSlide(dto);
    }

    @GetMapping("/all")
    public List<HomeCarouselSlide> getAll() {
        return service.getAllSlides();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSlide(@PathVariable Long id){
        return service.deleteSlide(id) ?
                "Deleted successfully" : "Slide not found";
    }

    @PutMapping("/update/{id}")
    public HomeCarouselSlide updateSlide(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("subtitle") String subtitle,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        return service.updateSlide(id, title, subtitle, image);
    }
}

