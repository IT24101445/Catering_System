// app.js - Main JavaScript for the Kitchen System

console.log("Kitchen App JS loaded");

// Kitchen Login Functionality
class KitchenAuth {
    constructor() {
        this.isLoggedIn = false;
        this.userEmail = null;
        this.userName = null;
        this.init();
    }

    init() {
        this.checkExistingLogin();
        this.bindEvents();
    }

    bindEvents() {
        // Login form submission
        const loginForm = document.getElementById('kitchen-login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => this.handleLogin(e));
        }

        // Register form submission
        const registerForm = document.getElementById('kitchen-register-form');
        if (registerForm) {
            registerForm.addEventListener('submit', (e) => this.handleRegister(e));
        }

        // Logout button
        const logoutBtn = document.getElementById('kitchen-logout-btn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => this.handleLogout(e));
        }
    }

    handleLogin(event) {
        event.preventDefault();
        
        const email = document.getElementById('kitchen-email').value.trim();
        const password = document.getElementById('kitchen-password').value.trim();

        // Basic validation
        if (!email || !password) {
            this.showError('Please fill in all fields.');
            return;
        }

        if (!email.includes('@')) {
            this.showError('Please enter a valid email address.');
            return;
        }

        this.showLoading();

        // Send login request to server
        const formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);

