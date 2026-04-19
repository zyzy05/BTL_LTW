const API_BASE_URL = 'http://localhost:8081/api';

/**
 * Handle fetch requests and automatic JSON parsing
 */
async function fetchAPI(endpoint, options = {}) {
    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    // Include Auth Token if available (Using HTTP Basic Auth for this backend)
    const token = localStorage.getItem('jwtToken');
    if (token) {
        defaultHeaders['Authorization'] = `Basic ${token}`;
    }

    const config = {
        ...options,
        headers: {
            ...defaultHeaders,
            ...options.headers,
        },
    };

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        let data;
        try {
            data = await response.json();
        } catch {
            data = null;
        }

        if (!response.ok) {
            throw { status: response.status, data };
        }

        return data; // Assuming it returns ResponseData { success, data, message }
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Toast Notifications
function showToast(message, type = 'success') {
    let container = document.getElementById('toastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toastContainer';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerText = message;
    
    container.appendChild(toast);
    
    // Trigger animation
    setTimeout(() => {
        toast.classList.add('show');
    }, 10);

    // Remove
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Check logged-in status to update nav
function updateNav() {
    const userString = localStorage.getItem('user');
    const authLinksId = document.getElementById('auth-links');
    
    if (!authLinksId) return;

    // Base Cart Icon (always visible)
    const cartIconHtml = `
        <a href="cart.html" class="nav-cart-icon" style="position: relative; margin-right: 1.5rem; text-decoration: none; color: var(--text-main); display: flex; align-items: center;">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"></circle><circle cx="20" cy="21" r="1"></circle><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path></svg>
            <span id="cart-badge" style="position: absolute; top: -8px; right: -12px; background: var(--accent); color: white; border-radius: 50%; width: 20px; height: 20px; font-size: 11px; display: none; align-items: center; justify-content: center; font-weight: 700;">0</span>
        </a>
    `;

    if (userString) {
        try {
            const user = JSON.parse(userString);
            let adminBtn = '';
            if (user.username === 'admin') {
                adminBtn = `<a href="admin/index.html" class="btn btn-outline" style="border-color: var(--accent); color: var(--accent); margin-right: 1rem;">Admin</a>`;
            }

            authLinksId.innerHTML = `
                <div style="display: flex; align-items: center;">
                    ${cartIconHtml}
                    ${adminBtn}
                    <span style="color: var(--text-muted); font-weight: 700; margin-right: 1.5rem;">Hi, ${user.username}!</span>
                    <button onclick="logout()" class="btn btn-primary" style="padding: 0.4rem 1rem;">Logout</button>
                </div>
            `;
            updateCartCount();
        } catch (e) {
            console.error("Lỗi đọc user cache.");
        }
    } else {
        authLinksId.innerHTML = `
            <div style="display: flex; align-items: center;">
                ${cartIconHtml}
                <a href="login.html" style="text-decoration:none; color:var(--text-main); font-weight:700; margin-right: 1.5rem;">Login</a>
                <a href="register.html" class="btn btn-primary">Register</a>
            </div>
        `;
    }
}

async function updateCartCount() {
    const userString = localStorage.getItem('user');
    const badge = document.getElementById('cart-badge');
    if (!userString || !badge) return;

    try {
        const user = JSON.parse(userString);
        // Correct endpoint for cart using Auth session
        const cartData = await fetchAPI('/user/cart');
        const count = cartData.items ? cartData.items.length : 0;
        badge.innerText = count;
        badge.style.display = count > 0 ? 'flex' : 'none';
    } catch (error) {
        console.error('Error updating cart count:', error);
    }
}

function logout() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('user');
    showToast('Đã đăng xuất thành công', 'success');
    updateNav();
    setTimeout(() => {
        window.location.reload();
    }, 800);
}

// Format price payload
function formatPrice(price) {
    if(!price) return "0 đ";
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
}

// Attach load to modify UI
document.addEventListener('DOMContentLoaded', () => {
    updateNav();
});
