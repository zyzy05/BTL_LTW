/**
 * Handles fetching and rendering of products 
 */

async function loadProducts(containerId, limit = null) {
    const container = document.getElementById(containerId);
    if (!container) return;

    try {
        const response = await fetchAPI('/products');
        // Structure expected: { data: [...] }
        const products = response.data || [];
        
        let displayProducts = products;
        if (limit) {
            displayProducts = products.slice(0, limit);
        }

        renderProducts(displayProducts, container);

    } catch (error) {
        console.error("Lỗi tải sản phẩm:", error);
        container.innerHTML = `<div style="text-align:center; color: red;">Xin lỗi, không thể tải danh sách sản phẩm.</div>`;
    }
}

function renderProducts(products, container) {
    container.innerHTML = ''; // Clear loading state
    
    if (products.length === 0) {
        container.innerHTML = '<div style="text-align:center; color: var(--text-muted); width: 100%; grid-column: 1 / -1; padding: 3rem;">Không tìm thấy sản phẩm nào phù hợp.</div>';
        return;
    }

    products.forEach(product => {
        // Extract main image from DB relationship
        let mainImg = "https://via.placeholder.com/400x500?text=No+Image";
        if (product.images && product.images.length > 0) {
            const foundMain = product.images.find(img => img.isMain);
            let imgUrl = foundMain ? foundMain.imageUrl : product.images[0].imageUrl;
            mainImg = imgUrl.startsWith('http') ? imgUrl : `images/${imgUrl}`;
        }

        const catName = product.category ? product.category.name : 'Thời trang';
        const formattedPrice = formatPrice(product.price);

        const card = document.createElement('div');
        card.className = 'product-card';
        card.innerHTML = `
            <div class="product-img-wrapper">
                <img src="${mainImg}" alt="${product.name}" class="product-img" onerror="this.src='https://via.placeholder.com/400x500?text=Error'">
                <button class="quick-view-btn" onclick="showQuickView(${product.id})">🔍 Xem nhanh</button>
            </div>
            <div class="product-category">${catName}</div>
            <h3 class="product-name" title="${product.name}">${product.name}</h3>
            <div class="product-price">${formattedPrice}</div>
            <button class="btn btn-primary" style="width: 100%; margin-top: 1rem; border-radius:4px" onclick="showQuickView(${product.id})">Mua ngay</button>
        `;
        
        container.appendChild(card);
    });
}

async function addToCart(productId, variantId, qty = 1) {
    if (!localStorage.getItem('jwtToken')) {
        showToast('Vui lòng đăng nhập để mua hàng', 'error');
        setTimeout(() => window.location.href = 'login.html', 1500);
        return;
    }
    
    if (!variantId) {
        // Fallback if no variant provided
        showToast('Vui lòng chọn Size!', 'warning');
        return;
    }

    try {
        await fetchAPI(`/user/cart/items?productId=${productId}&variantId=${variantId}&quantity=${qty}`, {
            method: 'POST'
        });
        showToast(`Đã thêm vào giỏ hàng!`, 'success');
        updateCartCount(); 
    } catch (error) {
        console.error(error);
        showToast('Lỗi khi thêm vào giỏ hàng', 'error');
    }
}

