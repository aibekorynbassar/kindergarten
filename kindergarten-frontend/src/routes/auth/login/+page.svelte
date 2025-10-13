<script>
	// @ts-nocheck
	import { goto } from '$app/navigation';
	import { login } from '$lib/api/auth.js';
	import { setLoggedIn } from '$lib/stores/userStore.js';

	let username = '';
	let password = '';
	let error = '';
	let loading = false;


	async function handleLogin() {
		if (!username.trim() || !password.trim()) {
			error = 'Заполните все поля';
			return;
		}

		loading = true;
		error = '';

		try {
			await login({ username, password });
			setLoggedIn();
			goto('/admin');
		} catch (err) {
			error = 'Неверный логин или пароль';
			console.error(err);
		} finally {
			loading = false;
		}
	}
</script>

<svelte:head>
	<title>Вход — Детский сад «Солнышко»</title>
</svelte:head>

<div class="flex min-h-screen items-center justify-center bg-gray-50 px-4 py-12">
	<div class="w-full max-w-md space-y-8">
		<div class="text-center">
			<h2 class="mt-6 text-3xl font-extrabold text-gray-900">Вход в систему</h2>
			<p class="mt-2 text-sm text-gray-600">Детский сад «Солнышко»</p>
		</div>

		<form class="mt-8 space-y-6" on:submit|preventDefault={handleLogin}>
			<div class="space-y-4">
				<div>
					<label for="username" class="block text-sm font-medium text-gray-700"
						>Имя пользователя</label
					>
					<input
						id="username"
						name="username"
						bind:value={username}
						type="text"
						required
						class="mt-1 block w-full rounded-md border-gray-300 focus:border-blue-500 focus:ring-blue-500"
						placeholder="Введите имя пользователя"
					/>
				</div>
				<div>
					<label for="password" class="block text-sm font-medium text-gray-700">Пароль</label>
					<input
						id="password"
						name="password"
						bind:value={password}
						type="password"
						required
						class="mt-1 block w-full rounded-md border-gray-300 focus:border-blue-500 focus:ring-blue-500"
						placeholder="Введите пароль"
					/>
				</div>
			</div>

			{#if error}
				<div class="rounded bg-red-50 p-2 text-center text-sm text-red-600">
					{error}
				</div>
			{/if}

			<div>
				<button
					type="submit"
					disabled={loading}
					class="flex w-full justify-center rounded-md border border-transparent bg-blue-600 px-4 py-2 text-white hover:bg-blue-700 disabled:opacity-50"
				>
					{#if loading}
						<svg
							class="mr-3 h-5 w-5 animate-spin text-white"
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
						>
							<circle
								class="opacity-25"
								cx="12"
								cy="12"
								r="10"
								stroke="currentColor"
								stroke-width="4"
							></circle>
							<path
								class="opacity-75"
								fill="currentColor"
								d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
							></path>
						</svg>
						Вход...
					{:else}
						Войти
					{/if}
				</button>
			</div>

			<div class="text-center text-sm text-gray-600">
				<p>Для демонстрации: admin / admin123</p>
			</div>
		</form>
	</div>
</div>
