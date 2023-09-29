package com.nevinabradley.recipeFinder.domain.recipeFinder.services;

import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceCreationException;
import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceNotFoundException;
import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;

import java.util.List;

public interface RecipeFinderService {
    RecipeFinder create(RecipeFinder recipeFinder) throws ResourceCreationException;

    RecipeFinder getById(Integer id) throws ResourceNotFoundException;

    RecipeFinder getByName(String name) throws ResourceNotFoundException;

    List<RecipeFinder> getAll();

    RecipeFinder update(Integer id, RecipeFinder recipeFinderDetail) throws ResourceNotFoundException;

    Boolean delete(Integer id);
}
