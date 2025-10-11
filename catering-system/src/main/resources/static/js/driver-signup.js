document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('signupForm');
    const signupBtn = document.getElementById('signupBtn');
    const errorDiv = document.getElementById('error');
    const successDiv = document.getElementById('success');

    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const name = document.getElementById('name').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // Clear previous messages
        hideMessages();

        // Validation
        if (!name || !email || !password || !confirmPassword) {
            showError('Please fill in all fields');
            return;
        }

        if (password !== confirmPassword) {
            showError('Passwords do not match');
            return;
        }

        if (password.length < 6) {
            showError('Password must be at least 6 characters long');
            return;
        }

        if (!isValidEmail(email)) {
            showError('Please enter a valid email address');
            return;
        }

        signupBtn.disabled = true;
        signupBtn.textContent = 'Creating Account...';

        try {
            const response = await fetch('/api/drivers', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ 
                    name: name,
                    email: email, 
                    password: password,
                    status: 'AVAILABLE'
                })
            });

            if (response.ok) {
                const driver = await response.json();
                showSuccess('Account created successfully! Redirecting to login...');
                
                // Redirect to login page after 2 seconds
                setTimeout(() => {
                    window.location.href = '/driver/login';
                }, 2000);
            } else {
                const error = await response.json();
                showError(error.message || 'Signup failed. Email might already be in use.');
            }
        } catch (error) {
            console.error('Signup error:', error);
            showError('Network error. Please try again.');
        } finally {
            signupBtn.disabled = false;
            signupBtn.textContent = 'Create Account';
        }
    });

    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function showError(message) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
        successDiv.style.display = 'none';
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }

    function showSuccess(message) {
        successDiv.textContent = message;
        successDiv.style.display = 'block';
        errorDiv.style.display = 'none';
    }

    function hideMessages() {
        errorDiv.style.display = 'none';
        successDiv.style.display = 'none';
    }
});
