// 全局变量
let currentPage = 1;
const pageSize = 10;
let selectedBooks = new Set();

// DOM 加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 初始化页面
    loadBooks();
    
    // 绑定事件监听器
    document.getElementById('searchBtn').addEventListener('click', handleSearch);
    document.getElementById('selectAll').addEventListener('change', handleSelectAll);
    document.getElementById('batchDeleteBtn').addEventListener('click', handleBatchDelete);
    document.getElementById('saveBookBtn').addEventListener('click', handleSaveBook);
    document.getElementById('updateBookBtn').addEventListener('click', handleUpdateBook);
    
    // 搜索框回车事件
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
});

// 加载图书列表
async function loadBooks(page = 1, bookId = null) {
    try {
        const response = await fetch('/books/page', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                pageNum: page,
                pageSize: pageSize,
                bookId: bookId
            })
        });
        
        const result = await response.json();
        if (result.code === 200) {
            renderBookList(result.data);
            renderPagination(result.data);
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '加载图书列表失败');
        console.error('Error:', error);
    }
}

// 渲染图书列表
function renderBookList(data) {
    const bookList = document.getElementById('bookList');
    bookList.innerHTML = '';
    
    data.list.forEach(book => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>
                <input type="checkbox" class="form-check-input book-checkbox" 
                    value="${String(book.bookId)}" ${selectedBooks.has(String(book.bookId)) ? 'checked' : ''}>
            </td>
            <td>${book.title || ''}</td>
            <td>${book.author || ''}</td>
            <td>${book.isbn || ''}</td>
            <td>${book.publisher || ''}</td>
            <td>${book.publishDate ? formatDate(book.publishDate) : ''}</td>
            <td>${book.categoryId || ''}</td>
            <td>${book.price ? book.price.toFixed(2) : ''}</td>
            <td>${book.discountPrice ? book.discountPrice.toFixed(2) : ''}</td>
            <td>${book.stockQuantity || 0}</td>
            <td>${book.salesVolume || 0}</td>
            <td>
                <span class="badge ${book.status === 1 ? 'bg-success' : 'bg-danger'}">
                    ${book.status === 1 ? '上架' : '下架'}
                </span>
                ${book.isRecommended === 1 ? '<span class="badge bg-info ms-1">推荐</span>' : ''}
                ${book.isBestseller === 1 ? '<span class="badge bg-warning ms-1">畅销</span>' : ''}
                ${book.isNew === 1 ? '<span class="badge bg-primary ms-1">新书</span>' : ''}
            </td>
            <td>
                <button class="btn btn-sm btn-primary edit-btn" data-id="${String(book.bookId)}" title="编辑">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${String(book.bookId)}" title="删除">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        bookList.appendChild(tr);
    });
    
    // 绑定编辑和删除按钮事件
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', () => handleEdit(btn.dataset.id));
    });
    
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => handleDelete(btn.dataset.id));
    });
    
    // 绑定复选框事件
    document.querySelectorAll('.book-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', handleBookSelect);
    });
    
    // 更新批量删除按钮状态
    updateBatchDeleteButton();
}

