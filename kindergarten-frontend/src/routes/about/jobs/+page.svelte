<script lang="ts">
	import { t } from '$lib/lang';
	// Заглушки вакансий
	let vacancies = [
		{
			id: 1,
			title: 'Воспитатель (полный день)',
			desc: 'Требования: педагогическое образование. Зарплата по договоренности.'
		},
		{
			id: 2,
			title: 'Медсестра',
			desc: 'Обязанности: медицинский контроль детей, оформление медкнижки.'
		}
	];
</script>

<section>
	<h1 class="mb-4 text-2xl font-semibold">{t('about.vacancies') || 'Вакансии'}</h1>

	{#if vacancies.length === 0}
		<p>Сейчас вакансий нет.</p>
	{:else}
		<ul class="space-y-3">
			{#each vacancies as v}
				<li class="rounded border mb-4 p-3">
					<h3 class="font-medium">{v.title}</h3>
					<p class="mt-1 text-sm">{v.desc}</p>
				</li>
			{/each}
		</ul>
	{/if}
</section>
