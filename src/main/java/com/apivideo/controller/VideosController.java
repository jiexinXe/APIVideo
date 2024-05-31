package com.apivideo.controller;

import com.apivideo.entity.Videos;
import com.apivideo.service.VideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideosController {

    @Autowired
    private VideosService videosService;

    @GetMapping("/recommend")
    public List<Videos> getRecommendedVideos() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        return videosService.getRecommendedVideos(username, 4);
    }
}
