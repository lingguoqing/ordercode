const API_BASE_URL = 'http://localhost:9635/api'; // 后端API地址

// DOM 元素引用
const authSection = document.getElementById('auth-section');
const carSection = document.getElementById('car-section');
const registerUsernameInput = document.getElementById('register-username');
const registerPasswordInput = document.getElementById('register-password');
const registerEmailInput = document.getElementById('register-email');
const registerPhoneInput = document.getElementById('register-phone');
const registerButton = document.getElementById('register-button');
const loginUsernameInput = document.getElementById('login-username');
const loginPasswordInput = document.getElementById('login-password');
const loginButton = document.getElementById('login-button');
const logoutButton = document.getElementById('logout-button');

const carsContainer = document.getElementById('cars-container');
const brandsContainer = document.getElementById('brands-container');
const modelsContainer = document.getElementById('models-container');
const favoritesContainer = document.getElementById('favorites-container');
const appointmentsContainer = document.getElementById('appointments-container');
const carDetailContainer = document.getElementById('car-detail-container');
const singleCarItemDisplay = document.getElementById('single-car-item-display');
const registerForm = document.getElementById('register-form');
const loginForm = document.getElementById('login-form');
const showRegisterFormButton = document.getElementById('show-register-form-button');
const showLoginFormButton = document.getElementById('show-login-form-button');
const appointmentFormContainer = document.getElementById('appointment-form-container');
const appointmentCarIdInput = document.getElementById('appointment-car-id');
const appointmentDateInput = document.getElementById('appointment-date');
const appointmentTimeInput = document.getElementById('appointment-time');
const appointmentContactInput = document.getElementById('appointment-contact');
const submitAppointmentButton = document.getElementById('submit-appointment-button');
const cancelAppointmentFormButton = document.getElementById('cancel-appointment-form-button');
const paginationControls = document.getElementById('pagination-controls');
const prevPageButton = document.getElementById('prev-page');
const nextPageButton = document.getElementById('next-page');
const pageInfoSpan = document.getElementById('page-info');
const brandPrevPageButton = document.getElementById('brand-prev-page');
const brandNextPageButton = document.getElementById('brand-next-page');
const brandPageInfoSpan = document.getElementById('brand-page-info');
const modelPrevPageButton = document.getElementById('model-prev-page');
const modelNextPageButton = document.getElementById('model-next-page');
const modelPageInfoSpan = document.getElementById('model-page-info');

let currentPage = 1; // 当前页码
const pageSize = 10; // 每页显示的车辆数量
let totalPages = 1; // 总页数
let currentFilter = 'all'; // 当前的过滤类型：'all', 'brand', 'model'
let currentFilterId = null; // 当前过滤的ID (品牌ID或车型ID)

let jwtToken = localStorage.getItem('jwtToken'); // 从本地存储获取JWT Token
let currentUserId = null; // 存储当前登录用户的ID，用于获取收藏和预约

// 获取模态框相关的DOM元素
const messageModal = document.getElementById('message-modal');
const modalMessageText = document.getElementById('modal-message-text');
const modalCloseButton = messageModal.querySelector('.modal-close-button');

// 解析JWT Token获取Payload
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("Error parsing JWT: ", e);
        return null;
    }
}

// 更新UI以反映认证状态
function updateUIForAuthStatus() {
    if (jwtToken) {
        logoutButton.style.display = 'inline-block'; // 登录后显示注销按钮
    } else {
        logoutButton.style.display = 'none'; // 未登录时隐藏注销按钮
    }
}

// 显示消息函数
function displayMessage(text, type) {
    modalMessageText.textContent = text;
    modalMessageText.className = `modal-message ${type}`;
    messageModal.classList.add('visible'); // 显示模态框

    // 3秒后自动关闭模态框
    setTimeout(() => {
        messageModal.classList.remove('visible');
    }, 3000);
}

// 关闭模态框事件监听器
if (modalCloseButton) {
    modalCloseButton.addEventListener('click', () => {
        messageModal.classList.remove('visible');
    });
}

