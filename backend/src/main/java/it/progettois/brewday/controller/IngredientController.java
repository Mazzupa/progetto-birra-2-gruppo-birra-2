package it.progettois.brewday.controller;

import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.EmptyStorageException;
import it.progettois.brewday.common.exception.IngredientNotFoundException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static it.progettois.brewday.common.constant.SecurityConstants.HEADER_STRING;

@RestController
public class IngredientController {

    private final IngredientService ingredientService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public IngredientController(IngredientService ingredientService, JwtTokenUtil jwtTokenUtil) {
        this.ingredientService = ingredientService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //get all brewer ingredients
    @GetMapping("/ingredient")
    public ResponseEntity<?> getIngredients(HttpServletRequest request) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        List<IngredientDto> ingredients;
        try{
            ingredients = this.ingredientService.getIngredients(username);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }

        if (ingredients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ingredients found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(ingredients);
        }

    }

    //get brewer ingredient by IngredientId
    @GetMapping("/ingredient/{id}")
    public ResponseEntity<?> getIngredient(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        IngredientDto ingredientDto;
        try{
            ingredientDto = this.ingredientService.getIngredient(username, id);
            return ResponseEntity.ok(ingredientDto);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        }



    }

    @PostMapping("/ingredient")
    public ResponseEntity<?> createIngredient(HttpServletRequest request, @RequestBody IngredientDto ingredientDto) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            return ResponseEntity.status(HttpStatus.OK).body(this.ingredientService.createIngredient(ingredientDto, username));
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }

    }

    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<?> deleteIngredient(HttpServletRequest request, @PathVariable("id") Integer id) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            this.ingredientService.deleteIngredient(username, id);
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient was deleted successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }

    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<?> editIngredient(HttpServletRequest request, @PathVariable("id") Integer id, @RequestBody IngredientDto ingredientDto){

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            this.ingredientService.editIngredient(username, id, ingredientDto);
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient has been updated");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        }
    }

    @GetMapping("/storage")
    public ResponseEntity<?> getStorage(HttpServletRequest request){

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try {
            List<IngredientDto> ingredientDtoList = this.ingredientService.getStorage(username);
            return ResponseEntity.status(HttpStatus.OK).body(ingredientDtoList);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer does not exist");
        } catch (EmptyStorageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/storage/{id}")
    public ResponseEntity<?> getStorageIngredient(HttpServletRequest request, @PathVariable("id") Integer ingredientId) {

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        IngredientDto ingredientDto;
        try {
            ingredientDto = this.ingredientService.getStorageIngredient(username, ingredientId);
            return ResponseEntity.ok(ingredientDto);
        } catch (BrewerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer does not exist");
        } catch (EmptyStorageException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // This is actually a setter for the quantity of an owned ingredient
    // quantity > 0 -> create / update
    // quantity = 0 -> delete
    @PutMapping("/storage/{id}")
    public ResponseEntity<?> modifyStorage(HttpServletRequest request, @PathVariable("id") Integer ingredientId, @RequestBody Double quantity){

        String username = this.jwtTokenUtil.getUsernameFromToken(request.getHeader(HEADER_STRING));

        try{
            this.ingredientService.addToStorage(username, ingredientId, quantity);
            return ResponseEntity.status(HttpStatus.OK).body("The ingredient has been updated");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The ingredient does not exist");
        } catch (BrewerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The brewer with username: " + username + " does not exist");
        } catch (InvalidPropertiesFormatException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

}
