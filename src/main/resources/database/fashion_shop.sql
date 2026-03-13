    use fashion_shop;
    CREATE TABLE users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255),
        email VARCHAR(255),
        password VARCHAR(255),
        full_name VARCHAR(255),
        phone VARCHAR(20),
        role VARCHAR(50),
        status VARCHAR(50),
        created_at DATETIME
    );

    CREATE TABLE addresses (
        id INT AUTO_INCREMENT PRIMARY KEY,
        address_line VARCHAR(255),
        city VARCHAR(255),
        district VARCHAR(255),
        ward VARCHAR(255),
        user_id INT UNIQUE,
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

    CREATE TABLE carts (
        id INT AUTO_INCREMENT PRIMARY KEY,
        created_at DATETIME,
        user_id INT UNIQUE,
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

    CREATE TABLE categories (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255),
        description VARCHAR(255)
    );

    CREATE TABLE sizes (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(50)
    );

    CREATE TABLE products (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255),
        description TEXT,
        price DOUBLE,
        created_at DATETIME,
        category_id INT,
        FOREIGN KEY (category_id) REFERENCES categories(id)
    );

    CREATE TABLE product_images (
        id INT AUTO_INCREMENT PRIMARY KEY,
        image_url VARCHAR(255),
        is_main BOOLEAN,
        product_id INT,
        FOREIGN KEY (product_id) REFERENCES products(id)
    );

    CREATE TABLE product_variants (
        id INT AUTO_INCREMENT PRIMARY KEY,
        stock_quantity INT,
        product_id INT,
        size_id INT,
        FOREIGN KEY (product_id) REFERENCES products(id),
        FOREIGN KEY (size_id) REFERENCES sizes(id)
    );

    CREATE TABLE cart_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        quantity INT,
        cart_id INT,
        product_variant_id INT,
        FOREIGN KEY (cart_id) REFERENCES carts(id),
        FOREIGN KEY (product_variant_id) REFERENCES product_variants(id)
    );

    CREATE TABLE orders (
        id INT AUTO_INCREMENT PRIMARY KEY,
        total_price DOUBLE,
        status VARCHAR(50),
        payment_method VARCHAR(50),
        payment_status VARCHAR(50),
        shipping_name VARCHAR(255),
        shipping_phone VARCHAR(20),
        shipping_address VARCHAR(255),
        created_at DATETIME,
        user_id INT,
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

    CREATE TABLE order_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        price DOUBLE,
        quantity INT,
        order_id INT,
        product_variant_id INT,
        FOREIGN KEY (order_id) REFERENCES orders(id),
        FOREIGN KEY (product_variant_id) REFERENCES product_variants(id)
    );