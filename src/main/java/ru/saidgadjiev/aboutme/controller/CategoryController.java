package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;

/**
 * Created by said on 12.02.2018.
 */
@RestController
@RequestMapping(value = "/api/category")
public class CategoryController {

    @Autowired
    private BlogService blogService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity create(
            @RequestBody @Valid CategoryDetails categoryDetails, BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        blogService.createCategory(categoryDetails);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public ResponseEntity<CategoryDetails> update(
            @RequestBody @Valid CategoryDetails categoryDetails, BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors() || categoryDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        int count = blogService.updateCategory(categoryDetails);

        return ResponseEntity.ok(categoryDetails);
    }

    @GetMapping(value = "")
    public ResponseEntity<Page<CategoryDetails>> getCategories(
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        Page<CategoryDetails> categories = blogService.getCategories(page);

        return ResponseEntity.ok(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDetails> getCategory(@PathVariable("id") Integer id) throws SQLException {
        CategoryDetails categoryDetails = blogService.getCategoryById(id);

        return new ResponseEntity<>(categoryDetails, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<CategoryDetails> deleteCategory(@PathVariable("id") Integer id) throws SQLException {
        blogService.deleteCategoryById(id);

        return ResponseEntity.ok().build();
    }
}
