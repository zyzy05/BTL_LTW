let loadedData = {
    products: false,
    categories: false,
    orders: false,
    users: false
};

// Handle Tab Switching
function switchTab(tabId) {
    // Update active class on nav
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    event.currentTarget.classList.add('active');

    // Show correct section
    document.querySelectorAll('.tab-section').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tabId}`).classList.add('active');

    // Trigger data fetch if not loaded
    if (tabId === 'products' && !loadedData.products) loadProducts();
    if (tabId === 'categories' && !loadedData.categories) loadCategories();
    // Orders and Users are highly restricted, might need /api/admin/ endpoints
    if (tabId === 'orders' && !loadedData.orders) loadOrders();
    if (tabId === 'users' && !loadedData.users) loadUsers();
}

// FORMAT PRICE Helper
function formatPrice(priceNum) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(priceNum || 0);
}

/* ==================
   PRODUCTS MODULE (Đáp ứng yêu cầu: Name, Price, Cột bảng)
================== */
async function loadProducts() {
    const tbody = document.getElementById('tb-products');
    try {
        // Can use public /products or admin /admin/products depending on backend. We try /products
        const res = await fetchAdminAPI('/products');
        const products = res.data || [];
        
        let html = '';
        if (products.length === 0) {
            html = '<tr><td colspan="5" style="text-align:center;">Kho sản phẩm trống.</td></tr>';
        } else {
            products.forEach(p => {
                let mainImg = 'https://via.placeholder.com/50';
                if (p.images && p.images.length > 0) {
                    let imgObj = p.images.find(img => img.isMain) || p.images[0];
                    mainImg = imgObj.imageUrl.startsWith('http') ? imgObj.imageUrl : `../images/${imgObj.imageUrl}`;
                }
                const catName = p.category ? p.category.name : 'Unknown';
                
                html += `
                <tr>
                    <td>
                        <div class="td-flex">
                            <img src="${mainImg}" class="product-tb-img" alt="img">
                            <span class="item-name">${p.name}</span>
                        </div>
                    </td>
                    <td style="font-weight: 600; color: var(--success);">${formatPrice(p.price)}</td>
                    <td><span class="badge badge-warning">${catName}</span></td>
                    <td>${p.createdAt ? p.createdAt.split('T')[0] : 'N/A'}</td>
                    <td>
                        <button class="act-btn" onclick="alert('Sửa SP ${p.id}')">Sửa</button>
                        <button class="act-btn delete" onclick="alert('Xóa SP ${p.id}')">Xóa</button>
                    </td>
                </tr>
                `;
            });
        }
        tbody.innerHTML = html;
        loadedData.products = true;
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="5" style="color:red; text-align:center;">Lỗi tải dữ liệu: ${err.message}</td></tr>`;
    }
}

/* ==================
   CATEGORIES MODULE
================== */
async function loadCategories() {
    const tbody = document.getElementById('tb-categories');
    try {
        const res = await fetchAdminAPI('/categories'); // assumption backend has this
        const categories = res.data || [];
        let html = '';
        if (categories.length === 0) {
            html = '<tr><td colspan="3" style="text-align:center;">Chưa có danh mục.</td></tr>';
        } else {
            categories.forEach(c => {
                html += `
                <tr>
                    <td>#${c.id}</td>
                    <td class="item-name">${c.name}</td>
                    <td>
                        <button class="act-btn">Sửa</button>
                    </td>
                </tr>`;
            });
        }
        tbody.innerHTML = html;
        loadedData.categories = true;
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="3" style="color:red; text-align:center;">Không thể tải Danh mục (vui lòng kiểm tra Admin endpoint)</td></tr>`;
    }
}

/* ==================
   ORDERS MODULE
================== */
async function loadOrders() {
    const tbody = document.getElementById('tb-orders');
    try {
        const res = await fetchAdminAPI('/admin/orders'); // highly likely secured
        const orders = res.data || [];
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding: 20px;">
        Chức năng đang bảo trì hoặc chưa có đơn hàng.<br> (Endpoint /api/admin/orders)</td></tr>`;
        loadedData.orders = true;
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="5" style="color:red; text-align:center;">Lỗi phân quyền hoặc 404: Đã khóa bởi Spring Security Admin.</td></tr>`;
    }
}

/* ==================
   USERS MODULE
================== */
async function loadUsers() {
    const tbody = document.getElementById('tb-users');
    try {
        const res = await fetchAdminAPI('/admin/users'); // secured
        const users = res.data || [];
        tbody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding: 20px;">
        Giao diện Quản lý Users.<br> (Cần phân quyền ROLE_ADMIN để fetch từ /api/admin/users)</td></tr>`;
        loadedData.users = true;
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="4" style="color:red; text-align:center;">Không có quyền truy cập Dữ liệu Người dùng (Lỗi Xác thực).</td></tr>`;
    }
}
