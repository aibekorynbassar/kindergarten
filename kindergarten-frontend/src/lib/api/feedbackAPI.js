// src/lib/api/feedbackAPI.js
// @ts-nocheck

/**
 * Отправить обращение обратной связи
 * @param {Object} feedbackData - данные обратной связи
 * @param {string} feedbackData.name - имя пользователя
 * @param {string} feedbackData.phone - номер телефона
 * @param {string} feedbackData.subject - тема сообщения
 * @param {string} feedbackData.message - комментарии
 */

export async function submitFeedback(feedbackData) {
    const res = await fetch('http://localhost:8080/api/feedback', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(feedbackData)
    });
    if (!res.ok) throw new Error('Не удалось отправить обращение');

    return await res.json();
}


export async function getAllFeedback() {
    const res = await fetch('http://localhost:8080/api/admin/feedback', {
        credentials: 'include'
    });
    if (!res.ok) {
        if (res.status === 403) {
            throw new Error('Доступ запрещен');
        }
        throw new Error('Не удалось загрузить обращения');
    }
    return await res.json();
}

export async function deleteFeedback(id) {
    const res = await fetch(`http://localhost:8080/api/admin/feedback/${id}`, {
        method: 'DELETE',
        credentials: 'include'
    });
    if (!res.ok) {
        if (res.status === 403) {
            throw new Error('Доступ запрещен');
        }
        throw new Error('Не удалось удалить обращение');
    }
    return await res.json();
}