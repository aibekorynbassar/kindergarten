// src/routes/admin/+page.ts
import { checkAuth } from '$lib/api/auth';
import { getAdminNews } from '$lib/api/newsAPI';
import type { PageLoad } from './$types';
import { redirect } from '@sveltejs/kit';

export const load: PageLoad = async ({ fetch }) => {
    // Проверка авторизации бэком (опционально)
    const { authenticated, role } = await checkAuth(fetch);
    if (!authenticated || role !== 'ADMIN') throw redirect(303, '/auth/login');

    // Загрузка новостей
    const news = await getAdminNews(fetch);
    return { news };
};