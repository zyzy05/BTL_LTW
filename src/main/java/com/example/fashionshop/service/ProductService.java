package com.example.fashionshop.service;


import com.example.fashionshop.dto.response.ProductImageResponse;
import com.example.fashionshop.dto.response.ProductVariantResponse;
import com.example.fashionshop.dto.response.ProductsResponse;
import com.example.fashionshop.dto.response.SizeResponse;
import com.example.fashionshop.entity.Product;
import com.example.fashionshop.entity.ProductImage;
import com.example.fashionshop.entity.ProductVariant;
import com.example.fashionshop.entity.Size;
import com.example.fashionshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductsResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<ProductsResponse> productsResponses = new ArrayList<>();

        for (Product product : products) {
            ProductsResponse request = new ProductsResponse();

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
}
