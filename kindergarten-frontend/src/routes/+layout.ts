// src/routes/+layout.ts
import type { LayoutLoad } from './$types';
import { userStore } from '$lib/stores/userStore';
import { checkAuth } from '$lib/api/auth';

export const load: LayoutLoad = async ({ fetch }) => {
    
    const { authenticated, role } = await checkAuth(fetch);

    if (authenticated && role === 'ADMIN') {
        userStore.set({ isLoggedIn: true, role: 'ADMIN' });
    } else {
        userStore.set({ isLoggedIn: false, role: 'USER' });
    }

    return {};
};
