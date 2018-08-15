package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.json.CategoryJsonBuilder;
import ru.saidgadjiev.aboutme.model.CategoryRequest;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<ObjectNode> create(
            @RequestBody @Valid CategoryRequest categoryRequest, BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Category category = blogService.createCategory(categoryRequest);
        ObjectNode response = toJson(category);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> update(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CategoryRequest categoryRequest, BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        int count = blogService.updateCategory(id, categoryRequest);

        if (count == 0) {
            return ResponseEntity.notFound().build();
        }

        ObjectNode objectNode = new CategoryJsonBuilder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .build();


        return ResponseEntity.ok(objectNode);
    }

    @GetMapping(value = "")
    public ResponseEntity<Page<ObjectNode>> getCategories(
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        List<ObjectNode> content = new ArrayList<>();

        for (Category category: blogService.getCategoriesList(page.getPageSize(), page.getOffset())) {
            content.add(toJson(category));
        }

        return ResponseEntity.ok(new PageImpl<>(content, page, blogService.categoryCountOff()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ObjectNode> getCategory(@PathVariable("id") Integer id) throws SQLException {
        Category category = blogService.getCategoryById(id);

        return ResponseEntity.ok(toJson(category));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deleteCategoryById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    private ObjectNode toJson(Category category) {
        return new CategoryJsonBuilder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .postsCount(category.getPosts().size())
                .build();
    }
}
