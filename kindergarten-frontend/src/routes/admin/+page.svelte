<script>
	// @ts-nocheck
	import NewsTab from './tabs/NewsTab.svelte';
	import FeedbackTab from './tabs/FeedbackTab.svelte';
	import StaffTab from './tabs/StaffTab.svelte';
	import { userStore } from '$lib/stores/userStore.js';

	// data приходит из +page.ts (load)
	export let data;

	let activeTab = 'news';
	let user = { isLoggedIn: false, role: 'USER' };

	userStore.subscribe((value) => {
		user = value;
	});

	function switchTab(tab) {
		activeTab = tab;
	}
</script>

<svelte:head>
	<title>Админ панель - Детский сад "Солнышко"</title>
</svelte:head>

<div class="container mx-auto px-4 py-8">
	<h1 class="mb-6 text-3xl font-bold">Панель управления данными</h1>

	<!-- Переключатель вкладок -->
	<div class="mb-6 border-b">
		<nav class="-mb-px flex space-x-8">
			<button
				on:click={() => switchTab('news')}
				class="border-b-2 px-1 py-2 text-sm font-medium {activeTab === 'news'
					? 'border-blue-500 text-blue-600'
					: 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'}"
			>
				Управление новостями
			</button>
			<button
				on:click={() => switchTab('feedback')}
				class="border-b-2 px-1 py-2 text-sm font-medium {activeTab === 'feedback'
					? 'border-blue-500 text-blue-600'
					: 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'}"
			>
				Обратная связь
			</button>
			<button
				on:click={() => switchTab('staff')}
				class="border-b-2 px-1 py-2 text-sm font-medium {activeTab === 'staff'
					? 'border-blue-500 text-blue-600'
					: 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'}"
			>
				Преподаватели
			</button>
		</nav>
	</div>

	<!-- Вкладки (вставляем компоненты) -->
	{#if activeTab === 'news'}
		<!-- Передаём server-side data напрямую в NewsTab -->
		<NewsTab {data} />
	{:else if activeTab === 'feedback'}
		<FeedbackTab />
	{:else if activeTab === 'staff'}
		<StaffTab />
	{/if}
</div>

<style>
	.container {
		max-width: 1000px;
	}
</style>
