package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface BrandRepo extends JpaRepository<Brand, Long> {
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
