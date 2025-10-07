package com.gamehub.app.controller;

import com.gamehub.app.model.Review;
import com.gamehub.app.repository.ReviewRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/reviews/game/{gameId}")
    public String listByGame(@PathVariable Long gameId, Model model) {
        model.addAttribute("reviews", reviewRepository.findByGameId(gameId));
        model.addAttribute("gameId", gameId);
        return "reviews/list";
    }

    @GetMapping("/reviews/game/{gameId}/new")
    public String newForm(@PathVariable Long gameId, Model model) {
        Review r = new Review();
        r.setGameId(gameId);
        model.addAttribute("review", r);
        return "reviews/form";
    }

    @PostMapping("/reviews")
    public String create(@Valid @ModelAttribute("review") Review review, BindingResult result) {
        if (result.hasErrors()) return "reviews/form";
        reviewRepository.save(review);
        return "redirect:/reviews/game/" + review.getGameId();
    }

    @GetMapping("/reviews/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Review r = reviewRepository.findById(id).orElseThrow();
        model.addAttribute("review", r);
        return "reviews/form";
    }

    @PostMapping("/reviews/{id}/delete")
    public String delete(@PathVariable Long id) {
        Review r = reviewRepository.findById(id).orElseThrow();
        Long gameId = r.getGameId();
        reviewRepository.deleteById(id);
        return "redirect:/reviews/game/" + gameId;
    }
}
