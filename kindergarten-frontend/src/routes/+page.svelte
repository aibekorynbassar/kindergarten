<script>
	// @ts-nocheck
	import { onMount } from 'svelte';
	import { getPublishedNews } from '$lib/api/newsAPI.js';
	import { checkAuth } from '$lib/api/auth.js';
	import { lang, t } from '$lib/lang/index.js';
	import FeedbackForm from '$lib/components/FeedbackForm.svelte';
	import StaffList from '$lib/components/StaffList.svelte';

	// Переменные для хранения данных
	let news = []; // Список новостей
	let loading = true; // Идет ли загрузка
	let error = null; // Есть ли ошибка

	// локальные текстовые переменные
	let metaTitle = '';
	let heroTitle = '';
	let heroSubtitle = '';
	let aboutTitle = '';
	let aboutText = '';
	let aboutList = [];
	let newsTitle = '';
	let newsLoading = '';
	let newsError = '';
	let newsRetry = '';
	let newsEmpty = '';
	let staffTitle = '';
	let feedbackTitle = '';
	let feedbackSubtitle = '';
	let mapTitle = '';

	$: {
		$lang;
		metaTitle = t('home.title') || 'Детский сад "Солнышко" - Главная';
		heroTitle = t('home.hero_title') || 'Добро пожаловать в детский сад "Солнышко"';
		heroSubtitle =
			t('home.hero_subtitle') ||
			'Место, где каждый ребенок может расти, учиться и играть в безопасной и заботливой среде';
		aboutTitle = t('home.about_title') || 'О нашем детском саду';
		aboutText =
			t('home.about_text') ||
			'Детский сад "Солнышко" - это современное образовательное учреждение, где дети получают всестороннее развитие в атмосфере любви и заботы.';

		// Здесь — безопасно нормализуем aboutList:
		const _about = t('home.about_list');
		if (Array.isArray(_about)) {
			aboutList = _about;
		} else if (typeof _about === 'string' && _about.trim() !== '') {
			// если вдруг в JSON хранится строка с разделителем, попробуем разделить по новой строке
			// или если просто одна строка — превратим в массив с одним элементом
			if (_about.includes('\n')) {
				aboutList = _about
					.split('\n')
					.map((s) => s.trim())
					.filter(Boolean);
			} else {
				aboutList = [_about];
			}
		}

		newsTitle = t('home.news_title') || 'Новости и события';
		newsLoading = t('home.news_loading') || 'Загрузка новостей...';
		newsError = t('home.news_error') || 'Не удалось загрузить новости';
		newsRetry = t('home.news_retry') || 'Попробовать снова';
		newsEmpty = t('home.news_empty') || 'Новостей пока нет';
		staffTitle = t('home.staff_title') || 'Наши преподаватели';
		feedbackTitle = t('home.feedback_title') || 'Остались вопросы?';
		feedbackSubtitle =
			t('home.feedback_subtitle') ||
			'Заполните форму ниже, и мы обязательно свяжемся с вами в ближайшее время';
		mapTitle = t('home.map_title') || 'Интерактивная карта — 2GIS';
	}
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
	<title>{metaTitle}</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<section
		class="hero mb-8 rounded-lg bg-gradient-to-r from-yellow-400 to-orange-500 p-8 text-white"
	>
		<div class="text-center">
			<h1 class="mb-4 text-4xl font-bold">{heroTitle}</h1>
			<p class="mb-6 text-xl">{heroSubtitle}</p>
		</div>
	</section>

	<section class="about mb-12">
		<div class="grid items-center gap-8 md:grid-cols-2">
			<div>
				<h2 class="mb-4 text-3xl font-bold text-gray-800">{aboutTitle}</h2>
				<p class="mb-4 text-gray-600">{aboutText}</p>
				<ul class="space-y-2 text-gray-600">
					{#each aboutList || [] as item}
						<li>✓ {item}</li>
					{/each}
				</ul>
			</div>
			<div class="rounded-lg bg-blue-100 p-6">
				<img
					src="/images/main.png"
					alt="Фото детского сада"
					loading="lazy"
					class="h-64 w-full rounded-lg object-cover"
				/>
			</div>
		</div>
	</section>

	<section class="news mb-12">
		<h2 class="mb-6 text-center text-3xl font-bold text-gray-800">{newsTitle}</h2>

		{#if loading}
			<div class="py-8 text-center">
				<div
					class="inline-block h-8 w-8 animate-spin rounded-full border-b-2 border-blue-500"
				></div>
				<p class="mt-2 text-gray-600">{newsLoading}</p>
			</div>
		{:else if error}
			<div class="py-8 text-center text-red-600">
				<p>{error}</p>
				<button
					on:click={loadNews}
					class="mt-4 rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
				>
					{newsRetry}
				</button>
			</div>
		{:else if news.length === 0}
			<div class="py-8 text-center text-gray-600">
				<p>{newsEmpty}</p>
			</div>
		{:else}
			<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
				{#each news as article}
					<div
						class="news-card overflow-hidden rounded-lg bg-white shadow-md transition hover:shadow-lg"
					>
						{#if article.imageUrl}
							<img
								src={article.imageUrl}
								alt={article.title}
								loading="lazy"
								class="h-auto max-h-96 w-full object-cover"
							/>
						{:else}
							<div class="w-full overflow-hidden rounded-md">
								<img
									src="/path/to/photo.jpg"
									alt="Фото новости"
									class="h-auto max-h-96 w-full object-cover"
								/>
							</div>
						{/if}
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
	<section class="bg-gray-50 py-16">
		<div class="mx-auto max-w-7xl px-4 text-center">
			<h2 class="mb-14 text-4xl font-extrabold text-gray-800">{staffTitle}</h2>
			<StaffList />
		</div>
	</section>

	<section class="feedback mb-8">
		<div class="mx-auto w-full max-w-6xl">
			<div class="grid items-stretch gap-6 md:grid-cols-2">
				<!-- ЛЕВАЯ КОЛОНКА: flex-col, карта занимает всё оставшееся место -->
				<div class="order-1 flex flex-col space-y-4">
					<!-- КАРТОЧКА: заголовок и данные (КОНТАКТЫ) -->
					<div class="rounded-md bg-white p-6 shadow-sm">
						<div class="mb-2">
							<h3 class="text-2xl font-bold text-gray-800">КОНТАКТЫ</h3>
						</div>

						<p class="mb-2"><strong>Адрес:</strong> Lorem ipsum dolor sit amet, 12345</p>
						<p class="mb-2"><strong>Телефон:</strong> +7 (000) 000-00-00</p>
						<p class="mb-2"><strong>E-mail:</strong> lorem@ipsum.example</p>
						<p class="mt-3 text-sm text-gray-500">
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer nec odio. Praesent
							libero.
						</p>
					</div>

					<!-- ОТДЕЛЬНАЯ КАРТОЧКА: только карта, растягиваемая по высоте -->
					<div class="flex-1 overflow-hidden rounded-md bg-white p-0 shadow-sm">
						<iframe
							class="block h-full w-full border-0"
							title={mapTitle}
							frameborder="0"
							src="https://makemap.2gis.ru/widget?data=eJw9Ul2PojAU_S_dxzGz5UvEZB8QMohbkQ9nETfzgLSLYKEEK6LG_74V3W3StL3n3pvTc-4NsBaTlmCHsIrwtiBHMP19A_zSEDAFHyTlp5aAEWha1pCWD7iAC04feFhRNfoV5PDnPODYMSFCwclzZqIAk2PWFg0vWC0S_bn5Bhdt0HFrAtH3sPScFUzQq6h7neuQFnbA-aqBSAm4Z0kwWU62Fa3xZkH9nOWu3Te7OMszZVH60YLheXjOrqxD8kFD9YztlLBD5aJON8tTIhscKa9YjPc7p6dZfei2VVgklat565wv10nvRRAm17BAsdt75Sf3ShMu1x_lygnOSzlRN9dE8ur86tnmKa1ntfvgYZnDxjI9YCc33NI9Ly1X8Aq1nfNpuNWepjFm2G72eBN2q4ubJ3EP_QgFFbfOMFkHPLLDPLKDI7dU8fdBG9ezDIjMHCJfvB-a6AG33DerPHf_dRD3zFZ_CJEzRlkr5P0GZSX9I4vI1a0x6cFUgv_WfQTyp7mXwbqnsz4raj50EANQ1CkfjNf0d0mTdXU80uC7bEygZnyJ-gKD6UTT7l8jUKWNz47F09YboCkH02eupCiSJOmGrkojQB_wq9tYlwRojFVD0GOsEuQmoqkYD0ZpvCeEbocob0_k_hfvjtOQ"
							sandbox="allow-modals allow-forms allow-scripts allow-same-origin allow-popups allow-top-navigation-by-user-activation"
						></iframe>
					</div>
				</div>

				<!-- ПРАВАЯ КОЛОНКА: отдельный заголовок + отдельный блок с формой -->
				<div class="order-2 flex justify-end">
					<div class="w-full space-y-4 md:w-[520px]">
						<!-- ОТДЕЛЬНЫЙ КОНТЕЙНЕР С ЗАГОЛОВКОМ -->
						<div class="rounded-lg bg-white p-6 shadow-md">
							<h2 class="mb-2 text-3xl font-bold text-gray-800">{feedbackTitle}</h2>
							<p class="text-gray-600">
								{feedbackSubtitle}
							</p>
						</div>

						<!-- Если FeedbackForm сам рисует заголовок — передаем пустой title -->
						<FeedbackForm/>
					</div>
				</div>
			</div>
		</div>
	</section>
</div>
<footer class="bg-blue-600 py-6 text-center text-white">
	<p>&copy; {new Date().getFullYear()} Детский сад "Солнышко". Все права защищены.
	<br>
	" Разработка сайтов: "
	<a href="https://fixit-servis.kz/разработка-сайтов/">FIXIT Service</a>
	</p>
</footer>
<style>
	.container {
		max-width: 1440px;
	}
	body,
	h1,
	h2,
	h3 {
		font-family: Arial, 'Segoe UI', Tahoma, sans-serif !important;
	}
</style>
