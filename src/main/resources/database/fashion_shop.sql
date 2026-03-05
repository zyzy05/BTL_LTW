-- =====================================
-- DATABASE: FASHION SHOP
-- =====================================

DROP DATABASE IF EXISTS fashion_shop;
CREATE DATABASE fashion_shop;
USE fashion_shop;

-- =====================================
-- 1. USERS
-- =====================================

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    role ENUM('ADMIN','CUSTOMER') DEFAULT 'CUSTOMER',
    status ENUM('ACTIVE','LOCKED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================
-- 2. ADDRESSES
-- =====================================

CREATE TABLE addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    address_line VARCHAR(255),
    city VARCHAR(100),
    district VARCHAR(100),
    ward VARCHAR(100),

    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

-- =====================================
-- 3. CATEGORIES
-- =====================================

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- =====================================
-- 4. PRODUCTS
-- =====================================

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON DELETE SET NULL
);

-- =====================================
-- 5. PRODUCT IMAGES
-- =====================================

CREATE TABLE product_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    image_url VARCHAR(255),
    is_main BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
);

-- =====================================
-- 6. SIZES
-- =====================================

CREATE TABLE sizes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

-- =====================================
-- 7. PRODUCT VARIANTS
-- =====================================

CREATE TABLE product_variants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    size_id INT,
    stock_quantity INT DEFAULT 0,

    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE,

    FOREIGN KEY (size_id)
    REFERENCES sizes(id)
    ON DELETE CASCADE
);

-- =====================================
-- 8. CARTS
-- =====================================

CREATE TABLE carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
);

-- =====================================
-- 9. CART ITEMS
-- =====================================

CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    product_variant_id INT,
    quantity INT NOT NULL,

    FOREIGN KEY (cart_id)
    REFERENCES carts(id)
    ON DELETE CASCADE,

    FOREIGN KEY (product_variant_id)
    REFERENCES product_variants(id)
);

-- =====================================
-- 10. ORDERS
-- =====================================

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,

    total_price DECIMAL(10,2),

    status ENUM(
        'PENDING',
        'CONFIRMED',
        'SHIPPING',
        'COMPLETED',
        'CANCELLED'
    ) DEFAULT 'PENDING',

    payment_method ENUM('COD') DEFAULT 'COD',
    payment_status ENUM('PENDING','PAID') DEFAULT 'PENDING',

    shipping_name VARCHAR(100),
    shipping_phone VARCHAR(20),
    shipping_address VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
    REFERENCES users(id)
);

-- =====================================
-- 11. ORDER ITEMS
-- =====================================

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_variant_id INT,
    price DECIMAL(10,2),
    quantity INT,

    FOREIGN KEY (order_id)
    REFERENCES orders(id)
    ON DELETE CASCADE,

    FOREIGN KEY (product_variant_id)
    REFERENCES product_variants(id)
);