// 切换显示区域
function showSection(sectionId) {
    // Always hide all tab-content sections first
    document.querySelectorAll('.tab-content').forEach(tabContent => {
        tabContent.style.display = 'none';
    });

    // Always hide car detail container and appointment form container first
    if (carDetailContainer) {
        carDetailContainer.style.display = 'none';
    }
    if (appointmentFormContainer) {
        appointmentFormContainer.style.display = 'none';
    }

    // Hide pagination controls by default
    if (paginationControls) {
        paginationControls.style.display = 'none';
    }

    // Handle authentication sections
    if (sectionId === 'register' || sectionId === 'login') {
        authSection.style.display = 'block';
        carSection.style.display = 'none';

        registerForm.style.display = 'none';
        loginForm.style.display = 'none';
        showRegisterFormButton.classList.remove('active');
        showLoginFormButton.classList.remove('active');

        if (sectionId === 'register') {
            registerForm.style.display = 'block';
            showRegisterFormButton.classList.add('active');
        } else {
            loginForm.style.display = 'block';
            showLoginFormButton.classList.add('active');
        }
    } else { // Handle car-related sections (tabs or car detail)
        authSection.style.display = 'none';
        carSection.style.display = 'block';

        // Check if the sectionId corresponds to a tab-content or car detail
        const targetElement = document.getElementById(sectionId);
        if (targetElement) {
            targetElement.style.display = 'block';

            // Special handling for cars-tab to show pagination
            if (sectionId === 'cars-tab' && paginationControls) {
                paginationControls.style.display = 'flex';
            }
        }
    }
}

// 渲染分页控件
function renderPaginationControls() {
    if (!paginationControls) return; // 确保分页控件存在

    pageInfoSpan.textContent = `第 ${currentPage} 页 / 共 ${totalPages} 页`;
    prevPageButton.disabled = (currentPage <= 1);
    nextPageButton.disabled = (currentPage >= totalPages);
}

