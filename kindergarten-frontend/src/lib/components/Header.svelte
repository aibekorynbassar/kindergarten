<script>
	// @ts-nocheck
	import { onMount, onDestroy } from 'svelte';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import { writable } from 'svelte/store';
	import { lang, setLang, t } from '$lib/lang/index.js'; // путь поправь если нужно

	import { userStore } from '$lib/stores/userStore.js';
	import { logout } from '$lib/api/auth';

	// user
	let user = { isLoggedIn: false, role: 'USER' };
	const unsubscribeUser = userStore.subscribe((v) => (user = v));
	onDestroy(unsubscribeUser);

	// прочие переменные
	const mobileOpen = writable(false);
	let aboutOpen = false;
	let scrolled = false;
	let hovered = false;
	const scrollThreshold = 10;

	// объявляем значения, чтобы SSR не падал
	let nav = [];
	let aboutMenu = [];
	let siteTitle = '';
	let siteTagline = '';
	let siteAddress = '';
	let loginLabel = '';
	let logoutLabel = '';

	// следим за путём
	$: pathname = $page.url?.pathname || '/';

	// переключение языка
	function toggleLang() {
		const next = $lang === 'ru' ? 'kk' : 'ru';
		setLang(next);
	}

	// реактивно заполняем навигацию и строки: явно зависим от $lang
	$: {
		$lang; // <- фикс: Svelte увидит зависимость и пересчитает блок при смене языка
		nav = [
			{ href: '/', label: t('header.home') || 'Главная' },
			{ href: '/about', label: t('header.about') || 'О дет саде' },
			{ href: '/documents', label: t('header.documents') || 'Документы' },
			{ href: '/news', label: t('header.news') || 'Новости' }
		];

		aboutMenu = [
			{ href: '/about/team', label: t('about.team') || 'Коллектив' },
			{ href: '/about/schedule', label: t('about.schedule') || 'Расписание' },
			{ href: '/about/jobs', label: t('about.vacancies') || 'Вакансии' }
		];

		siteTitle = t('header.title') || '🌟 Детский сад "Солнышко"';
		siteTagline = t('header.tagline') || 'Тепло. Забота. Развитие.';
		siteAddress = t('header.address') || 'Проспект Абулхаир хана 25А';
		loginLabel = t('header.login') || 'Войти';
		logoutLabel = t('header.logout') || 'Выйти';
	}

	// scroll handling (как было)
	function handleScroll() {
		if (typeof window === 'undefined') return;
		scrolled = window.scrollY > scrollThreshold;
	}
	onMount(() => {
		if (typeof window === 'undefined') return;
		handleScroll();
		window.addEventListener('scroll', handleScroll, { passive: true });
	});
	onDestroy(() => {
		if (typeof window === 'undefined') return;
		window.removeEventListener('scroll', handleScroll);
	});

	async function handleLogout() {
		try {
			await logout();
		} catch (e) {
			console.error(e);
		}
		userStore.set({ isLoggedIn: false, role: 'USER' });
		goto('/');
	}
</script>