// 渲染分页
function renderPagination(data) {
    const pagination = document.getElementById('pagination');
    const totalPages = Math.ceil(data.total / pageSize);
    
    let html = '';
    
    // 上一页
    html += `
        <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage - 1}">上一页</a>
        </li>
    `;
    
    // 页码
    for (let i = 1; i <= totalPages; i++) {
        if (i === 1 || i === totalPages || (i >= currentPage - 2 && i <= currentPage + 2)) {
            html += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>
            `;
        } else if (i === currentPage - 3 || i === currentPage + 3) {
            html += '<li class="page-item disabled"><span class="page-link">...</span></li>';
        }
    }
    
    // 下一页
    html += `
        <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${currentPage + 1}">下一页</a>
        </li>
    `;
    
    pagination.innerHTML = html;
    
    // 绑定分页事件
    pagination.querySelectorAll('.page-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const page = parseInt(e.target.dataset.page);
            if (!isNaN(page) && page !== currentPage) {
                currentPage = page;
                loadBooks(page);
            }
        });
    });
}

// 处理搜索
function handleSearch() {
    const bookId = document.getElementById('searchInput').value.trim();
    currentPage = 1;
    loadBooks(currentPage, bookId ? parseInt(bookId) : null);
}

// 处理全选
function handleSelectAll(e) {
    const checkboxes = document.querySelectorAll('.book-checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = e.target.checked;
        if (e.target.checked) {
            selectedBooks.add(String(checkbox.value));
        } else {
            selectedBooks.delete(String(checkbox.value));
        }
    });
    updateBatchDeleteButton();
}

// 处理单个选择
function handleBookSelect(e) {
    const bookId = String(e.target.value);
    if (e.target.checked) {
        selectedBooks.add(bookId);
    } else {
        selectedBooks.delete(bookId);
    }
    updateBatchDeleteButton();
}

// 更新批量删除按钮状态
function updateBatchDeleteButton() {
    const batchDeleteBtn = document.getElementById('batchDeleteBtn');
    batchDeleteBtn.disabled = selectedBooks.size === 0;
}

// 处理批量删除
async function handleBatchDelete() {
    if (selectedBooks.size === 0) return;
    
    if (!confirm(`确定要删除选中的 ${selectedBooks.size} 本图书吗？`)) return;
    
    try {
        const response = await fetch('/books/batchDelete', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(Array.from(selectedBooks))
        });
        
        const result = await response.json();
        if (result.code === 200) {
            showAlert('success', result.message);
            selectedBooks.clear();
            loadBooks(currentPage);
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '批量删除失败');
        console.error('Error:', error);
    }
}

// 格式化日期
function formatDate(dateStr) {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
    return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
}

// 格式化日期时间
function formatDateTime(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    return date.toISOString().slice(0, 19).replace('T', ' ');
}

// 处理编辑
async function handleEdit(bookId) {
    try {
        const response = await fetch(`/books/${bookId}`);
        const result = await response.json();
        
        if (result.code === 200) {
            const book = result.data;
            const form = document.getElementById('editBookForm');
            
            // 填充表单，将 bookId 作为字符串处理
            form.bookId.value = String(book.bookId);
            form.title.value = book.title || '';
            form.author.value = book.author || '';
            form.isbn.value = book.isbn || '';
            form.publisher.value = book.publisher || '';
            form.publishDate.value = book.publishDate ? book.publishDate.replace(' ', 'T').substring(0, 16) : '';
            form.categoryId.value = book.categoryId || '';
            form.price.value = book.price || '';
            form.discountPrice.value = book.discountPrice || '';
            form.marketPrice.value = book.marketPrice || '';
            form.stockQuantity.value = book.stockQuantity || '';
            form.status.value = book.status || '1';
            form.summary.value = book.summary || '';
            form.isRecommended.checked = book.isRecommended === 1;
            form.isBestseller.checked = book.isBestseller === 1;
            form.isNew.checked = book.isNew === 1;
            
            // 显示模态框
            const modal = new bootstrap.Modal(document.getElementById('editBookModal'));
            modal.show();
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '获取图书信息失败');
        console.error('Error:', error);
    }
}

// 验证价格字段
function validatePriceFields(book) {
    if (!book.price || book.price <= 0) {
        showAlert('error', '请输入有效的价格');
        return false;
    }
    if (!book.discountPrice || book.discountPrice <= 0) {
        showAlert('error', '请输入有效的折扣价');
        return false;
    }
    if (!book.marketPrice || book.marketPrice <= 0) {
        showAlert('error', '请输入有效的市场价');
        return false;
    }
    
    // 验证折扣价不能高于市场价
    if (Number(book.discountPrice) > Number(book.marketPrice)) {
        showAlert('error', '折扣价不能高于市场价');
        return false;
    }
    
    // 验证价格不能高于市场价
    if (Number(book.price) > Number(book.marketPrice)) {
        showAlert('error', '价格不能高于市场价');
        return false;
    }
    
    return true;
}

// 处理保存新图书
async function handleSaveBook() {
    const form = document.getElementById('addBookForm');
    const formData = new FormData(form);
    const book = Object.fromEntries(formData.entries());
    
    // 验证必填字段
    if (!book.publishDate) {
        showAlert('error', '请选择出版日期');
        return;
    }
    
    // 验证价格字段
    if (!validatePriceFields(book)) {
        return;
    }
    
    // 处理复选框值
    book.isRecommended = book.isRecommended ? 1 : 0;
    book.isBestseller = book.isBestseller ? 1 : 0;
    book.isNew = book.isNew ? 1 : 0;
    
    // 处理日期时间
    if (book.publishDate) {
        book.publishDate = formatDateTime(book.publishDate);
    }
    
    // 处理数字类型
    const numberFields = ['categoryId', 'stockQuantity', 'price', 'discountPrice', 'marketPrice', 'status'];
    
    numberFields.forEach(field => {
        if (book[field]) {
            book[field] = Number(book[field]);
        }
    });
    
    try {
        const response = await fetch('/books/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        });
        
        const result = await response.json();
        if (result.code === 200) {
            showAlert('success', result.message);
            form.reset();
            bootstrap.Modal.getInstance(document.getElementById('addBookModal')).hide();
            loadBooks(currentPage);
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '添加图书失败');
        console.error('Error:', error);
    }
}

// 处理更新
async function handleUpdateBook() {
    const form = document.getElementById('editBookForm');
    const formData = new FormData(form);
    const book = Object.fromEntries(formData.entries());
    
    // 验证必填字段
    if (!book.publishDate) {
        showAlert('error', '请选择出版日期');
        return;
    }
    
    // 验证价格字段
    if (!validatePriceFields(book)) {
        return;
    }
    
    // 处理复选框值
    book.isRecommended = book.isRecommended ? 1 : 0;
    book.isBestseller = book.isBestseller ? 1 : 0;
    book.isNew = book.isNew ? 1 : 0;
    
    // 处理日期时间
    if (book.publishDate) {
        book.publishDate = formatDateTime(book.publishDate);
    }
    
    // 处理数字类型
    const numberFields = ['categoryId', 'stockQuantity', 'price', 'discountPrice', 'marketPrice', 'status'];
    
    numberFields.forEach(field => {
        if (book[field]) {
            book[field] = Number(book[field]);
        }
    });
    
    try {
        const response = await fetch('/books/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)
        });
        
        const result = await response.json();
        if (result.code === 200) {
            showAlert('success', result.message);
            bootstrap.Modal.getInstance(document.getElementById('editBookModal')).hide();
            loadBooks(currentPage);
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '更新图书失败');
        console.error('Error:', error);
    }
}

// 处理删除
async function handleDelete(bookId) {
    if (!confirm('确定要删除这本图书吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`/books/${bookId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        if (result.code === 200) {
            showAlert('success', result.message);
            loadBooks(currentPage);
        } else {
            showAlert('error', result.message);
        }
    } catch (error) {
        showAlert('error', '删除图书失败');
        console.error('Error:', error);
    }
}

// 显示提示框
function showAlert(type, message) {
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    const toastInstance = bootstrap.Toast.getOrCreateInstance(toast);
    
    // 移除所有类型类
    toast.classList.remove('success', 'error', 'warning', 'info');
    
    // 设置类型和图标
    let icon = 'fa-info-circle';
    switch (type) {
        case 'success':
            toast.classList.add('success');
            icon = 'fa-check-circle';
            break;
        case 'error':
            toast.classList.add('error');
            icon = 'fa-exclamation-circle';
            break;
        case 'warning':
            toast.classList.add('warning');
            icon = 'fa-exclamation-triangle';
            break;
        case 'info':
            toast.classList.add('info');
            icon = 'fa-info-circle';
            break;
    }
    
    // 更新消息和图标
    toastMessage.innerHTML = `<i class="fas ${icon} me-2"></i>${message}`;
    
    // 显示提示框
    toastInstance.show();
    
    // 自动隐藏
    setTimeout(() => {
        toastInstance.hide();
    }, 3000);
} 