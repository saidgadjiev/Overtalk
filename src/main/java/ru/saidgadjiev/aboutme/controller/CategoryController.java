package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.annotation.JsonView;
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
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.aboutme.model.JsonViews;
import ru.saidgadjiev.aboutme.service.BlogService;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import javax.validation.Valid;
import java.sql.SQLException;
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
    public ResponseEntity<CategoryDetails> create(
            @JsonView(JsonViews.Rest.class) @RequestBody @Valid CategoryDetails categoryDetails,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Category category = blogService.createCategory(categoryDetails);

        return ResponseEntity.ok(DTOUtils.convert(category, CategoryDetails.class));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> update(
            @PathVariable("id") Integer id,
            @JsonView(JsonViews.Rest.class) @RequestBody @Valid CategoryDetails categoryDetails,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Category category = blogService.updateCategory(id, categoryDetails);

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        ObjectNode response = new CategoryJsonBuilder()
                .name(category.getName())
                .description(category.getDescription())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "")
    public ResponseEntity<Page<CategoryDetails>> getCategories(
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        Page<Category> result = blogService.getCategories(page);
        List<CategoryDetails> converted = DTOUtils.convert(result.getContent(), CategoryDetails.class);

        return ResponseEntity.ok(new PageImpl<>(converted, page, result.getTotalElements()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDetails> getCategory(@PathVariable("id") Integer id) throws SQLException {
        Category category = blogService.getCategoryById(id);

        return ResponseEntity.ok(DTOUtils.convert(category, CategoryDetails.class));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deleteCategoryById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
