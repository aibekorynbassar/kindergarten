// frontend/src/lib/api/auth.js
// @ts-nocheck
/**
 * Выполняет POST /login и сохраняет JSESSIONID cookie.
 */
export async function login({ username, password }) {
    const params = new URLSearchParams();
    params.append("username", username);
    params.append("password", password);

    const response = await fetch("/login", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params,
        credentials: "include", // важно для сохранения сессионной cookie
        // redirect: 'manual'
    });

    if (response.url.includes('/login' || !response.ok)) {
        throw new Error("Login failed");
    }
    // Сессия установлена автоматически через cookie
}

/**
 * Выход из системы
 */
export async function logout() {
    const response = await fetch("/logout", {
        method: "POST",
        credentials: "include",
        // redirect: 'manual'
    });

    if (!response.ok) {
        throw new Error("Logout failed");
    }
}

/**
 * Регистрация нового пользователя
 */
export async function register({ username, password }) {
    const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
        credentials: "include"
    });

    if (!response.ok) {
        throw new Error("Registration failed");
    }

    return response.json();
}

export async function checkAuth(fetch) {
    try {
        const response = await fetch('/api/auth/checker', {
            credentials: 'include'
        });

        if (response.ok) {
            return response.json();
        }
    } catch {
        return { authenticated: false, role: 'USER' };
    }
    return { authenticated: false, role: 'USER' };
}
