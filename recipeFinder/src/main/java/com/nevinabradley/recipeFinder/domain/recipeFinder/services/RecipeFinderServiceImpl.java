package com.nevinabradley.recipeFinder.domain.recipeFinder.services;

import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceNotFoundException;
import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import com.nevinabradley.recipeFinder.domain.recipeFinder.repos.RecipeFinderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeFinderServiceImpl implements RecipeFinderService {
    private RecipeFinderRepo recipeFinderRepo;

    @Autowired
    public RecipeFinderServiceImpl(RecipeFinderRepo recipeFinderRepo) {this.recipeFinderRepo = recipeFinderRepo;}

    @Override
    public RecipeFinder create(RecipeFinder recipeFinder) {
        Optional<RecipeFinder> optional = recipeFinderRepo.findByName(recipeFinder.getName());
        recipeFinder = recipeFinderRepo.save(recipeFinder);
        return recipeFinder;
    }

    @Override
    public RecipeFinder getById(Integer id) throws ResourceNotFoundException {
        RecipeFinder recipeFinder = recipeFinderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("No recipe with id: " + id));
        return recipeFinder;
    }

    @Override
    public RecipeFinder getByName(String name) throws ResourceNotFoundException {
        RecipeFinder recipeFinder = recipeFinderRepo.findByName(name).orElseThrow(()->new ResourceNotFoundException("No recipe with name: " + name));
        return recipeFinder;
    }

    @Override
    public List<RecipeFinder> getAll() {return recipeFinderRepo.findAll();}

    @Override
    public RecipeFinder update(Integer id, RecipeFinder recipeFinderDetail) throws ResourceNotFoundException {
        RecipeFinder recipeFinder = getById(id);
        recipeFinder.setName(recipeFinderDetail.getName());
        recipeFinder.setIngredients(recipeFinderDetail.getIngredients());
        recipeFinder.setSteps(recipeFinderDetail.getSteps());
        recipeFinder.setUser(recipeFinderDetail.getUser());
        recipeFinder.setRating(recipeFinderDetail.getRating());
        recipeFinder = recipeFinderRepo.save(recipeFinder);
        return recipeFinder;
    }

    @Override
    public Boolean delete(Integer id) throws ResourceNotFoundException {
        try {
            RecipeFinder recipeFinder = getById(id);
            recipeFinderRepo.delete(recipeFinder);
            return true;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }
}
