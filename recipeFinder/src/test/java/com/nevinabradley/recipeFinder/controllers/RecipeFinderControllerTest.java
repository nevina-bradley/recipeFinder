package com.nevinabradley.recipeFinder.controllers;

import com.nevinabradley.recipeFinder.domain.core.exceptions.ResourceNotFoundException;
import com.nevinabradley.recipeFinder.domain.recipeFinder.models.RecipeFinder;
import com.nevinabradley.recipeFinder.domain.recipeFinder.services.RecipeFinderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.core.Is;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
//allows HTTP requests to be mocked
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RecipeFinderControllerTest {

    //allows an existing object to be mocked
    @MockBean
    //mocks the RecipeFinderService
    private RecipeFinderService mockRecipeFinderService;

    //injects dependencies
    @Autowired
    //mocks the MVC (model view controller)
    private MockMvc mockMvc;

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

        //sets the fake data to go into the parameters
        inputRecipeFinder = new RecipeFinder("Test Recipe");
        inputRecipeFinder.setIngredients("Ingredient 1, Ingredient 2, Ingredient 3");
        inputRecipeFinder.setSteps("Step 1, Step 2, Step 3");
        inputRecipeFinder.setUser("John Doe");
        inputRecipeFinder.setRating(4);
    }

    @Test
    @DisplayName("Recipe Finder: /api/v1/recipe-finder - Success")
    public void createRecipeRequestSuccess() throws Exception {
        //mocks the method to create/add a recipe
        BDDMockito.doReturn(mockResponseRecipe1).when(mockRecipeFinderService).create(any());

        //mocks the HTTP post method at the specified url
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe-finder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputRecipeFinder)))
                //expects the status code to be a 201
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                //tests to see if the id matches the expected
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)))
                //tests to see if the name matches the expected
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("Test 1")));
    }

    private String asJsonString(final Object obj) {
        //formats the output as json
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("GET /api/v1/recipe-finder/1 - Found")
    public void getRecipeByIdTestSuccess() throws Exception {
        //mocks the method to search for a recipe by its id
        BDDMockito.doReturn(mockResponseRecipe1).when(mockRecipeFinderService).getById(1);

        //mocks the HTTP get method at the specified url
        mockMvc.perform(get("/api/v1/recipe-finder/{id}", 1))
                //expects the status code to be a 200
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //tests to see if the id matches the expected
                .andExpect(jsonPath("$.id", is(1)))
                //tests to see if the name matches the expected
                .andExpect(jsonPath("$.name", is("Test 1")));
    }

    @Test
    @DisplayName("GET /api/v1/recipe-finder/1 - Not Found")
    public void getRecipeByIdTestFailed() throws Exception {
        //throws an exception that says that the searched id couldn't be found/doesn't exist
        BDDMockito.doThrow(new ResourceNotFoundException("Not Found")).when(mockRecipeFinderService).getById(1);
        //mocks the HTTP get method at the specified url
        mockMvc.perform(get("/api/v1/recipe-finder/{id}", 1))
                //expects the status code to be a 404
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/v1/recipe-finder/1 - Success")
    public void putRecipeTestNotSuccess() throws Exception {
        //sets up a fake updated recipe
        String requestBody = "{"
                + "\"id\": 1,"
                + "\"name\": \"After Update Recipe\","
                + "\"ingredients\": \"Ingredient 1, Ingredient 2\","
                + "\"steps\": \"Step 1, Step 2\","
                + "\"user\": \"John Doe\","
                + "\"rating\": 4"
                + "}";

        //mocks the HTTP put method at the specified url
        mockMvc.perform(put("/api/v1/recipe-finder/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        //expects the status code to be a 200
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/v1/recipe-finder/1 - Not Found")
    public void putRecipeTestNotFound() throws Exception {
        //throws an exception that says that the id of the recipe trying to be updated couldn't be found/doesn't exist
        BDDMockito.doThrow(new ResourceNotFoundException("Not Found")).when(mockRecipeFinderService).update(any(), any());
        //mocks the HTTP put method at the specified url
        mockMvc.perform(put("/api/v1/recipe-finder/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                        //expects the status code to be a 404
                        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/recipe-finder/1 - Success")
    public void deleteRecipeTestNotSuccess() throws Exception {
        //mocks the method to delete a recipe
        BDDMockito.doReturn(true).when(mockRecipeFinderService).delete(any());
        //mocks the HTTP delete method at the specified url
        mockMvc.perform(delete("/api/v1/recipe-finder/{id}", 1))
                //expects the status code to be a 204
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/recipe-finder/1 - Not Found")
    public void deleteRecipeTestNotFound() throws Exception{
        //throws an exception that says that the id of the recipe trying to be deleted couldn't be found/doesn't exist
        BDDMockito.doThrow(new ResourceNotFoundException("Not Found")).when(mockRecipeFinderService).delete(any());
        //mocks the HTTP delete method at the specified url
        mockMvc.perform(delete("/api/v1/recipe-finder/{id}", 1))
                //expects the status code to be a 204
                .andExpect(status().isNotFound());
    }
}
