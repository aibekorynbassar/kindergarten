// src/lib/api/staffAPI.js
// @ts-nocheck

/**
 * Получить список всех преподавателей
 * Возвращает массив объектов { name, imageUrl, profession }
 */
export async function getAllStaff() {
	try {
		const res = await fetch('http://localhost:8080/api/staff', {
			credentials: 'include'
		});

		if (!res.ok) {
			throw new Error('Ошибка при загрузке данных');
		}

		return await res.json();
	} catch (error) {
		console.error('Ошибка при получении списка преподавателей:', error);
		throw error;
	}
}

export async function createStaff(staffDto){
	const res = await fetch('http://localhost:8080/api/admin/staff', {
		method: 'POST',
		headers: {'Content-Type': 'application/json'},
		body: JSON.stringify(staffDto),
		credentials: 'include'
	});
	if(!res.ok){
		throw new Error('Не удалось создать преподавателя');
	}
	return await res.json();
}

export async function updateStaff(id, staffDto) {
	const res = await fetch(`http://localhost:8080/api/admin/staff/${id}`, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(staffDto),
		credentials: 'include'
	});
	if (!res.ok) throw new Error('Не удалось обновить преподавателя');
	return await res.json();
}

export async function deleteStaff(id) {
	const res = await fetch(`http://localhost:8080/api/admin/staff/${id}`, {
		method: 'DELETE',
		credentials: 'include'
	});
	if (!res.ok) throw new Error('Не удалось удалить преподавателя');
	return await res.json();
}
