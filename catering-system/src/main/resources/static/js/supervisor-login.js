// same-origin (served by Spring Boot)
const API_BASE = '';
const DASHBOARD_URL = '/supervisor-dashboard.html';

function show(el) { el?.classList.remove('hidden'); }
function hide(el) { el?.classList.add('hidden'); }
function setInfo(msg, isError = false) {
    const info = document.getElementById('info');
    if (!info) return;
    info.textContent = msg || '';
    info.className = 'mt-4 text-center text-sm ' + (isError ? 'text-red-600' : 'text-gray-600');
    if (msg) show(info); else hide(info);
}

async function loginRequest(email, password) {
    const res = await fetch(API_BASE + '/api/supervisors/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });

    const ct = res.headers.get('content-type') || '';
    let body = null;
    if (ct.includes('application/json')) body = await res.json().catch(() => null);
    else body = await res.text().catch(() => null);

    if (!res.ok) {
        const msg = body && body.message ? body.message : 'Login failed.';
        throw new Error(msg);
    }

    if (!body || typeof body !== 'object' || body.id == null || !body.email) {
        throw new Error('Unexpected response from server.');
    }
    return body;
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('supervisor-login.js loaded');

    const form = document.getElementById('login-form');
    const emailEl = document.getElementById('email');
    const passEl = document.getElementById('password');
    const failBox = document.getElementById('fail-box');
    const failText = document.getElementById('fail-text');
    const tryAgainBtn = document.getElementById('try-again');
    const loginBtn = document.getElementById('login-btn');

    async function doLogin() {
        hide(failBox);
        setInfo('Signing in...');
        loginBtn.disabled = true;

        try {
            const user = await loginRequest(emailEl.value.trim(), passEl.value);
            localStorage.setItem('supervisor', JSON.stringify(user));
            setInfo('Login successful. Redirecting...');
            setTimeout(() => { window.location.href = DASHBOARD_URL; }, 600);
        } catch (err) {
            console.error('Login error:', err);
            setInfo('');
            if (failText) failText.textContent = err.message || 'Login failed. Please contact the admin.';
            show(failBox);
        } finally {
            loginBtn.disabled = false;
        }
    }

    loginBtn?.addEventListener('click', doLogin);
    form?.addEventListener('submit', (e) => { e.preventDefault(); doLogin(); });

    tryAgainBtn?.addEventListener('click', () => {
        hide(failBox);
        setInfo('');
        passEl.value = '';
        passEl.focus();
    });
});