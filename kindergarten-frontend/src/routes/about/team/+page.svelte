<script lang="ts">
	// @ts-nocheck
	import { onMount } from 'svelte';
	import { t } from '$lib/lang';
	import * as staffAPI from '$lib/api/staffAPI.js'; // существующий API (fetch на /api/staff)
	// Путь к локальному файлу с командой — поменяй, если у тебя другой.
	const LOCAL_TEAM_PATHS = [
		'$lib/data/team.js',
		'$lib/data/team.ts',
		'$lib/data/team.json',
		'$lib/data/team/index.js',
		'$lib/data/team/index.json'
	];

	let staff = [];
	let loading = true;
	let error = null;

	onMount(async () => {
		loading = true;
		error = null;

		// 1) Попытка динамически импортировать локальный модуль с командой (если он есть)
		for (const p of LOCAL_TEAM_PATHS) {
			try {
				// динамический импорт не падает сборкой, если модуль отсутствует — он вернёт ошибку и мы перейдём к следующему
				const mod = await import(p);
				const data = mod?.default ?? mod;
				if (Array.isArray(data) && data.length > 0) {
					staff = data;
					loading = false;
					return;
				}
			} catch (e) {
				// молча пробрасываем — модуль может не существовать
			}
		}

		// 2) Если локального файла нет — берем с бэкенда через staffAPI
		try {
			const res = await staffAPI.getAllStaff();
			if (Array.isArray(res) && res.length > 0) {
				staff = res;
				loading = false;
				return;
			}
		} catch (e) {
			console.error('Ошибка при загрузке преподавателей через staffAPI:', e);
			// продолжим к заглушкам
		}

		// 3) Фоллбэк: простые заглушки (lorem)
		staff = [
			{
				name: 'Иванова О. П.',
				profession: 'Воспитатель',
				imageUrl: '/images/defaultStaff.jpg',
				bio: 'Стаж 10 лет. Любит рукоделие.'
			},
			{
				name: 'Петрова А. К.',
				profession: 'Музыкальный руководитель',
				imageUrl: '/images/defaultStaff.jpg',
				bio: 'Ведёт музыкальные занятия.'
			},
			{
				name: 'Сидоров Н. В.',
				profession: 'Инструктор по физкультуре',
				imageUrl: '/images/defaultStaff.jpg',
				bio: 'Отвечает за утреннюю зарядку.'
			}
		];
		loading = false;
		error = 'Используются локальные заглушки — реальные данные не найдены.';
	});
</script>

<section class="container mx-auto py-6">
	<h1 class="mb-4 text-2xl font-semibold">{t('about.team') || 'Коллектив'}</h1>

	{#if loading}
		<p>Загрузка...</p>
	{:else}
		{#if error}
			<p class="mb-4 text-sm text-red-600">{error}</p>
		{/if}

		{#if staff.length === 0}
			<p>Преподаватели не найдены.</p>
		{:else}
			<!-- Контейнер карточек -->
			<div class="flex flex-wrap justify-center gap-x-14 gap-y-14">
				{#each staff as member (member.id)}
					<div
						class="w-[250px] rounded-3xl border border-gray-200 bg-white p-6 text-center shadow-lg transition duration-300 hover:shadow-2xl sm:w-[220px] md:w-[240px] lg:w-[260px]"
					>
						<!-- Фото преподавателя -->
						<div
							class="group relative mx-auto h-[340px] w-[230px] overflow-hidden rounded-[50%/60%] border border-gray-300 bg-gray-100 transition-all duration-300 ease-in-out"
						>
							<img
								src={member.imageUrl || '/images/defaultStaff.jpg'}
								alt={member.name}
								class="h-full w-full transform object-cover object-center transition-transform duration-500 group-hover:scale-110"
								on:error={(e) => {
									if (e.target.src.includes('defaultStaff.jpg')) return;
									e.target.src = '/images/defaultStaff.jpg';
								}}
							/>
						</div>

						<!-- Имя -->
						<h3
							class="mt-5 text-lg leading-snug font-bold tracking-wide text-gray-800 uppercase md:text-xl"
						>
							{member.name}
						</h3>

						<!-- Должность -->
						<p class="mt-2 text-base text-gray-600 md:text-lg">{member.profession}</p>
					</div>
				{/each}
			</div>
		{/if}
	{/if}
</section>
