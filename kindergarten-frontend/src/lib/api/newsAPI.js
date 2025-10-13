// src/lib/api/newsAPI.js
// @ts-nocheck
/**
 * Получить все новости
 */
// export async function getAllNews() {
//     const res = await fetch('http://localhost:8080/api/news', {
//         credentials: 'include'
//     });
//     if (!res.ok) throw new Error('Не удалось загрузить новости');
//     return await res.json();
// }

/**
 * Получить опубликованные новости
 */
export async function getPublishedNews() {
    const res = await fetch('http://localhost:8080/api/published_news', {
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Не удалось загрузить опубликованные новости');
    return await res.json();
}

/**
 * Получить новости для админа
 */
export async function getAdminNews(fetch) {
    const res = await fetch('http://localhost:8080/api/admin/news', {
        credentials: 'include'
    });
    if (!res.ok) [];
    return await res.json();
}

/**
 * Создать новость (админ)
 */
export async function createNews(contentDto) {
    const res = await fetch('http://localhost:8080/api/admin/news', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(contentDto)
    });
    if (!res.ok) throw new Error('Не удалось создать новость');
    return await res.json();
}

/**
 * Обновить новость (админ)
 */
export async function updateNews(id, contentDto) {
    const res = await fetch(`http://localhost:8080/api/admin/news/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(contentDto)
    });
    if (!res.ok) throw new Error('Не удалось обновить новость');
    return await res.json();
}

/**
 * Удалить новость (админ)
 */
export async function deleteNews(id) {
    const res = await fetch(`http://localhost:8080/api/admin/news/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    });
    if (!res.ok) throw new Error('Не удалось удалить новость');
    return await res.json();
}
