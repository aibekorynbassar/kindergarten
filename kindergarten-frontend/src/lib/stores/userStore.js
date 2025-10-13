// frontend/src/lib/stores/userStore.js
import { writable } from 'svelte/store';

export const userStore = writable({
    isLoggedIn: false,
    role: 'USER'  // по умолчанию USER
});

// Вход как админ
export function setLoggedIn() {
    userStore.set({
        isLoggedIn: true,
        role: 'ADMIN'
    });
}

// Выход (возврат к USER)
export function resetAuth() {
    userStore.set({
        isLoggedIn: false,
        role: 'USER'
    });
}
