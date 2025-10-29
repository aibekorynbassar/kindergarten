<script>
  // @ts-nocheck
  import { onMount } from 'svelte';
  import * as newsAPI from '$lib/api/newsAPI.js';

  // Получаем server-side данные из +page.ts (если есть)
  export let data = {};
  let news = data.news || [];

  let showCreateForm = false;
  let editingArticle = null;
  let newArticle = { title: '', content: '', imageUrl: '', published: false };
  let loading = false;
  let error = null;

  async function loadNews() {
    try {
      loading = true;
      // если есть server-side данные — не делаем fetch повторно
      if (news && news.length > 0) {
        return;
      }
      news = await newsAPI.getAdminNews(fetch);
    } catch (e) {
      console.error('Error loading news:', e);
      error = 'Не удалось загрузить новости';
      news = [];
    } finally {
      loading = false;
    }
  }

  async function createNews() {
    try {
      await newsAPI.createNews(newArticle);
      newArticle = { title: '', content: '', imageUrl: '', published: false };
      showCreateForm = false;
      await loadNews();
    } catch (e) {
      console.error('Error creating news:', e);
    }
  }

  async function updateNews() {
    try {
      await newsAPI.updateNews(editingArticle.id, editingArticle);
      editingArticle = null;
      await loadNews();
    } catch (e) {
      console.error('Error updating news:', e);
    }
  }

  async function deleteNews(id) {
    if (!confirm('Вы уверены, что хотите удалить эту новость?')) return;
    try {
      await newsAPI.deleteNews(id);
      await loadNews();
    } catch (e) {
      console.error('Error deleting news:', e);
    }
  }

  function startEditing(article) {
    editingArticle = { ...article };
  }
  function cancelEditing() {
    editingArticle = null;
  }

  function formatDate(dateString) {
    return new Date(dateString).toLocaleString('ru-RU');
  }

  onMount(loadNews);
</script>

<div>
  <div class="mb-4 flex items-center justify-between">
    <div>
      <button class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600" on:click={() => (showCreateForm = !showCreateForm)}>
        {showCreateForm ? 'Отмена' : 'Добавить новость'}
      </button>
    </div>
    <div class="text-sm text-gray-600">{news.length} записей</div>
  </div>

  {#if showCreateForm}
    <div class="create-form mb-6 rounded bg-gray-100 p-4">
      <h3 class="mb-4 text-xl font-semibold">Создать новость</h3>
      <input bind:value={newArticle.title} placeholder="Заголовок" class="mb-3 w-full rounded border p-2" />
      <textarea bind:value={newArticle.content} placeholder="Содержание" rows="5" class="mb-3 w-full rounded border p-2"></textarea>
      <input bind:value={newArticle.imageUrl} placeholder="URL изображения" class="mb-3 w-full rounded border p-2" />
      <label class="mb-3 flex items-center">
        <input type="checkbox" bind:checked={newArticle.published} class="mr-2" />Опубликовать
      </label>
      <button on:click={createNews} class="rounded bg-green-500 px-4 py-2 text-white hover:bg-green-600">Создать</button>
    </div>
  {/if}

  {#if editingArticle}
    <div class="edit-form mb-6 rounded bg-yellow-100 p-4">
      <h3 class="mb-4 text-xl font-semibold">Редактировать новость</h3>
      <input bind:value={editingArticle.title} placeholder="Заголовок" class="mb-3 w-full rounded border p-2" />
      <textarea bind:value={editingArticle.content} placeholder="Содержание" rows="5" class="mb-3 w-full rounded border p-2"></textarea>
      <input bind:value={editingArticle.imageUrl} placeholder="URL изображения" class="mb-3 w-full rounded border p-2" />
      <label class="mb-3 flex items-center">
        <input type="checkbox" bind:checked={editingArticle.published} class="mr-2" />Опубликовать
      </label>
      <div class="space-x-2">
        <button on:click={updateNews} class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600">Сохранить</button>
        <button on:click={cancelEditing} class="rounded bg-gray-500 px-4 py-2 text-white hover:bg-gray-600">Отмена</button>
      </div>
    </div>
  {/if}

  <div class="news-list">
    <h3 class="mb-4 text-xl font-semibold">Все новости</h3>

    {#if loading}
      <p class="text-gray-600">Загрузка...</p>
    {:else if news.length === 0}
      <p>Новостей нет</p>
    {:else}
      {#each news as article}
        <div class="news-item mb-3 rounded border bg-white p-4">
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <h4 class="mb-2 text-lg font-semibold">{article.title}</h4>
              <p class="mb-2 text-gray-600">{article.content ? article.content.substring(0,200) + '...' : ''}</p>
              <div class="flex items-center space-x-4 text-sm text-gray-500">
                <span>Создано: {formatDate(article.createdAt)}</span>
                <span class="rounded px-2 py-1 {article.published ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">{article.published ? 'Опубликовано' : 'Черновик'}</span>
              </div>
            </div>

            <div class="actions ml-4 space-x-2">
              <button class="px-2 py-1 text-blue-500 hover:underline" on:click={() => startEditing(article)}>Редактировать</button>
              <button class="px-2 py-1 text-red-500 hover:underline" on:click={() => deleteNews(article.id)}>Удалить</button>
            </div>
          </div>
        </div>
      {/each}
    {/if}
  </div>
</div>
