<script>
	// @ts-nocheck
	import { userStore, resetAuth } from '$lib/stores/userStore.js';
	import { onDestroy } from 'svelte';
	import { goto } from '$app/navigation';
	import { logout } from '$lib/api/auth';

	let user = { isLoggedIn: false, role: 'USER' };

	const unsubscribe = userStore.subscribe((value) => {
		user = value;
	});

	// Отписка при уничтожении компонента (по необходимости)
	onDestroy(unsubscribe);

	async function handleLogout() {
		try {
			// Реальный logout на сервере
			// await fetch('http://localhost:8080/logout', {
			// 	method: 'POST',
			// 	credentials: 'include'
			// });
			logout();
		} catch (error) {
			console.error('Logout error:', error);
		}

		// Обновляем стор на фронте
		userStore.set({ isLoggedIn: false, role: 'USER' });

		// Переход на главную
		goto('/');
	}
</script>

<!-- Шапка сайта -->
<header class="bg-blue-600 p-4 text-white">
	<nav class="mx-auto flex max-w-6xl items-center justify-between">
		<!-- Логотип/название -->
		<h1 class="text-xl font-bold">
			<a href="/" class="hover:text-blue-200">🌟 Детский сад "Солнышко"</a>
		</h1>

		<!-- Навигационное меню -->
		<div class="space-x-4">
			<a href="/" class="hover:text-blue-200">Главная</a>

			{#if user.isLoggedIn && user.role === 'ADMIN'}
				<button on:click={handleLogout} class="hover:text-blue-200">Выйти</button>
			{:else}
				<a href="/auth/login" class="hover:text-blue-200">Войти</a>
			{/if}
		</div>
	</nav>
</header>
