// src/routes/auth/login/+page.ts
import type { LoadEvent } from '@sveltejs/kit';
import { redirect } from '@sveltejs/kit';
import { resetAuth, userStore, setLoggedIn } from '$lib/stores/userStore';
import { checkAuth } from '$lib/api/auth';

export const load = async ({ fetch }: LoadEvent) => {
    
    const data = await checkAuth(fetch);
    if (data.authenticated && data.role === 'ADMIN') {
        throw redirect(303, '/admin');
    }

    // return {};
};
