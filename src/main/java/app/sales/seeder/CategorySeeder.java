package app.sales.seeder;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import app.sales.entity.Category;
import app.sales.repository.CategoryRepository;

@Component
public class CategorySeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Category pakaian = new Category();
            pakaian.setCategoryName("Pakaian");
            pakaian.setCreatedAt(LocalDateTime.now());
            pakaian.setCreatedBy("SYSTEM");
            categoryRepository.save(pakaian);

            Category sepatu = new Category();
            sepatu.setCategoryName("Sepatu");
            sepatu.setCreatedAt(LocalDateTime.now());
            sepatu.setCreatedBy("SYSTEM");
            categoryRepository.save(sepatu);

            Category makanan = new Category();
            makanan.setCategoryName("Makanan");
            makanan.setCreatedAt(LocalDateTime.now());
            makanan.setCreatedBy("SYSTEM");
            categoryRepository.save(makanan);

            Category minuman = new Category();
            minuman.setCategoryName("Minuman");
            minuman.setCreatedAt(LocalDateTime.now());
            minuman.setCreatedBy("SYSTEM");
            categoryRepository.save(minuman);

            Category elektronik = new Category();
            elektronik.setCategoryName("Elektronik");
            elektronik.setCreatedAt(LocalDateTime.now());
            elektronik.setCreatedBy("SYSTEM");
            categoryRepository.save(elektronik);

            System.out.println("Categories created successfully.");
        } else {
            System.out.println("Categories already exist.");
        }
    }
}