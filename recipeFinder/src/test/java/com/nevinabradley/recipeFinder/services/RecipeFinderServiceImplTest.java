package com.nevinabradley.recipeFinder.services;

import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceNotFoundException;
import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import com.nevinabradley.recipeFinder.domain.recipeFinder.repos.RecipeFinderRepo;
import com.nevinabradley.recipeFinder.domain.recipeFinder.services.RecipeFinderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RecipeFinderServiceImplTest {

    //allows an existing object to be mocked
    @MockBean
    //mocks the RecipeFinderRepo
    private RecipeFinderRepo mockRecipeFinderRepo;

    //injects dependencies
    @Autowired
    //pulls in the RecipeFinderService
    private RecipeFinderService recipeFinderService;

    //input from the user
    private RecipeFinder inputRecipeFinder;

    //fake data/recipe to test
    private RecipeFinder mockResponseRecipe1;

    //fake data/recipe to test
    private RecipeFinder mockResponseRecipe2;

    //indicates that the method should be executed before each test
    @BeforeEach
    public void setUp() {
        //sets the name of the fake data/recipe
        mockResponseRecipe1 = new RecipeFinder("Test 1");
        //sets the id of the fake data/recipe
        mockResponseRecipe1.setId(1);

        //sets the name of the fake data/recipe
        mockResponseRecipe2 = new RecipeFinder("Test 2");
        //sets the id of the fake data/recipe
        mockResponseRecipe2.setId(2);
    }

    @Test
    @DisplayName("Recipe Finder: Create Recipe - Success")
    public void createRecipeTestSuccess(){
        RecipeFinder inputRecipeFinder = new RecipeFinder();
        //sets the name of the fake data/recipe
        inputRecipeFinder.setName("Sample Recipe");

        //mocks the method to save a recipe
        BDDMockito.doReturn(inputRecipeFinder).when(mockRecipeFinderRepo).save(ArgumentMatchers.any());

        //mocks the method to create/add a recipe
        RecipeFinder returnedRecipeFinder = recipeFinderService.create(inputRecipeFinder);

        //sends a message if any of the required fields are empty
        Assertions.assertNotNull(returnedRecipeFinder, "Recipe should not be null");
        //tests to see if the given name is the same as expected
        Assertions.assertEquals("Sample Recipe", returnedRecipeFinder.getName());
    }

    @Test
    @DisplayName("Recipe Finder: Get Recipe by Id - Success")
    public void getRecipeByIdTestSuccess() throws ResourceNotFoundException {
        //mocks the method to search for a recipe by its id
        BDDMockito.doReturn(Optional.of(mockResponseRecipe1)).when(mockRecipeFinderRepo).findById(1);
        //grabs the recipe that was found
        RecipeFinder foundRecipe = recipeFinderService.getById(1);
        //tests to see if the mocked recipe is the same as the one that the search found
        Assertions.assertEquals(mockResponseRecipe1.toString(), foundRecipe.toString());
    }

    @Test
    @DisplayName("Recipe Finder: Get Recipe by Id - Fail")
    public void getRecipeByIdTestFailed() {
        //mocks the method to search for a recipe by its id
        BDDMockito.doReturn(Optional.empty()).when(mockRecipeFinderRepo).findById(1);
        //throws an exception that says that the searched id couldn't be found/doesn't exist
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            recipeFinderService.getById(1);
        });
    }

    @Test
    @DisplayName("Recipe Finder: Get All Recipes - Success")
    public void getAllRecipesTestSuccess(){
        //makes an array list to put the fake data/recipes into
        List<RecipeFinder> recipeFinders = new ArrayList<>();
        //adds the fake data/recipe into the array list
        recipeFinders.add(mockResponseRecipe1);
        //adds the fake data/recipe into the array list
        recipeFinders.add(mockResponseRecipe2);

        //mocks the method to find all of the existing recipes
        BDDMockito.doReturn(recipeFinders).when(mockRecipeFinderRepo).findAll();

        //gets all of the existing recipes and puts them into a list
        List<RecipeFinder> responseRecipes = recipeFinderService.getAll();
        //tests to see if the array list with the fake data/recipes inside matches the one that was just made
        Assertions.assertIterableEquals(recipeFinders, responseRecipes);
    }

    @Test
    @DisplayName("Recipe Finder: Update Recipe - Success")
    public void updateRecipeTestSuccess() throws ResourceNotFoundException {

        //sets up the fake data/recipe update
        RecipeFinder expectedRecipeUpdate = new RecipeFinder();
        expectedRecipeUpdate.setName("After Update Recipe");
        expectedRecipeUpdate.setIngredients("Ingredient 1, Ingredient 2, Ingredient 3");
        expectedRecipeUpdate.setSteps("Step 1, Step 2, Step 3");
        expectedRecipeUpdate.setUser("nevina");
        expectedRecipeUpdate.setRating(5);

        //grabs the fake data/recipe from the searched id of the desired recipe to be updated
        BDDMockito.doReturn(Optional.of(mockResponseRecipe1)).when(mockRecipeFinderRepo).findById(1);
        //saves the new data/recipe information
        BDDMockito.doReturn(expectedRecipeUpdate).when(mockRecipeFinderRepo).save(ArgumentMatchers.any());

        //updates the old/original recipe with the new updated data/information provided
        RecipeFinder actualRecipe = recipeFinderService.update(1, expectedRecipeUpdate);
        //tests to see if the actual result matches the expected result
        Assertions.assertEquals(expectedRecipeUpdate.toString(), actualRecipe.toString());
    }

    @Test
    @DisplayName("Recipe Finder: Update Recipe - Fail")
    public void updateRecipeTestFail() {
        //sets a new updated name for the desired recipe to be updated
        RecipeFinder expectedRecipeUpdate = new RecipeFinder("After Update Recipe");

        //grabs the fake data/recipe from the searched id of the desired recipe to be updated
        BDDMockito.doReturn(Optional.empty()).when(mockRecipeFinderRepo).findById(1);
        //throws an exception that says that the id of the desired recipe to be updated couldn't be found/doesn't exist
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
            recipeFinderService.update(1, expectedRecipeUpdate);
        });
    }

    @Test
    @DisplayName("Recipe Finder: Delete Recipe - Success")
    public void deleteRecipeTestSuccess() throws ResourceNotFoundException {
        //grabs the fake data/recipe from the searched id of the desired recipe to be deleted
        BDDMockito.doReturn(Optional.of(mockResponseRecipe1)).when(mockRecipeFinderRepo).findById(1);
        //deletes the recipe
        Boolean actualResponse = recipeFinderService.delete(1);
        //shows that the recipe has been deleted
        Assertions.assertTrue(actualResponse);
    }

    @Test
    @DisplayName("Recipe Finder: Delete Recipe - Fail")
    public void deleteRecipeTestFail() {
        //grabs the fake data/recipe from the searched id of the desired recipe to be deleted
        BDDMockito.doReturn(Optional.empty()).when(mockRecipeFinderRepo).findById(1);
        //throws an exception that says that the id of the desired recipe to be deleted couldn't be found/doesn't exist
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
            recipeFinderService.delete(1);
        });
    }
}