<header class="bg-blue-600 text-white">
	<div class="mx-auto flex max-w-6xl items-center justify-between px-4 py-3">
		<!-- логотип / название -->
		<a href="/" class="flex items-center gap-3">
			<img src="images/logo.png" alt="Логотип" class="h-10 w-auto" />
			<div class="hidden sm:block">
				<div class="text-sm font-semibold">{@html siteTitle}</div>
				<div class="text-xs text-blue-100">{siteTagline}</div>
			</div>
		</a>

		<!-- адрес и соцсети (скрыты на маленьких экранах) -->
		<div class="hidden items-center gap-6 text-sm md:flex">
			<div class="flex items-center gap-2 text-blue-100">
				<svg class="h-4 w-4" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
					<path d="M12 2C8.14 2 5 5.14 5 9c0 7 7 13 7 13s7-6 7-13c0-3.86-3.14-7-7-7z" />
				</svg>
				<span>{siteAddress}</span>
			</div>

			<div class="flex items-center space-x-3 text-blue-100">
				<a
					href="https://wa.me/77777777777"
					aria-label="WhatsApp"
					class="inline-flex items-center hover:text-white"
				>
					<i class="bi bi-whatsapp text-lg"></i>
				</a>

				<a
					href="https://instagram.com/your_profile"
					aria-label="Instagram"
					class="ml-3 inline-flex items-center hover:text-white"
				>
					<i class="bi bi-instagram text-lg"></i>
				</a>
			</div>
		</div>

		<!-- Вход/Выход, кнопки языка, бургер -->
		<div class="flex items-center gap-3">
			{#if user.isLoggedIn && user.role === 'ADMIN'}
				<button on:click={handleLogout} class="text-sm hover:text-blue-200">{logoutLabel}</button>
			{:else}
				<a href="/auth/login" class="text-sm hover:text-blue-200">{loginLabel}</a>
			{/if}

			<!-- бургер для мобилки -->
			<button
				class="rounded p-2 md:hidden"
				on:click={() => mobileOpen.update((v) => !v)}
				aria-label="Открыть меню"
			>
				<svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M4 6h16M4 12h16M4 18h16"
					/>
				</svg>
			</button>
		</div>
	</div>
</header>

<nav
	aria-label="Главное навигационное меню"
	on:mouseenter={() => (hovered = true)}
	on:mouseleave={() => (hovered = false)}
	class={`sticky top-0 z-40 text-white transition-colors duration-300 ${!scrolled || hovered ? 'bg-orange-500' : 'bg-orange-500/50 backdrop-blur-sm'}`}
>
	<div class="mx-auto max-w-6xl px-4">
		<!-- Десктоп -->
		<div class="hidden items-center justify-between md:flex">
			<ul class="flex gap-6 py-3">
				{#each nav as item}
					{#if item.href === '/about'}
						<div
							class="relative"
							role="group"
							on:mouseenter={() => (aboutOpen = true)}
							on:mouseleave={() => (aboutOpen = false)}
						>
							<a
								href={item.href}
								class="flex items-center gap-2 rounded-md px-3 py-2 hover:bg-orange-500 hover:bg-yellow-400/50 hover:text-white"
								aria-haspopup="true"
								aria-expanded={aboutOpen}
								on:click|preventDefault={() => (aboutOpen = !aboutOpen)}
							>
								<span class={pathname === item.href ? 'font-semibold' : ''}>{item.label}</span>
								<svg class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
									<path
										fill-rule="evenodd"
										d="M5.23 7.21a.75.75 0 011.06-.02L10 10.66l3.71-3.47a.75.75 0 111.04 1.08l-4.25 3.98a.75.75 0 01-1.04 0L5.25 8.27a.75.75 0 01-.02-1.06z"
										clip-rule="evenodd"
									/>
								</svg>
							</a>

							{#if aboutOpen}
								<ul
									role="menu"
									aria-label="О дет саде меню"
									on:mouseenter={() => ((aboutOpen = true), (hovered = true))}
									on:mouseleave={() => ((aboutOpen = false), (hovered = false))}
									class={`absolute top-full left-0 z-50 w-48 rounded-md shadow-lg ring-1 ring-orange-700/30 ${!scrolled || hovered ? 'bg-orange-500' : 'bg-orange-500/50 backdrop-blur-sm'}`}
								>
									{#each aboutMenu as sub}
										<li>
											<a
												href={sub.href}
												role="menuitem"
												class="block px-4 py-2 text-sm text-white hover:bg-yellow-400/50"
											>
												{sub.label}
											</a>
										</li>
									{/each}
								</ul>
							{/if}
						</div>
					{:else}
						<a
							href={item.href}
							class="rounded-md px-3 py-2 hover:bg-orange-500 hover:bg-yellow-400/50 hover:text-white {pathname ===
							item.href
								? 'font-semibold'
								: ''}"
						>
							{item.label}
						</a>
					{/if}
				{/each}
				<!-- ПУНКТ: переключатель языка в конце списка -->
				<li>
					<button
						on:click={toggleLang}
						class="rounded-md px-3 py-2 hover:bg-orange-500 hover:bg-yellow-400/50 hover:text-white"
						aria-pressed={$lang !== 'ru'}
						title="Сменить язык"
					>
						{#if $lang === 'ru'}
							Қазақша
						{:else}
							Русский
						{/if}
					</button>
				</li>
			</ul>

			<div class="py-3 text-sm text-white/90">
				<a href="tel:+7700xxxxxxx" class="hover:underline"
					>{t('header.contactPhone') || '+7 (700) XXX-XX-XX'}</a
				>
			</div>
		</div>

		<!-- Мобильная навигация -->
		<div class="md:hidden">
			{#if $mobileOpen}
				<ul class="flex flex-col gap-1 py-2">
					{#each nav as item}
						<li>
							<a
								href={item.href}
								class="block rounded px-3 py-2 text-sm text-white hover:bg-orange-600"
								on:click={() => mobileOpen.set(false)}
								aria-current={pathname === item.href ? 'page' : undefined}
							>
								{item.label}
							</a>
						</li>
					{/each}
					<!-- переключатель языка внизу моб. меню -->
					<li>
						<button
							class="w-full rounded px-4 py-2 text-left text-sm"
							on:click={() => {
								toggleLang();
								mobileOpen.set(false);
							}}
							aria-pressed={$lang !== 'ru'}
						>
							{#if $lang === 'ru'}
								Қазақша
							{:else}
								Русский
							{/if}
						</button>
					</li>
				</ul>
			{/if}
		</div>
	</div>
</nav>

<style>
	/* корректируй стили под проект по необходимости */
</style>
