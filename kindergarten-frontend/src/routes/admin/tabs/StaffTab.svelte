<script>
  // @ts-nocheck
  import { onMount } from 'svelte';
  import * as staffAPI from '$lib/api/staffAPI.js';

  let staff = [];
  let staffCreateForm = false;
  let editingStaff = null;
  let newStaff = { name: '', profession: '', imageUrl: '' };
  let loading = false;
  let error = null;

  async function loadStaff() {
    try {
      loading = true;
      staff = await staffAPI.getAllStaff();
    } catch (e) {
      console.error('Error loading staff:', e);
      error = 'Не удалось загрузить преподавателей';
      staff = [];
    } finally {
      loading = false;
    }
  }

  async function createStaff() {
    try {
      await staffAPI.createStaff(newStaff);
      newStaff = { name: '', profession: '', imageUrl: '' };
      staffCreateForm = false;
      await loadStaff();
    } catch (e) {
      console.error('Error creating staff:', e);
    }
  }

  function staffEditing(teacher) {
    editingStaff = { ...teacher };
  }

  async function updateStaff() {
    try {
      await staffAPI.updateStaff(editingStaff.id, editingStaff);
      editingStaff = null;
      await loadStaff();
    } catch (e) {
      console.error('Error updating staff:', e);
    }
  }

  async function deleteStaff(id) {
    if (!confirm('Удалить этого преподавателя?')) return;
    try {
      await staffAPI.deleteStaff(id);
      await loadStaff();
    } catch (e) {
      console.error('Error deleting staff:', e);
    }
  }

  onMount(loadStaff);
</script>

<div>
  <h3 class="mb-4 text-xl font-semibold">Управление преподавателями</h3>

  <div class="mb-4">
    <button class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600" on:click={() => (staffCreateForm = !staffCreateForm)}>
      {staffCreateForm ? 'Отмена' : 'Добавить преподавателя'}
    </button>
  </div>

  {#if staffCreateForm}
    <div class="create-form mb-6 rounded bg-gray-100 p-4">
      <h3 class="mb-4 text-lg font-semibold">Создать преподавателя</h3>
      <input bind:value={newStaff.name} placeholder="ФИО" class="mb-3 w-full rounded border p-2" />
      <input bind:value={newStaff.profession} placeholder="Должность" class="mb-3 w-full rounded border p-2" />
      <input bind:value={newStaff.imageUrl} placeholder="URL фотографии" class="mb-3 w-full rounded border p-2" />
      <button on:click={createStaff} class="rounded bg-green-500 px-4 py-2 text-white hover:bg-green-600">Создать</button>
    </div>
  {/if}

  {#if editingStaff}
    <div class="edit-form mb-6 rounded bg-yellow-100 p-4">
      <h3 class="mb-4 text-lg font-semibold">Редактировать преподавателя</h3>
      <input bind:value={editingStaff.name} placeholder="ФИО" class="mb-3 w-full rounded border p-2" />
      <input bind:value={editingStaff.profession} placeholder="Должность" class="mb-3 w-full rounded border p-2" />
      <input bind:value={editingStaff.imageUrl} placeholder="URL фотографии" class="mb-3 w-full rounded border p-2" />
      <div class="space-x-2">
        <button on:click={updateStaff} class="rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600">Сохранить</button>
        <button on:click={() => (editingStaff = null)} class="rounded bg-gray-500 px-4 py-2 text-white hover:bg-gray-600">Отмена</button>
      </div>
    </div>
  {/if}

  <div class="staff-list">
    <h3 class="mb-4 text-lg font-semibold">Все преподаватели</h3>

    {#if loading}
      <p>Загрузка...</p>
    {:else if staff.length === 0}
      <p>Преподавателей пока нет</p>
    {:else}
      {#each staff as teacher}
        <div class="staff-item mb-3 flex items-center justify-between rounded border bg-white p-4">
          <div class="flex items-center space-x-4">
            <img
              src={teacher.imageUrl || '/images/defaultStaff.jpg'}
              alt={teacher.name}
              class="h-16 w-16 rounded-full border object-cover"
              on:error={(e) => {
                if (e.target.src.includes('defaultStaff.jpg')) return;
                e.target.src = '/images/defaultStaff.jpg';
              }}
            />
            <div>
              <p class="font-semibold text-gray-800">{teacher.name}</p>
              <p class="text-sm text-gray-500">{teacher.profession}</p>
            </div>
          </div>

          <div class="flex space-x-3">
            <button class="text-blue-500 hover:underline" on:click={() => staffEditing(teacher)}>Изменить</button>
            <button class="text-red-500 hover:underline" on:click={() => deleteStaff(teacher.id)}>Удалить</button>
          </div>
        </div>
      {/each}
    {/if}
  </div>
</div>
