<script lang="ts">
	import { t } from '$lib/lang';
	// Пример расписания по группам
	const schedule = [
		{ group: 'Младшая', arrival: '08:00', routine: 'Игры, прогулка, сон' },
		{ group: 'Средняя', arrival: '08:00', routine: 'Игры, развивающие занятия, прогулка' },
		{ group: 'Старшая', arrival: '08:00', routine: 'Подготовка к школе, занятия, прогулка' }
	];
</script>

<section>
	<h1 class="mb-4 text-center text-2xl font-semibold">{t('about.schedule') || 'Расписание'}</h1>

	<p class="mb-4">Режим дня и основные временные отметки по группам (пример).</p>

	<table class="w-full text-center table-auto border-collapse">
		<thead>
			<tr class="bg-gray-100">
				<th class="p-2">Группа</th>
				<th class="p-2">Время прихода</th>
				<th class="p-2">Режим</th>
			</tr>
		</thead>
		<tbody>
			{#each schedule as s}
				<tr class="border-t">
					<td class="p-2">{s.group}</td>
					<td class="p-2">{s.arrival}</td>
					<td class="p-2">{s.routine}</td>
				</tr>
			{/each}
		</tbody>
	</table>
</section>
