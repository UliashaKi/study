package ru.mtuci.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.demo.exception.EntityNotFoundException;
import ru.mtuci.demo.model.entity.Product;
import ru.mtuci.demo.service.ProductService;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/products")
@RestController
public class ProductController {

  private final ProductService productService;

  @PutMapping
  public ResponseEntity<?> saveProduct(@RequestBody Product product) {
    return ResponseEntity.ok(productService.saveProduct(product));
  }

  @GetMapping
  public ResponseEntity<?> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable long id) throws EntityNotFoundException {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<?> getProductByName(@PathVariable String name) throws EntityNotFoundException {
    return ResponseEntity.ok(productService.getProductByName(name));
  }

  @DeleteMapping("/{id}")
  public void removeProductById(@PathVariable long id) {
    productService.removeProductById(id);
  }
}