        fetch('/kitchen/login', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                this.showSuccess();
                
                // Store login state in sessionStorage for client-side checks
                sessionStorage.setItem('kitchenLoggedIn', 'true');
                sessionStorage.setItem('kitchenEmail', email);
                sessionStorage.setItem('kitchenLoginTime', new Date().toISOString());
                
                // Redirect to the kitchen dashboard after short delay
                setTimeout(() => {
                    window.location.href = data.redirectUrl || '/kitchen/dashboard';
                }, 1500);
            } else {
                this.showError(data.message || 'Invalid email or password. Please try again.');
            }
        })
        .catch(error => {
            console.error('Kitchen login error:', error);
            this.showError('Login failed. Please try again.');
        });
    }

    handleRegister(event) {
        event.preventDefault();
        
        const email = document.getElementById('register-email').value.trim();
        const password = document.getElementById('register-password').value.trim();
        const firstName = document.getElementById('register-firstName').value.trim();
        const lastName = document.getElementById('register-lastName').value.trim();
        const position = document.getElementById('register-position').value.trim();

        // Basic validation
        if (!email || !password || !firstName || !lastName || !position) {
            this.showError('Please fill in all fields.');
            return;
        }

        if (!email.includes('@')) {
            this.showError('Please enter a valid email address.');
            return;
        }

        if (password.length < 6) {
            this.showError('Password must be at least 6 characters long.');
            return;
        }

        this.showLoading();

        // Send registration request to server
        const formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);
        formData.append('firstName', firstName);
        formData.append('lastName', lastName);
        formData.append('position', position);

        fetch('/kitchen/register', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                this.showSuccess('Registration successful! Redirecting to login...');
                
                // Redirect to login page after short delay
                setTimeout(() => {
                    window.location.href = data.redirectUrl || '/kitchen/login';
                }, 1500);
            } else {
                this.showError(data.message || 'Registration failed. Please try again.');
            }
        })
        .catch(error => {
            console.error('Kitchen registration error:', error);
            this.showError('Registration failed. Please try again.');
        });
    }

    handleLogout(event) {
        event.preventDefault();
        
        fetch('/kitchen/logout', {
            method: 'GET'
        })
        .then(() => {
            // Clear session storage
            sessionStorage.removeItem('kitchenLoggedIn');
            sessionStorage.removeItem('kitchenEmail');
            sessionStorage.removeItem('kitchenLoginTime');
            
            // Redirect to login page
            window.location.href = '/kitchen/login';
        })
        .catch(error => {
            console.error('Logout error:', error);
            // Still redirect even if logout fails
            window.location.href = '/kitchen/login';
        });
    }

    checkExistingLogin() {
        const isLoggedIn = sessionStorage.getItem('kitchenLoggedIn');
        const loginTime = sessionStorage.getItem('kitchenLoginTime');
        
        if (isLoggedIn === 'true' && loginTime) {
            // Check if login is still valid (24 hours)
            const loginDate = new Date(loginTime);
            const now = new Date();
            const hoursDiff = (now - loginDate) / (1000 * 60 * 60);
            
            if (hoursDiff < 24) {
                this.isLoggedIn = true;
                this.userEmail = sessionStorage.getItem('kitchenEmail');
                this.userName = sessionStorage.getItem('kitchenStaffName');
            } else {
                // Session expired
                this.clearSession();
            }
        }
    }

    clearSession() {
        sessionStorage.removeItem('kitchenLoggedIn');
        sessionStorage.removeItem('kitchenEmail');
        sessionStorage.removeItem('kitchenLoginTime');
        sessionStorage.removeItem('kitchenStaffName');
    }

    showLoading() {
        const submitBtn = document.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';
        }
    }

    showSuccess(message = 'Login successful! Redirecting...') {
        this.showMessage(message, 'success');
        this.resetForm();
    }

    showError(message) {
        this.showMessage(message, 'error');
        this.resetForm();
    }

    showMessage(message, type) {
        // Remove existing messages
        const existingMessages = document.querySelectorAll('.kitchen-message');
        existingMessages.forEach(msg => msg.remove());

        // Create new message element
        const messageDiv = document.createElement('div');
        messageDiv.className = `kitchen-message alert alert-${type === 'success' ? 'success' : 'danger'} mt-3`;
        messageDiv.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-triangle'} me-2"></i>
            ${message}
        `;

        // Insert message after form
        const form = document.querySelector('form');
        if (form) {
            form.parentNode.insertBefore(messageDiv, form.nextSibling);
        }

        // Auto-remove success messages after 3 seconds
        if (type === 'success') {
            setTimeout(() => {
                messageDiv.remove();
            }, 3000);
        }
    }

    resetForm() {
        const submitBtn = document.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = submitBtn.getAttribute('data-original-text') || 'Sign In';
        }
    }
}

// Kitchen Dashboard Functionality
class KitchenDashboard {
    constructor() {
        this.init();
    }

    init() {
        this.bindEvents();
        this.loadDashboardData();
    }

    bindEvents() {
        // Navigation links
        const navLinks = document.querySelectorAll('.kitchen-nav-link');
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => this.handleNavigation(e));
        });

        // Order status updates
        const statusButtons = document.querySelectorAll('.order-status-btn');
        statusButtons.forEach(btn => {
            btn.addEventListener('click', (e) => this.updateOrderStatus(e));
        });
    }

    handleNavigation(event) {
        const target = event.target.getAttribute('data-target');
        if (target) {
            window.location.href = `/kitchen/${target}`;
        }
    }

    updateOrderStatus(event) {
        const orderId = event.target.getAttribute('data-order-id');
        const newStatus = event.target.getAttribute('data-status');
        
        if (orderId && newStatus) {
            fetch(`/api/orders/${orderId}/status`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ status: newStatus })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    this.showMessage('Order status updated successfully!', 'success');
                    // Reload the page to show updated status
                    setTimeout(() => {
                        window.location.reload();
                    }, 1000);
                } else {
                    this.showMessage('Failed to update order status.', 'error');
                }
            })
            .catch(error => {
                console.error('Error updating order status:', error);
                this.showMessage('Error updating order status.', 'error');
            });
        }
    }

    loadDashboardData() {
        // Load pending orders, notifications, etc.
        console.log('Loading kitchen dashboard data...');
    }

    showMessage(message, type) {
        // Similar to KitchenAuth showMessage method
        const messageDiv = document.createElement('div');
        messageDiv.className = `alert alert-${type === 'success' ? 'success' : 'danger'} alert-dismissible fade show`;
        messageDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        const container = document.querySelector('.dashboard-content') || document.body;
        container.insertBefore(messageDiv, container.firstChild);

        // Auto-remove after 5 seconds
        setTimeout(() => {
            messageDiv.remove();
        }, 5000);
    }
}

// Initialize when DOM is loaded
document.addEventListener("DOMContentLoaded", function() {
    console.log("Kitchen DOM fully loaded and parsed");
    
    // Initialize kitchen authentication
    window.kitchenAuth = new KitchenAuth();
    
    // Initialize kitchen dashboard if on dashboard page
    if (window.location.pathname.includes('/kitchen/dashboard')) {
        window.kitchenDashboard = new KitchenDashboard();
    }
});
