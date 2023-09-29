package com.nevinabradley.recipeFinder.domain.recipeFinder.controllers;

import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceNotFoundException;
import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import com.nevinabradley.recipeFinder.domain.recipeFinder.services.RecipeFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:5173/")
@RequestMapping("/api/v1/recipe-finder")

public class RecipeFinderController {
    private RecipeFinderService recipeFinderService;

    @Autowired
    public RecipeFinderController(RecipeFinderService recipeFinderService) {this.recipeFinderService = recipeFinderService;}

    @GetMapping
    public ResponseEntity<List<RecipeFinder>> getAll() {
        List<RecipeFinder> recipeFinders = recipeFinderService.getAll();
        return new ResponseEntity<>(recipeFinders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RecipeFinder> create(@RequestBody RecipeFinder recipeFinder) {
        recipeFinder = recipeFinderService.create(recipeFinder);
        return new ResponseEntity<>(recipeFinder, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<RecipeFinder> getById(@PathVariable("id") Integer id) {
        RecipeFinder recipeFinder = recipeFinderService.getById(id);
        return new ResponseEntity<>(recipeFinder, HttpStatus.OK);
    }

    @GetMapping("lookup")
    public ResponseEntity<RecipeFinder> getByName(@RequestParam String name) {
        RecipeFinder recipeFinder = recipeFinderService.getByName(name);
        return new ResponseEntity<>(recipeFinder, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<RecipeFinder> update(@PathVariable("id") Integer id, @RequestBody RecipeFinder recipeFinderDetail) {
        try {
            recipeFinderDetail = recipeFinderService.update(id, recipeFinderDetail);
            return new ResponseEntity<>(recipeFinderDetail, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        recipeFinderService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
