package app.sales.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.sales.dto.ApiResponse;
import app.sales.dto.category.CategoryResponse;
import app.sales.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>();
        response.setData(categories);
        response.setMessage("Categories retrieved successfully");
        response.setStatusCode(200);
        response.setStatus("Success");
        return ResponseEntity.ok(response);
    }

}
