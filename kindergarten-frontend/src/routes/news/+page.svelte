<script lang="ts">
	// @ts-nocheck
	import { onMount } from 'svelte';
	import { t } from '$lib/lang';
	import { getPublishedNews } from '$lib/api/newsAPI.js';
	let items = [];

	// простой пример — подставь реальный fetch из API
	onMount(async () => {
		try {
			items = await getPublishedNews();
		} catch (e) {
			console.error(e);
		}
	});
</script>

<section class="container mx-auto py-6">
	<h1 class="mb-4 text-2xl font-semibold">{t('header.news') || 'Новости'}</h1>

	{#if items.length === 0}
		<p>Новостей нет. Добавь первую.</p>
	{:else}
		<div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
			{#each items as item}
									<div
						class="news-card overflow-hidden rounded-lg bg-white shadow-md transition hover:shadow-lg"
					>
						{#if item.imageUrl}
							<img
								src={item.imageUrl}
								alt={item.title}
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
							<h3 class="mb-2 text-xl font-semibold text-gray-800">{item.title}</h3>
							<p class="mb-4 text-gray-600">{item.content}</p>
							<div class="text-sm text-gray-500">
								{new Date(item.createdAt).toLocaleDateString('ru-RU')}
							</div>
						</div>
					</div>
			{/each}
		</div>
	{/if}
</section>
