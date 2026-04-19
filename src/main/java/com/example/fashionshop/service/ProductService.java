package com.example.fashionshop.service;


import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.entity.*;
import com.example.fashionshop.repository.CategoryRepository;
import com.example.fashionshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    // hiển thi toàn bộ sản phẩm
    public List<ProductsResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }
        return productsResponses;
    }

    // hiển thị chi tiết san pham
    public ProductsResponse getProductDetail(long id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductsResponse productsResponse = new ProductsResponse();

        productsResponse.setId(product.getId());
        productsResponse.setName(product.getName());
        productsResponse.setPrice(product.getPrice());
        productsResponse.setDescription(product.getDescription());

        ProductsResponse request = new ProductsResponse();

        request.setId(product.getId());
        request.setName(product.getName());
        request.setPrice(product.getPrice());
        request.setDescription(product.getDescription());
        request.setPrice(product.getPrice());


        List<ProductImageResponse> productImageResponses = new ArrayList<>();
        for (ProductImage image : product.getImages()) {
            ProductImageResponse productImageResponse = new ProductImageResponse();
            productImageResponse.setImageUrl(image.getImageUrl());
            productImageResponses.add(productImageResponse);

        }
        List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
        for (ProductVariant variant : product.getVariants()) {
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
            productVariantResponse.setId(variant.getId());
            Size size = variant.getSize();
            SizeResponse sizeResponse = new SizeResponse();
            sizeResponse.setName(size.getName());

            productVariantResponse.setSize(sizeResponse);
            productVariantResponse.setStockQuantity(variant.getStockQuantity());
            productVariantResponses.add(productVariantResponse);
        }

        request.setProductVariants(productVariantResponses);
        request.setImages(productImageResponses);

        return request;
    }

    // loc san pham theo muc gia
    public List<ProductsResponse> filterPrice(double minPrice, double maxPrice) {
        System.out.println(minPrice);
        System.out.println(maxPrice);
        List<Product> products = productRepository.findByPriceBetweenOrderByPriceAsc(minPrice, maxPrice);
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }

        return productsResponses;
    }

    // tim kiem san pham theo ten
    public List<ProductsResponse> filterName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

            request.setId(product.getId());
            request.setName(product.getName());
            request.setPrice(product.getPrice());
            request.setDescription(product.getDescription());
            request.setPrice(product.getPrice());


            List<ProductImageResponse> productImageResponses = new ArrayList<>();
            for (ProductImage image : product.getImages()) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(image.getImageUrl());
                productImageResponses.add(productImageResponse);

            }
            List<ProductVariantResponse> productVariantResponses = new ArrayList<>();
            for (ProductVariant variant : product.getVariants()) {
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setId(variant.getId());
                Size size = variant.getSize();
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());

                productVariantResponse.setSize(sizeResponse);
                productVariantResponse.setStockQuantity(variant.getStockQuantity());
                productVariantResponses.add(productVariantResponse);
            }

            request.setProductVariants(productVariantResponses);
            request.setImages(productImageResponses);
            productsResponses.add(request);
        }
        return productsResponses;

    }
    // them san pham
    public boolean addProduct(ProductsResponse productsResponse) {
        Product product = new Product();
        product.setName(productsResponse.getName());
        product.setDescription(productsResponse.getDescription());
        product.setPrice(productsResponse.getPrice());
        product.setCreatedAt(LocalDateTime.now());

        Category category = categoryRepository.findById(productsResponse.getCategoryId()).orElse(null);

        product.setCategory(category);

        try
        {
            productRepository.save(product);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    // sua san pham
    public boolean updateProduct(long id , ProductsResponse productsResponse) {
        Product product = productRepository.findById(id).orElse(null);
        product.setName(productsResponse.getName());
        product.setDescription(productsResponse.getDescription());
        product.setPrice(productsResponse.getPrice());
        product.setCreatedAt(LocalDateTime.now());

        Category category = categoryRepository.findById(productsResponse.getCategoryId()).orElse(null);

        product.setCategory(category);

        try
        {
            productRepository.save(product);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    // xoa san pham
    public boolean deleteProduct(long id) {

        try
        {
            productRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
