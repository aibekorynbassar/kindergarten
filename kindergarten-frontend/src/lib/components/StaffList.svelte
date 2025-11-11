<!-- src/lib/components/StaffList.svelte -->
<script>
	// @ts-nocheck
	import { onMount } from 'svelte';
	import { getAllStaff } from '$lib/api/staffAPI.js';

	let staff = [];
	let loading = true;
	let error = null;

	async function loadStaff() {
		try {
			loading = true;
			error = null;
			staff = await getAllStaff();
		} catch (err) {
			error = 'Не удалось загрузить список сотрудников';
			console.error('Error loading staff:', err);
		} finally {
			loading = false;
		}
	}

	onMount(loadStaff);
</script>

		{#if loading}
			<div class="py-8 text-center">
				<div class="inline-block h-8 w-8 animate-spin rounded-full border-b-2 border-gray-500"></div>
				<p class="mt-2 text-gray-600 text-lg">Загрузка данных...</p>
			</div>
		{:else if error}
			<div class="py-8 text-center text-red-600 text-lg">
				<p>{error}</p>
				<button
					class="mt-4 rounded bg-blue-500 px-6 py-3 text-white hover:bg-blue-600 text-base"
					on:click={loadStaff}
				>
					Попробовать снова
				</button>
			</div>
		{:else if staff.length === 0}
			<p class="py-8 text-center text-gray-600 text-lg">Пока нет данных о преподавателях</p>
		{:else}
			<!-- Контейнер карточек -->
			<div class="flex flex-wrap justify-center gap-x-14 gap-y-14">
				{#each staff as member (member.id)}
					<div
						class="w-[250px] sm:w-[220px] md:w-[240px] lg:w-[260px] text-center bg-white border border-gray-200 rounded-3xl shadow-lg hover:shadow-2xl transition duration-300 p-6"
					>
						<!-- Фото преподавателя -->
						<div
							class="group relative mx-auto h-[340px] w-[230px] overflow-hidden rounded-[50%/60%] border border-gray-300 bg-gray-100 transition-all duration-300 ease-in-out"
						>
							<img
								src={member.imageUrl || '/images/defaultStaff.jpg'}
								alt={member.name}
								class="h-full w-full object-cover object-center transform transition-transform duration-500 group-hover:scale-110"
								on:error={(e) => {
									if (e.target.src.includes('defaultStaff.jpg')) return;
									e.target.src = '/images/defaultStaff.jpg';
								}}
							/>
						</div>

						<!-- Имя -->
						<h3 class="mt-5 text-lg md:text-xl font-bold text-gray-800 leading-snug uppercase tracking-wide">
							{member.name}
						</h3>

						<!-- Должность -->
						<p class="mt-2 text-base md:text-lg text-gray-600">{member.profession}</p>
					</div>
				{/each}
			</div>
		{/if}

<style>
	@media (max-width: 640px) {
		img {
			object-position: center top;
		}
	}

	/* Дополнительная корректировка теней и рамок */
	.shadow-lg {
		box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
	}

	.hover\:shadow-2xl:hover {
		box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
	}
</style>