<script>
  // @ts-nocheck
  import { onMount } from 'svelte';
  import * as feedbackAPI from '$lib/api/feedbackAPI.js';

  let feedbackList = [];
  let loadingFeedback = false;
  let error = null;

  async function loadFeedback() {
    try {
      loadingFeedback = true;
      feedbackList = await feedbackAPI.getAllFeedback();
    } catch (e) {
      console.error('Error loading feedback:', e);
      error = 'Не удалось загрузить обращения';
      feedbackList = [];
    } finally {
      loadingFeedback = false;
    }
  }

  async function deleteFeedback(id) {
    if (!confirm('Вы уверены, что хотите удалить это обращение?')) return;
    try {
      await feedbackAPI.deleteFeedback(id);
      await loadFeedback();
    } catch (e) {
      console.error('Error deleting feedback:', e);
    }
  }

  function formatDate(dateString) {
    return new Date(dateString).toLocaleString('ru-RU');
  }

  onMount(loadFeedback);
</script>

<div>
  <div class="mb-4 flex items-center justify-between">
    <h3 class="text-xl font-semibold">Обращения обратной связи</h3>
    <button on:click={loadFeedback} class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600" disabled={loadingFeedback}>
      {#if loadingFeedback}Загрузка...{:else}Обновить{/if}
    </button>
  </div>

  {#if loadingFeedback}
    <div class="py-8 text-center">
      <div class="inline-block h-8 w-8 animate-spin rounded-full border-b-2 border-blue-500"></div>
      <p class="mt-2 text-gray-600">Загрузка обращений...</p>
    </div>
  {:else if feedbackList.length === 0}
    <div class="py-8 text-center text-gray-600">
      <p>Обращений пока нет</p>
    </div>
  {:else}
    {#each feedbackList as fb}
      <div class="feedback-item mb-4 rounded border bg-white p-4 shadow-sm">
        <div class="flex items-start justify-between">
          <div class="flex-1">
            <div class="mb-2 flex items-center space-x-4">
              <h4 class="text-lg font-semibold text-gray-800">{fb.name}</h4>
              <span class="text-sm text-gray-500">{fb.phone}</span>
              <span class="rounded bg-blue-100 px-2 py-1 text-sm text-blue-800">{fb.subject}</span>
            </div>

            <p class="mb-3 whitespace-pre-wrap text-gray-700">{fb.message}</p>
            <div class="text-sm text-gray-500">Получено: {formatDate(fb.createdAt)}</div>
          </div>

          <div class="ml-4">
            <button class="rounded px-3 py-1 text-red-500 hover:bg-red-50" on:click={() => deleteFeedback(fb.id)}>🗑️ Удалить</button>
          </div>
        </div>
      </div>
    {/each}
  {/if}
</div>
