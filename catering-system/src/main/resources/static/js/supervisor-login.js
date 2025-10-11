(() => {
    const form = document.getElementById('loginForm');
    const emailEl = document.getElementById('email');
    const passwordEl = document.getElementById('password');
    const btn = document.getElementById('loginBtn');
    const errorEl = document.getElementById('error');

    if (!form) return;

    async function login(email, password) {
        const res = await fetch(`${window.APP_CONFIG?.apiBase || ''}/supervisor/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        const contentType = res.headers.get('content-type') || '';
        const data = contentType.includes('application/json') ? await res.json() : null;
        if (!res.ok) {
            const message = (data && (data.message || data.error || data.details)) || 'Invalid credentials';
            throw new Error(message);
        }
        return data;
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorEl.style.display = 'none';
        errorEl.textContent = '';
        btn.disabled = true;

        const email = emailEl.value.trim();
        const password = passwordEl.value;
        try {
            const user = await login(email, password);
            sessionStorage.setItem('supervisor', JSON.stringify(user));
            window.location.assign('/supervisor/dashboard');
        } catch (err) {
            errorEl.textContent = err.message || 'Login failed';
            errorEl.style.display = 'block';
        } finally {
            btn.disabled = false;
        }
    });
})();



