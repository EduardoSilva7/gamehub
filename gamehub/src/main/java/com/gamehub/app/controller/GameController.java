package com.gamehub.app.controller;

import com.gamehub.app.model.Game;
import com.gamehub.app.repository.GameRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class GameController {

    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/games")
    public String list(Model model) {
        model.addAttribute("games", gameRepository.findAll());
        return "games/list";
    }

    @GetMapping("/games/new")
    public String createForm(Model model) {
        model.addAttribute("game", new Game());
        return "games/form";
    }

    @PostMapping("/games")
    public String create(@Valid @ModelAttribute("game") Game game, BindingResult result) {
        if (result.hasErrors()) return "games/form";
        gameRepository.save(game);
        return "redirect:/games";
    }

    @GetMapping("/games/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Game g = gameRepository.findById(id).orElseThrow();
        model.addAttribute("game", g);
        return "games/form";
    }

    @PostMapping("/games/{id}/delete")
    public String delete(@PathVariable Long id) {
        gameRepository.deleteById(id);
        return "redirect:/games";
    }
}
