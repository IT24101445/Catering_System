const LOGIN_URL = '/supervisor-login.html';

document.addEventListener('DOMContentLoaded', () => {
    try {
        const raw = localStorage.getItem('supervisor');
        if (!raw) return (window.location.href = LOGIN_URL);
        const user = JSON.parse(raw);
        if (!user || !user.email) return (window.location.href = LOGIN_URL);
        document.getElementById('supervisorEmail').textContent = user.email;
    } catch {
        return (window.location.href = LOGIN_URL);
    }

    document.getElementById('logoutBtn')?.addEventListener('click', () => {
        localStorage.removeItem('supervisor');
        window.location.href = LOGIN_URL;
    });
});