package app.sales.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.sales.dto.category.CategoryResponse;
import app.sales.entity.Category;
import app.sales.repository.CategoryRepository;
import app.sales.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryResponse(category.getCategoryId(), category.getCategoryName()))
                .collect(Collectors.toList());
    }

}
