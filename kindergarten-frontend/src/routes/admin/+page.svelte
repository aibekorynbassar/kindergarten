<script>
	// @ts-nocheck
	import * as newsAPI from '$lib/api/newsAPI.js';
	import * as feedbackAPI from '$lib/api/feedbackAPI.js';
	import { userStore } from '$lib/stores/userStore.js';
	import { goto } from '$app/navigation';

	// Переменные состояния
	export let data;
	let news = data.news || []; // Список всех новостей
	let feedbackList = []; // Список обращений
	let activeTab = 'news'; // Активная вкладка: 'news' или 'feedback'
	let loadingFeedback = false;

	// Переменные для новостей
	let showCreateForm = false; // Показывать ли форму создания
	let editingArticle = null; // Редактируемая новость
	let newArticle = {
		// Данные новой новости
		title: '',
		content: '',
		imageUrl: '',
		published: false
	};

	let user = { isLoggedIn: false, role: 'USER' };

	// Подписываемся на изменения пользователя
	userStore.subscribe((value) => {
		user = value;
	});

	// Загрузка всех новостей
	async function loadNews() {
		try {
			news = await newsAPI.getAdminNews(fetch);
		} catch (error) {
			console.error('Error loading news:', error);
		}
	}

	// Загрузка обращений
	async function loadFeedback() {
		try {
			loadingFeedback = true;
			feedbackList = await feedbackAPI.getAllFeedback();
		} catch (error) {
			console.error('Error loading feedback:', error);
			feedbackList = [];
		} finally {
			loadingFeedback = false;
		}
	}

	// Переключение вкладок
	function switchTab(tab) {
		activeTab = tab;
		if (tab === 'feedback' && feedbackList.length === 0) {
			loadFeedback();
		}
	}

	// Создание новой новости
	async function createNews() {
		try {
			await newsAPI.createNews(newArticle);
			showCreateForm = false; // Скрываем форму
			newArticle = { title: '', content: '', imageUrl: '', published: false }; // Очищаем форму
			await loadNews(); // Перезагружаем список
		} catch (error) {
			console.error('Error creating news:', error);
		}
	}

	// Обновление существующей новости
	async function updateNews() {
		try {
			await newsAPI.updateNews(editingArticle.id, editingArticle);
			editingArticle = null; // Закрываем форму редактирования
			await loadNews(); // Перезагружаем список
		} catch (error) {
			console.error('Error updating news:', error);
		}
	}

	// Удаление новости
	async function deleteNews(id) {
		if (confirm('Вы уверены, что хотите удалить эту новость?')) {
			try {
				await newsAPI.deleteNews(id);
				await loadNews(); // Перезагружаем список
			} catch (error) {
				console.error('Error deleting news:', error);
			}
		}
	}

	// Удаление обращения
	async function deleteFeedback(id) {
		if (confirm('Вы уверены, что хотите удалить это обращение?')) {
			try {
				await feedbackAPI.deleteFeedback(id);
				await loadFeedback(); // Перезагружаем список
			} catch (error) {
				console.error('Error deleting feedback:', error);
			}
		}
	}

	// Начать редактирование новости
	function startEditing(article) {
		editingArticle = { ...article }; // Копируем данные для редактирования
	}

	// Отменить редактирование
	function cancelEditing() {
		editingArticle = null;
	}

	// Форматирование даты
	function formatDate(dateString) {
		return new Date(dateString).toLocaleString('ru-RU');
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
				Обратная связь ({feedbackList.length})
			</button>
		</nav>
	</div>

	<!-- Вкладка управления новостями -->
	{#if activeTab === 'news'}
		<!-- Кнопка добавления новости -->
		<div class="mb-4">
			<button
				class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
				on:click={() => (showCreateForm = !showCreateForm)}
			>
				{showCreateForm ? 'Отмена' : 'Добавить новость'}
			</button>
		</div>

		<!-- Форма создания новости -->
		{#if showCreateForm}
			<div class="create-form mb-6 rounded bg-gray-100 p-4">
				<h3 class="mb-4 text-xl font-semibold">Создать новость</h3>

				<!-- Поля формы -->
				<input
					bind:value={newArticle.title}
					placeholder="Заголовок"
					class="mb-3 w-full rounded border p-2"
				/>
				<textarea
					bind:value={newArticle.content}
					placeholder="Содержание"
					rows="5"
					class="mb-3 w-full rounded border p-2"
				></textarea>
				<input
					bind:value={newArticle.imageUrl}
					placeholder="URL изображения"
					class="mb-3 w-full rounded border p-2"
				/>

				<!-- Чекбокс публикации -->
				<label class="mb-3 flex items-center">
					<input type="checkbox" bind:checked={newArticle.published} class="mr-2" />
					Опубликовать
				</label>

				<!-- Кнопка создания -->
				<button
					on:click={createNews}
					class="rounded bg-green-500 px-4 py-2 text-white hover:bg-green-600"
				>
					Создать
				</button>
			</div>
		{/if}

		<!-- Форма редактирования (если редактируем) -->
		{#if editingArticle}
			<div class="edit-form mb-6 rounded bg-yellow-100 p-4">
				<h3 class="mb-4 text-xl font-semibold">Редактировать новость</h3>

				<!-- Поля формы редактирования -->
				<input
					bind:value={editingArticle.title}
					placeholder="Заголовок"
					class="mb-3 w-full rounded border p-2"
				/>
				<textarea
					bind:value={editingArticle.content}
					placeholder="Содержание"
					rows="5"
					class="mb-3 w-full rounded border p-2"
				></textarea>
				<input
					bind:value={editingArticle.imageUrl}
					placeholder="URL изображения"
					class="mb-3 w-full rounded border p-2"
				/>
				<label class="mb-3 flex items-center">
					<input type="checkbox" bind:checked={editingArticle.published} class="mr-2" />
					Опубликовать
				</label>

				<!-- Кнопки сохранения и отмены -->
				<div class="space-x-2">
					<button
						on:click={updateNews}
						class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
					>
						Сохранить
					</button>
					<button
						on:click={cancelEditing}
						class="rounded bg-gray-500 px-4 py-2 text-white hover:bg-gray-600"
					>
						Отмена
					</button>
				</div>
			</div>
		{/if}

		<!-- Список всех новостей -->
		<div class="news-list">
			<h3 class="mb-4 text-xl font-semibold">Все новости</h3>
			{#if news.length === 0}
				<p>Новостей нет</p>
			{:else}
				{#each news as article}
					<div class="news-item mb-3 rounded border bg-white p-4">
						<div class="flex items-start justify-between">
							<!-- Информация о новости -->
							<div class="flex-1">
								<h4 class="mb-2 text-lg font-semibold">{article.title}</h4>
								<p class="mb-2 text-gray-600">{article.content.substring(0, 200)}...</p>

								<!-- Метаинформация -->
								<div class="flex items-center space-x-4 text-sm text-gray-500">
									<span>Создано: {new Date(article.createdAt).toLocaleDateString('ru-RU')}</span>
									<span
										class="rounded px-2 py-1 {article.published
											? 'bg-green-100 text-green-800'
											: 'bg-red-100 text-red-800'}"
									>
										{article.published ? 'Опубликовано' : 'Черновик'}
									</span>
								</div>
							</div>

							<!-- Кнопки действий -->
							<div class="actions ml-4 space-x-2">
								<button
									class="px-2 py-1 text-blue-500 hover:underline"
									on:click={() => startEditing(article)}
								>
									Редактировать
								</button>
								<button
									class="px-2 py-1 text-red-500 hover:underline"
									on:click={() => deleteNews(article.id)}
								>
									Удалить
								</button>
							</div>
						</div>
					</div>
				{/each}
			{/if}
		</div>

		<!-- Вкладка обратной связи -->
	{:else if activeTab === 'feedback'}
		<div class="feedback-list">
			<div class="mb-4 flex items-center justify-between">
				<h3 class="text-xl font-semibold">Обращения обратной связи</h3>
				<button
					on:click={loadFeedback}
					class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600"
					disabled={loadingFeedback}
				>
					{#if loadingFeedback}
						Загрузка...
					{:else}
						Обновить
					{/if}
				</button>
			</div>

			{#if loadingFeedback}
				<div class="py-8 text-center">
					<div
						class="inline-block h-8 w-8 animate-spin rounded-full border-b-2 border-blue-500"
					></div>
					<p class="mt-2 text-gray-600">Загрузка обращений...</p>
				</div>
			{:else if feedbackList.length === 0}
				<div class="py-8 text-center text-gray-600">
					<p>Обращений пока нет</p>
				</div>
			{:else}
				{#each feedbackList as feedback}
					<div class="feedback-item mb-4 rounded border bg-white p-4 shadow-sm">
						<div class="flex items-start justify-between">
							<div class="flex-1">
								<div class="mb-2 flex items-center space-x-4">
									<h4 class="text-lg font-semibold text-gray-800">{feedback.name}</h4>
									<span class="text-sm text-gray-500">{feedback.phone}</span>
									<span class="rounded bg-blue-100 px-2 py-1 text-sm text-blue-800">
										{feedback.subject}
									</span>
								</div>

								<p class="mb-3 whitespace-pre-wrap text-gray-700">{feedback.message}</p>

								<div class="text-sm text-gray-500">
									Получено: {formatDate(feedback.createdAt)}
								</div>
							</div>

							<!-- Кнопка удаления -->
							<div class="ml-4">
								<button
									class="rounded px-3 py-1 text-red-500 transition-colors hover:bg-red-50 hover:text-red-700"
									on:click={() => deleteFeedback(feedback.id)}
									title="Удалить обращение"
								>
									🗑️ Удалить
								</button>
							</div>
						</div>
					</div>
				{/each}
			{/if}
		</div>
	{/if}
</div>

<style>
	.container {
		max-width: 1000px;
	}

	.feedback-item {
		border-left: 4px solid #3b82f6;
	}

	.news-item {
		border-left: 4px solid #10b981;
	}
</style>
