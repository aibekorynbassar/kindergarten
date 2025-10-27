<script>
	// @ts-nocheck
	import { onMount } from 'svelte';
	import { getPublishedNews } from '$lib/api/newsAPI.js';
	import { checkAuth } from '$lib/api/auth.js';
	import FeedbackForm from '$lib/components/FeedbackForm.svelte';
	import StaffList from '$lib/components/StaffList.svelte';

	// Переменные для хранения данных
	let news = []; // Список новостей
	let loading = true; // Идет ли загрузка
	let error = null; // Есть ли ошибка

	// Загружаем новости при открытии страницы
	onMount(async () => {
		checkAuth(); // Проверяем авторизацию пользователя
		await loadNews();
	});

	// Функция загрузки новостей с сервера
	async function loadNews() {
		try {
			loading = true;
			error = null;
			news = await getPublishedNews(); // Запрос к API
		} catch (err) {
			error = 'Не удалось загрузить новости';
			console.error('Error loading news:', err);
		} finally {
			loading = false; // Загрузка завершена в любом случае
		}
	}
</script>

<!-- Заголовок страницы в браузере -->
<svelte:head>
	<title>Детский сад "Солнышко" - Главная</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<!-- Главный баннер -->
	<section
		class="hero mb-8 rounded-lg bg-gradient-to-r from-yellow-400 to-orange-500 p-8 text-white"
	>
		<div class="text-center">
			<h1 class="mb-4 text-4xl font-bold">Добро пожаловать в детский сад "Солнышко"</h1>
			<p class="mb-6 text-xl">
				Место, где каждый ребенок может расти, учиться и играть в безопасной и заботливой среде
			</p>
		</div>
	</section>

	<!-- Информация о детском саде -->
	<section class="about mb-12">
		<div class="grid items-center gap-8 md:grid-cols-2">
			<div>
				<h2 class="mb-4 text-3xl font-bold text-gray-800">О нашем детском саду</h2>
				<p class="mb-4 text-gray-600">
					Детский сад "Солнышко" - это современное образовательное учреждение, где дети получают
					всестороннее развитие в атмосфере любви и заботы.
				</p>
				<ul class="space-y-2 text-gray-600">
					<li>✓ Индивидуальный подход к каждому ребенку</li>
					<li>✓ Современные методики развития</li>
					<li>✓ Здоровое питание</li>
					<li>✓ Безопасная игровая среда</li>
				</ul>
			</div>
			<div class="rounded-lg bg-blue-100 p-6">
				<div
					class="flex h-64 w-full items-center justify-center rounded-lg bg-gray-300 text-gray-600"
				>
					[Фото детского сада]
				</div>
			</div>
		</div>
	</section>

	<!-- Раздел новостей -->
	<section class="news mb-12">
		<h2 class="mb-6 text-center text-3xl font-bold text-gray-800">Новости и события</h2>

		<!-- Показываем разные состояния: загрузка, ошибка, данные -->
		{#if loading}
			<!-- Состояние загрузки -->
			<div class="py-8 text-center">
				<div
					class="inline-block h-8 w-8 animate-spin rounded-full border-b-2 border-blue-500"
				></div>
				<p class="mt-2 text-gray-600">Загрузка новостей...</p>
			</div>
		{:else if error}
			<!-- Состояние ошибки -->
			<div class="py-8 text-center text-red-600">
				<p>{error}</p>
				<button
					on:click={loadNews}
					class="mt-4 rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
				>
					Попробовать снова
				</button>
			</div>
		{:else if news.length === 0}
			<!-- Нет новостей -->
			<div class="py-8 text-center text-gray-600">
				<p>Новостей пока нет</p>
			</div>
		{:else}
			<!-- Список новостей -->
			<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
				{#each news as article}
					<div class="overflow-hidden rounded-lg bg-white shadow-md transition hover:shadow-lg">
						<!-- Изображение новости (если есть) -->
						{#if article.imageUrl}
							<img src={article.imageUrl} alt={article.title} class="h-48 w-full object-cover" />
						{:else}
							<div class="flex h-48 w-full items-center justify-center bg-gray-300 text-gray-600">
								[Фото новости]
							</div>
						{/if}

						<!-- Содержимое карточки новости -->
						<div class="p-6">
							<h3 class="mb-2 text-xl font-semibold text-gray-800">{article.title}</h3>
							<p class="mb-4 text-gray-600">{article.content}</p>
							<div class="text-sm text-gray-500">
								{new Date(article.createdAt).toLocaleDateString('ru-RU')}
							</div>
						</div>
					</div>
				{/each}
			</div>
		{/if}
	</section>

		<StaffList />

	<!-- Форма обратной связи -->

	<section class="feedback mb-8">
		<div class="mb-8 text-center">
			<h2 class="mb-4 text-3xl font-bold text-gray-800">Остались вопросы?</h2>

			<p class="text-lg text-gray-600">
				Заполните форму ниже, и мы обязательно свяжемся с вами в ближайшее время
			</p>
		</div>

		<div class="mx-auto max-w-2xl">
			<FeedbackForm />
		</div>
	</section>
</div>

<style>
	.container {
		max-width: 1200px;
	}
</style>