// Quick View Modal JS
window.showQuickView = async function(productId) {
    try {
        const res = await fetchAPI(`/products/${productId}`);
        const product = res.data;
        if (!product) return;

        let modal = document.getElementById('quick-view-modal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'quick-view-modal';
            modal.className = 'modal-overlay';
            document.body.appendChild(modal);
        }

        const images = product.images || [];
        const mainImg = images.length > 0 ? (images[0].imageUrl.startsWith('http') ? images[0].imageUrl : `images/${images[0].imageUrl}`) : "https://via.placeholder.com/400x500";
        
        const thumbHtml = images.map((img, i) => `
            <img src="${img.imageUrl.startsWith('http') ? img.imageUrl : 'images/'+img.imageUrl}" 
                 class="modal-thumb ${i===0?'active':''}" 
                 onclick="changeModalMainImg(this)">
        `).join('');

        const variants = product.productVariants || [];
        const sizeHtml = variants.map((v, i) => `
            <div class="size-item ${i===0?'active':''}" data-variant-id="${v.id}" onclick="selectVariant(this)">
                ${v.size ? v.size.name : 'FreeSize'}
            </div>
        `).join('');

        modal.innerHTML = `
            <div class="modal-content">
                <span class="modal-close" onclick="closeModal()">&times;</span>
                <div class="modal-left">
                    <img id="modal-main-img" src="${mainImg}" class="modal-img-main" onerror="this.src='https://via.placeholder.com/400x500'">
                    <div class="modal-thumbnails">${thumbHtml}</div>
                </div>
                <div class="modal-right">
                    <div class="product-category" style="margin-bottom: 0.5rem; color: var(--accent); font-weight: 700;">${product.category ? product.category.name : 'LUXE FW KIDS'}</div>
                    <h2 style="font-size: 2.2rem; margin-bottom: 1rem; text-transform: none; letter-spacing: 0;">${product.name}</h2>
                    <div class="product-price" style="font-size: 1.8rem; margin-bottom: 2rem; color: var(--accent);">${formatPrice(product.price)}</div>
                    
                    <p style="color: var(--text-muted); margin-bottom: 2rem; line-height: 1.8; font-size: 1.05rem;">
                        ${product.description || 'Dòng sản phẩm thời trang cao cấp với chất liệu tự nhiên, an toàn tuyệt đối cho làn da bé, mang lại vẻ ngoài sành điệu và tự tin.'}
                    </p>

                    <div style="font-weight: 700; margin-bottom: 1rem; border-top: 1px solid var(--border); padding-top: 1.5rem;">CHỌN KÍCH CỠ:</div>
                    <div class="size-grid">${sizeHtml}</div>

                    <div style="font-weight: 700; margin-bottom: 1rem;">SỐ LƯỢNG:</div>
                    <div class="qty-box">
                        <input type="number" id="quick-qty" class="qty-input" value="1" min="1">
                    </div>

                    <button class="btn btn-primary" style="width: 100%; padding: 1.5rem; font-size: 1.2rem; border-radius: var(--radius-sm);" 
                            onclick="addToCartFromModal(${product.id})">
                        THÊM VÀO GIỎ HÀNG
                    </button>
                </div>
            </div>
        `;

        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; 

    } catch (err) {
        console.error(err);
        showToast("Không thể tải chi tiết sản phẩm.", "error");
    }
}

window.closeModal = function() {
    const modal = document.getElementById('quick-view-modal');
    if (modal) modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

window.changeModalMainImg = function(thumb) {
    const main = document.getElementById('modal-main-img');
    if (main) main.src = thumb.src;
    document.querySelectorAll('.modal-thumb').forEach(t => t.classList.remove('active'));
    thumb.classList.add('active');
}

window.selectVariant = function(el) {
    document.querySelectorAll('.size-item').forEach(i => i.classList.remove('active'));
    el.classList.add('active');
}

window.addToCartFromModal = function(productId) {
    const activeSize = document.querySelector('.size-item.active');
    const variantId = activeSize ? activeSize.getAttribute('data-variant-id') : null;
    const qty = document.getElementById('quick-qty').value;
    
    addToCart(productId, variantId, qty);
    closeModal();
}

// Search & Filter
window.handleSearch = async function() {
    const input = document.getElementById('search-input');
    const name = input ? input.value.trim() : "";
    const grid = document.getElementById('products-grid') || document.getElementById('new-products');
    
    if (!grid) return;
    grid.innerHTML = '<div style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 3rem;">Đang tìm kiếm...</div>';

    try {
        const res = await fetchAPI(`/products/search?name=${encodeURIComponent(name)}`);
        renderProducts(res.data || [], grid);
    } catch (err) {
        showToast("Lỗi tìm kiếm sản phẩm", "error");
    }
}

window.applyFilters = async function() {
    const priceRadio = document.querySelector('input[name="price"]:checked');
    const grid = document.getElementById('products-grid');
    if (!grid || !priceRadio) return;

    grid.innerHTML = '<div style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 3rem;">Đang lọc sản phẩm...</div>';
    
    let minPrice = 0;
    let maxPrice = 999999999;

    if (priceRadio.value === 'under100') maxPrice = 100000;
    else if (priceRadio.value === '100to200') { minPrice = 100000; maxPrice = 200000; }
    else if (priceRadio.value === 'over200') minPrice = 200000;

    try {
        const res = await fetchAPI(`/products/filter-price?minPrice=${minPrice}&maxPrice=${maxPrice}`);
        renderProducts(res.data || [], grid);
        showToast("Đã áp dụng bộ lọc giá.", "success");
    } catch (err) {
        showToast("Lỗi khi lọc sản phẩm.", "error");
    }
}
