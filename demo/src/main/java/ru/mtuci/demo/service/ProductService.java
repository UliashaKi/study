package ru.mtuci.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.NotFoundException;
import ru.mtuci.demo.model.entity.Product;
import ru.mtuci.demo.repo.ProductRepo;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepo productRepo;

  public Product saveProduct(Product product) {
    return productRepo.save(product);
  }

  public List<Product> getAllProducts() {
    return productRepo.findAll();
  }

  public Product getProductById(long id) throws NotFoundException {
    return productRepo.findById(id).orElseThrow(() -> new NotFoundException("Продукт с таким id не найден"));
  }

  public Product getProductByName(String name) throws NotFoundException {
    return productRepo.findByName(name).orElseThrow(() -> new NotFoundException("Продукт с таким именем не найден"));
  }

  public void removeProductById(long id) {
    productRepo.deleteById(id);
  }

}