// 注册用户
async function registerUser() {
    const username = registerUsernameInput.value;
    const password = registerPasswordInput.value;
    const email = registerEmailInput.value;
    const phone = registerPhoneInput.value;

    try {
        const response = await fetch(`${API_BASE_URL}/user/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password, email, phone })
        });
        const data = await response.json();
        if (data.success) {
            displayMessage(data.message, 'success');
            // 清空注册表单
            registerUsernameInput.value = '';
            registerPasswordInput.value = '';
            registerEmailInput.value = '';
            registerPhoneInput.value = '';
            showSection('login'); // 注册成功后切换到登录页面
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('注册失败:', error);
        displayMessage('注册请求失败，请检查网络或后端服务。', 'error');
    }
}

// 登录用户
async function loginUser() {
    const username = loginUsernameInput.value;
    const password = loginPasswordInput.value;

    try {
        const response = await fetch(`${API_BASE_URL}/user/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        const data = await response.json();
        if (data.success) {
            jwtToken = data.data; // JWT Token在data字段中
            localStorage.setItem('jwtToken', jwtToken); // 存储Token
            
            const decodedToken = parseJwt(jwtToken);
            if (decodedToken && decodedToken.userId) {
                currentUserId = decodedToken.userId; // 从Token中获取userId
            } else {
                console.warn("JWT does not contain userId or could not be parsed.");
            }

            displayMessage(data.message, 'success');
            loginUsernameInput.value = '';
            loginPasswordInput.value = '';
            showSection('cars-tab'); // 默认显示车辆列表区域，修正为完整ID
            fetchCars(currentPage, pageSize); // 获取车辆列表
            updateUIForAuthStatus(); // 登录成功后更新UI
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('登录失败:', error);
        displayMessage('登录请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取车辆列表 (带分页)
async function fetchCars(page = currentPage, size = pageSize) {
    if (!jwtToken) {
        displayMessage('请先登录以查看车辆信息。', 'error');
        showSection('login');
        return;
    }

    currentPage = page; // 更新当前页码
    currentFilter = 'all'; // 重置过滤类型
    currentFilterId = null; // 重置过滤ID

    try {
        const response = await fetch(`${API_BASE_URL}/cars?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}` // 发送JWT Token
            }
        });
        const data = await response.json();
        if (data.success) {
            carsContainer.innerHTML = ''; // 清空现有列表
            if (data.data && data.data.records && data.data.records.length > 0) {
                data.data.records.forEach(car => {
                    const carItem = document.createElement('div');
                    carItem.className = 'car-item'; // 使用新的car-item类
                    carItem.innerHTML = `
                        ${car.imageUrl ? `<img src="${car.imageUrl}" alt="${car.brandName} ${car.modelName}">` : ''} <!-- 车辆图片 -->
                        <h3>${car.brandName} ${car.modelName}</h3>
                        <p>生产年份: ${car.productionYear}</p>
                        <p>价格: ¥${car.price ? car.price.toLocaleString() : 'N/A'}</p>
                        <p>颜色: ${car.color}</p>
                        <p>里程数: ${car.mileage} 公里</p>
                        <p>库存状态: ${car.inventoryStatus}</p>
                        <button class="detail-button" data-car-id="${car.carId}">查看详情</button>
                    `;
                    carsContainer.appendChild(carItem);
                });
                totalPages = data.data.pages; // 更新总页数
                renderPaginationControls(); // 渲染分页控件
            } else {
                carsContainer.innerHTML = '<p class="no-results">暂无车辆信息。</p>';
                totalPages = 0; // 没有数据，总页数为0
                renderPaginationControls();
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取车辆列表失败:', error);
        displayMessage('获取车辆列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取品牌列表 (不再需要分页，直接获取所有品牌)
async function fetchBrands() {
    if (!jwtToken) {
        displayMessage('请先登录以查看品牌信息。', 'error');
        showSection('login');
        return;
    }
    currentPage = 1; // 重置页码
    currentFilter = 'brand'; // 设置过滤类型
    currentFilterId = null; // 重置过滤ID

    try {
        const response = await fetch(`${API_BASE_URL}/brands`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            brandsContainer.innerHTML = '';
            if (data.data && data.data.length > 0) {
                data.data.forEach(brand => {
                    const brandItem = document.createElement('div');
                    brandItem.className = 'list-item'; // 使用通用列表项样式
                    brandItem.innerHTML = `
                        <h3>${brand.brandName}</h3>
                        <button class="detail-button" data-brand-id="${brand.brandId}">查看车辆</button>
                    `;
                    // 为"查看车辆"按钮添加事件监听器
                    const viewCarsButton = brandItem.querySelector('.detail-button');
                    viewCarsButton.addEventListener('click', () => {
                        currentPage = 1; // 重置页码
                        currentFilter = 'brand';
                        currentFilterId = brand.brandId;
                        fetchCarsByBrand(currentPage, pageSize, brand.brandId); // 根据品牌ID获取车辆列表
                        showSection('cars-tab'); // 显示车辆列表，修正为完整ID
                    });
                    brandsContainer.appendChild(brandItem);
                });
            } else {
                brandsContainer.innerHTML = '<p class="no-results">暂无品牌信息。</p>';
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取品牌列表失败:', error);
        displayMessage('获取品牌列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 根据品牌ID获取车辆列表 (带分页)
async function fetchCarsByBrand(page = currentPage, size = pageSize, brandId = currentFilterId) {
    if (!jwtToken) {
        displayMessage('请先登录以查看车辆信息。', 'error');
        showSection('login');
        return;
    }

    currentPage = page;
    currentFilter = 'brand';
    currentFilterId = brandId; // 确保currentFilterId被正确设置

    try {
        const response = await fetch(`${API_BASE_URL}/cars/brand/${brandId}?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            carsContainer.innerHTML = ''; // 清空现有列表
            if (data.data && data.data.records && data.data.records.length > 0) {
                data.data.records.forEach(car => {
                    const carItem = document.createElement('div');
                    carItem.className = 'car-item';
                    carItem.innerHTML = `
                        ${car.imageUrl ? `<img src="${car.imageUrl}" alt="${car.brandName} ${car.modelName}">` : ''}
                        <h3>${car.brandName} ${car.modelName}</h3>
                        <p>生产年份: ${car.productionYear}</p>
                        <p>价格: ¥${car.price ? car.price.toLocaleString() : 'N/A'}</p>
                        <p>颜色: ${car.color}</p>
                        <p>里程数: ${car.mileage} 公里</p>
                        <p>库存状态: ${car.inventoryStatus}</p>
                        <button class="detail-button" data-car-id="${car.carId}">查看详情</button>
                    `;
                    carsContainer.appendChild(carItem);
                });
                totalPages = data.data.pages;
                renderPaginationControls();
            } else {
                carsContainer.innerHTML = '<p class="no-results">暂无该品牌车辆信息。</p>';
                totalPages = 0;
                renderPaginationControls();
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取品牌车辆列表失败:', error);
        displayMessage('获取品牌车辆列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取车型列表 (不再需要分页，直接获取所有车型)
async function fetchCarModels() {
    if (!jwtToken) {
        displayMessage('请先登录以查看车型信息。', 'error');
        showSection('login');
        return;
    }
    currentPage = 1; // 重置页码
    currentFilter = 'model'; // 设置过滤类型
    currentFilterId = null; // 重置过滤ID

    try {
        const response = await fetch(`${API_BASE_URL}/car-models`, { // 注意：路径已在后端修改为 /api/car-models
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            modelsContainer.innerHTML = '';
            if (data.data && data.data.length > 0) {
                data.data.forEach(model => {
                    const modelItem = document.createElement('div');
                    modelItem.className = 'list-item'; // 使用通用列表项样式
                    modelItem.innerHTML = `
                        <h3>${model.modelName}</h3>
                        <button class="detail-button" data-model-id="${model.modelId}">查看车辆</button>
                    `;
                    // 为"查看车辆"按钮添加事件监听器
                    const viewCarsButton = modelItem.querySelector('.detail-button');
                    viewCarsButton.addEventListener('click', () => {
                        currentPage = 1; // 重置页码
                        currentFilter = 'model';
                        currentFilterId = model.modelId;
                        fetchCarsByModel(currentPage, pageSize, model.modelId); // 根据车型ID获取车辆列表
                        showSection('cars-tab'); // 显示车辆列表，修正为完整ID
                    });
                    modelsContainer.appendChild(modelItem);
                });
            } else {
                modelsContainer.innerHTML = '<p class="no-results">暂无车型信息。</p>';
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取车型列表失败:', error);
        displayMessage('获取车型列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 根据车型ID获取车辆列表 (带分页)
async function fetchCarsByModel(page = currentPage, size = pageSize, modelId = currentFilterId) {
    if (!jwtToken) {
        displayMessage('请先登录以查看车辆信息。', 'error');
        showSection('login');
        return;
    }
    currentPage = page;
    currentFilter = 'model';
    currentFilterId = modelId; // 确保currentFilterId被正确设置

    try {
        const response = await fetch(`${API_BASE_URL}/cars/model/${modelId}?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            carsContainer.innerHTML = ''; // 清空现有列表
            if (data.data && data.data.records && data.data.records.length > 0) {
                data.data.records.forEach(car => {
                    const carItem = document.createElement('div');
                    carItem.className = 'car-item';
                    carItem.innerHTML = `
                        ${car.imageUrl ? `<img src="${car.imageUrl}" alt="${car.brandName} ${car.modelName}">` : ''}
                        <h3>${car.brandName} ${car.modelName}</h3>
                        <p>生产年份: ${car.productionYear}</p>
                        <p>价格: ¥${car.price ? car.price.toLocaleString() : 'N/A'}</p>
                        <p>颜色: ${car.color}</p>
                        <p>里程数: ${car.mileage} 公里</p>
                        <p>库存状态: ${car.inventoryStatus}</p>
                        <button class="detail-button" data-car-id="${car.carId}">查看详情</button>
                    `;
                    carsContainer.appendChild(carItem);
                });
                totalPages = data.data.pages;
                renderPaginationControls();
            } else {
                carsContainer.innerHTML = '<p class="no-results">暂无该车型车辆信息。</p>';
                totalPages = 0;
                renderPaginationControls();
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取车型车辆列表失败:', error);
        displayMessage('获取车型车辆列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取收藏列表
async function fetchFavorites() {
    if (!jwtToken || currentUserId === null) {
        displayMessage('请先登录以查看收藏信息。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/favorites/user/${currentUserId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            favoritesContainer.innerHTML = '';
            if (data.data && data.data.length > 0) {
                data.data.forEach(favorite => {
                    const favoriteItem = document.createElement('div');
                    favoriteItem.className = 'list-item';
                    favoriteItem.innerHTML = `
                        <h3>收藏车辆 ID: ${favorite.carId}</h3>
                        <p>收藏时间: ${new Date(favorite.favoriteTime).toLocaleString()}</p>
                        <button class="cancel-favorite-button" data-favorite-id="${favorite.favoriteId}">取消收藏</button>
                    `;
                    favoritesContainer.appendChild(favoriteItem);
                });
            } else {
                favoritesContainer.innerHTML = '<p class="no-results">暂无收藏车辆。</p>';
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取收藏列表失败:', error);
        displayMessage('获取收藏列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 取消收藏
async function deleteFavorite(event) {
    const favoriteId = event.target.dataset.favoriteId;
    if (!favoriteId) {
        displayMessage('无法获取收藏ID。', 'error');
        return;
    }
    if (!jwtToken) {
        displayMessage('请先登录。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/favorites/${favoriteId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            displayMessage(data.message, 'success');
            fetchFavorites(); // 重新加载收藏列表
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('取消收藏失败:', error);
        displayMessage('取消收藏请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取预约列表
async function fetchAppointments() {
    if (!jwtToken || currentUserId === null) {
        displayMessage('请先登录以查看预约信息。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/test-drive-appointments/user/${currentUserId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            appointmentsContainer.innerHTML = '';
            if (data.data && data.data.length > 0) {
                data.data.forEach(appointment => {
                    const appointmentItem = document.createElement('div');
                    appointmentItem.className = 'list-item';
                    appointmentItem.innerHTML = `
                        <h3>预约车辆 ID: ${appointment.carId}</h3>
                        <p>预约时间: ${new Date(appointment.appointmentTime).toLocaleString()}</p>
                        <p>联系方式: ${appointment.contactInfo || '未提供'}</p>
                        <button class="cancel-appointment-button" data-appointment-id="${appointment.appointmentId}" data-car-id="${appointment.carId}">取消预约</button>
                    `;
                    appointmentsContainer.appendChild(appointmentItem);
                });
            } else {
                appointmentsContainer.innerHTML = '<p class="no-results">暂无预约。</p>';
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('获取预约列表失败:', error);
        displayMessage('获取预约列表请求失败，请检查网络或后端服务。', 'error');
    }
}

// 取消预约函数
async function cancelAppointment(appointmentId, carId) {
    if (!jwtToken) {
        displayMessage('请先登录。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/test-drive-appointments/${appointmentId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            displayMessage(data.message, 'success');
            fetchAppointments(); // 重新加载预约列表
            if (carId) {
                fetchCarDetails(carId); 
            }
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('取消预约失败:', error);
        displayMessage('取消预约请求失败，请检查网络或后端服务。', 'error');
    }
}

// 获取车辆详情
async function fetchCarDetails(carId) {
    console.log('fetchCarDetails called with carId:', carId);
    if (!jwtToken) {
        displayMessage('请先登录以查看车辆详情。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/cars/${carId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        if (data.success) {
            console.log('Car details fetched successfully. Data:', data.data);
            const car = data.data;
            singleCarItemDisplay.innerHTML = `
                <img src="${car.imageUrl}" alt="${car.brandName} ${car.modelName}" class="car-detail-image">
                <h2>${car.brandName} ${car.modelName}</h2>
                <p><strong>生产年份:</strong> ${car.productionYear}</p>
                <p><strong>价格:</strong> ¥${car.price ? car.price.toLocaleString() : 'N/A'}</p>
                <p><strong>颜色:</strong> ${car.color}</p>
                <p><strong>里程数:</strong> ${car.mileage} 公里</p>
                <p><strong>库存状态:</strong> ${car.inventoryStatus}</p>
            `;

            const buttonGroup = document.createElement('div');
            buttonGroup.className = 'button-group';

            const backButton = document.createElement('button');
            backButton.textContent = '返回车辆列表';
            backButton.className = 'appointment-button';
            backButton.addEventListener('click', () => {
                showSection('cars-tab'); // 修正为完整ID
                fetchCars(currentPage, pageSize); 
                appointmentFormContainer.style.display = 'none';
                console.log('Back button clicked. Showing cars-tab.');
            });
            buttonGroup.appendChild(backButton);

            const isFavorited = await checkIfCarIsFavorited(carId); 
            console.log('Is car favorited?', isFavorited);
            const favoriteButton = document.createElement('button');
            favoriteButton.textContent = isFavorited ? '取消收藏' : '收藏';
            favoriteButton.className = isFavorited ? 'cancel-favorite-button' : 'favorite-button';
            favoriteButton.dataset.carId = carId; 
            favoriteButton.addEventListener('click', async (e) => {
                if (isFavorited) {
                    await removeFavorite(e); 
                } else {
                    await addFavorite(carId); 
                }
                fetchCarDetails(carId); 
                console.log('Favorite button clicked.');
            });
            buttonGroup.appendChild(favoriteButton);

            const isAppointed = await checkIfCarIsAppointed(carId); 
            console.log('Is car appointed?', isAppointed);
            const appointmentButton = document.createElement('button');
            appointmentButton.textContent = isAppointed ? '已预约' : '预约试驾';
            appointmentButton.className = isAppointed ? 'cancel-appointment-button' : 'appointment-button';
            appointmentButton.dataset.carId = carId; 
            appointmentButton.disabled = isAppointed; 
            appointmentButton.addEventListener('click', () => {
                if (!isAppointed) {
                    appointmentCarIdInput.value = carId;
                    carDetailContainer.style.display = 'none'; 
                    appointmentFormContainer.style.display = 'block'; 
                    console.log('Appointment button clicked. Showing appointment form.');
                }
            });
            buttonGroup.appendChild(appointmentButton);

            singleCarItemDisplay.appendChild(buttonGroup);

            // 确保显示车辆详情容器，并隐藏其他所有内容区域
            showSection('car-detail-container'); 
            console.log('Called showSection for car-detail-container.');
        } else {
            displayMessage(data.message, 'error');
            showSection('cars-tab'); 
            console.log('Failed to fetch car details. Showing cars-tab.');
        }
    } catch (error) {
        console.error('获取车辆详情失败:', error);
        displayMessage('获取车辆详情请求失败，请检查网络或后端服务。', 'error');
        showSection('cars-tab'); 
        console.log('Error fetching car details. Showing cars-tab.');
    }
}

// 检查车辆是否已被当前用户收藏
async function checkIfCarIsFavorited(carId) {
    if (!jwtToken || currentUserId === null) {
        return false; 
    }
    try {
        const response = await fetch(`${API_BASE_URL}/favorites/user/${currentUserId}/car/${carId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        return data.success && data.data; 
    } catch (error) {
        console.error('检查收藏状态失败:', error);
        return false;
    }
}

// 检查车辆是否已被当前用户预约
async function checkIfCarIsAppointed(carId) {
    if (!jwtToken || currentUserId === null) {
        return false; 
    }
    try {
        const response = await fetch(`${API_BASE_URL}/test-drive-appointments/user/${currentUserId}/car/${carId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const data = await response.json();
        // 检查data.data是否为数组且长度大于0
        return data.success && Array.isArray(data.data) && data.data.length > 0; 
    } catch (error) {
        console.error('检查预约状态失败:', error);
        return false;
    }
}

// 添加收藏
async function addFavorite(carId) {
    if (!jwtToken || currentUserId === null) {
        displayMessage('请先登录以收藏车辆。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/favorites`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify({ userId: currentUserId, carId: carId })
        });
        const data = await response.json();
        if (data.success) {
            displayMessage(data.message, 'success');
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('添加收藏失败:', error);
        displayMessage('添加收藏请求失败，请检查网络或后端服务。', 'error');
    }
}

// 移除收藏
async function removeFavorite(event) {
    const carId = event.target.dataset.carId; 
    if (!carId) {
        displayMessage('无法获取车辆ID。', 'error');
        return;
    }
    if (!jwtToken || currentUserId === null) {
        displayMessage('请先登录。', 'error');
        showSection('login');
        return;
    }

    try {
        const checkResponse = await fetch(`${API_BASE_URL}/favorites/user/${currentUserId}/car/${carId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        });
        const checkData = await checkResponse.json();

        if (checkData.success && checkData.data) {
            const favoriteId = checkData.data.favoriteId; 

            if (favoriteId) {
                const response = await fetch(`${API_BASE_URL}/favorites/${favoriteId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${jwtToken}`
                    }
                });
                const data = await response.json();
                if (data.success) {
                    displayMessage(data.message, 'success');
                } else {
                    displayMessage(data.message, 'error');
                }
            } else {
                displayMessage('未找到收藏记录的ID。', 'error');
            }
        } else {
            displayMessage('未找到收藏记录。', 'error');
        }
    } catch (error) {
        console.error('移除收藏失败:', error);
        displayMessage('移除收藏请求失败，请检查网络或后端服务。', 'error');
    }
}

// 预约试驾函数
async function scheduleAppointment(carId, appointmentTime, contactInfo) { 
    if (!jwtToken || currentUserId === null) {
        displayMessage('请先登录以预约试驾。', 'error');
        showSection('login');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/test-drive-appointments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify({ userId: currentUserId, carId: carId, appointmentTime: appointmentTime, contactInfo: contactInfo }) 
        });
        const data = await response.json();
        if (data.success) {
            displayMessage(data.message, 'success');
            appointmentFormContainer.style.display = 'none'; 
            fetchCarDetails(carId); 
        } else {
            displayMessage(data.message, 'error');
        }
    } catch (error) {
        console.error('预约试驾失败:', error);
        displayMessage('预约试驾请求失败，请检查网络或后端服务。', 'error');
    }
}

// 退出登录
function logoutUser() {
    jwtToken = null;
    localStorage.removeItem('jwtToken');
    currentUserId = null; 
    displayMessage('已成功退出登录。', 'success');
    showSection('login'); 
    updateUIForAuthStatus(); 
}

// 事件监听器
registerButton.addEventListener('click', registerUser);
loginButton.addEventListener('click', loginUser);
logoutButton.addEventListener('click', logoutUser);

// Tab 切换逻辑
document.querySelectorAll('.tab-button').forEach(button => {
    console.log('Found tab button:', button.id, button.dataset.tab);
    button.addEventListener('click', function() {
        console.log('Tab button clicked:', this.id, this.dataset.tab);
        document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        const tab = this.dataset.tab;
        console.log('Attempting to show section:', tab);
        // 根据tab的类型调用相应的fetch函数
        if (tab === 'cars-tab') {
            currentFilter = 'all'; // 重置为获取所有车辆
            currentPage = 1; // 重置页码
            fetchCars(currentPage, pageSize);
            showSection(tab); // 修正：传递完整的tab ID
        } else if (tab === 'brands-tab') {
            fetchBrands();
            showSection(tab); // 修正：传递完整的tab ID
        } else if (tab === 'models-tab') {
            fetchCarModels();
            showSection(tab); // 修正：传递完整的tab ID
        } else if (tab === 'favorites-tab') {
            fetchFavorites();
            showSection(tab); // 修正：传递完整的tab ID
        } else if (tab === 'appointments-tab') {
            fetchAppointments();
            showSection(tab); // 修正：传递完整的tab ID
        }
    });
});

// 登录/注册表单切换逻辑
if (showRegisterFormButton) {
    showRegisterFormButton.addEventListener('click', () => showSection('register'));
}
if (showLoginFormButton) {
    showLoginFormButton.addEventListener('click', () => showSection('login'));
}

// 车辆详情中的"查看详情"按钮（事件委托）
carsContainer.addEventListener('click', (event) => {
    console.log('Cars container clicked.', event.target);
    if (event.target.classList.contains('detail-button')) {
        const carId = event.target.dataset.carId;
        console.log('Detail button clicked. Car ID:', carId);
        if (carId) {
            fetchCarDetails(carId);
            if (paginationControls) {
                paginationControls.style.display = 'none';
            }
        }
    }
});

// 品牌列表中的"查看车辆"按钮（事件委托）
brandsContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('detail-button')) {
        const brandId = event.target.dataset.brandId;
        if (brandId) {
            currentPage = 1; 
            currentFilter = 'brand';
            currentFilterId = brandId;
            fetchCarsByBrand(currentPage, pageSize, brandId);
            showSection('cars-tab'); 
        }
    }
});

// 车型列表中的"查看车辆"按钮（事件委托）
modelsContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('detail-button')) {
        const modelId = event.target.dataset.modelId;
        if (modelId) {
            currentPage = 1; 
            currentFilter = 'model';
            currentFilterId = modelId;
            fetchCarsByModel(currentPage, pageSize, modelId);
            showSection('cars-tab'); 
        }
    }
});

// 取消收藏按钮（事件委托）
favoritesContainer.addEventListener('click', async (event) => {
    if (event.target.classList.contains('cancel-favorite-button')) {
        await deleteFavorite(event);
    }
});

// 取消预约按钮（事件委托）
appointmentsContainer.addEventListener('click', async (event) => {
    if (event.target.classList.contains('cancel-appointment-button')) {
        const appointmentId = event.target.dataset.appointmentId;
        const carId = event.target.dataset.carId; 
        if (appointmentId) {
            await cancelAppointment(parseInt(appointmentId), parseInt(carId)); 
        }
    }
});

// 预约试驾表单提交
if (submitAppointmentButton) {
    submitAppointmentButton.addEventListener('click', async () => {
        const carId = appointmentCarIdInput.value;
        const appointmentDate = appointmentDateInput.value;
        const appointmentTime = appointmentTimeInput.value;
        const contactInfo = appointmentContactInput.value; 

        if (!appointmentDate || !appointmentTime) {
            displayMessage('请选择预约日期和时间。', 'error');
            return;
        }

        const fullAppointmentDateTime = `${appointmentDate}T${appointmentTime}:00`; 
        
        await scheduleAppointment(parseInt(carId), fullAppointmentDateTime, contactInfo); 
    });
}

// 取消预约试驾表单
if (cancelAppointmentFormButton) {
    cancelAppointmentFormButton.addEventListener('click', () => {
        appointmentFormContainer.style.display = 'none'; 
        carDetailContainer.style.display = 'block'; 
    });
}

// 分页按钮事件监听器
if (prevPageButton) {
    prevPageButton.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            if (currentFilter === 'all') {
                fetchCars(currentPage, pageSize);
            } else if (currentFilter === 'brand' && currentFilterId) {
                fetchCarsByBrand(currentPage, pageSize, currentFilterId);
            } else if (currentFilter === 'model' && currentFilterId) {
                fetchCarsByModel(currentPage, pageSize, currentFilterId);
            }
        }
    });
}

if (nextPageButton) {
    nextPageButton.addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            if (currentFilter === 'all') {
                fetchCars(currentPage, pageSize);
            } else if (currentFilter === 'brand' && currentFilterId) {
                fetchCarsByBrand(currentPage, pageSize, currentFilterId);
            } else if (currentFilter === 'model' && currentFilterId) {
                fetchCarsByModel(currentPage, pageSize, currentFilterId);
            }
        }
    });
}

// 初始化：检查登录状态并显示相应内容
document.addEventListener('DOMContentLoaded', () => {
    if (jwtToken) {
        const decodedToken = parseJwt(jwtToken);
        if (decodedToken && decodedToken.userId) {
            currentUserId = decodedToken.userId;
            showSection('cars-tab'); // 修正：传递完整的tab ID
            fetchCars(currentPage, pageSize); 
        } else {
            console.warn("Invalid JWT or missing userId, logging out.");
            logoutUser();
        }
    } else {
        showSection('login'); 
    }
    updateUIForAuthStatus();
}); 