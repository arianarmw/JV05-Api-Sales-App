package app.sales.service;

import java.util.List;

import app.sales.dto.ApiResponse;
import app.sales.dto.category.CategoryResponse;

public interface CategoryService {
    ApiResponse<List<CategoryResponse>> getAllCategories();
}
