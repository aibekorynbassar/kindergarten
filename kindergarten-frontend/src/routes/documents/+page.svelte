<script lang="ts">
  import { t } from '$lib/lang'; // путь подставь, как у тебя
</script>

<section class="container mx-auto py-6">
  <h1 class="text-2xl font-semibold mb-4">{t('header.documents') || 'Документы'}</h1>

  <p class="mb-4">Здесь будут размещаться важные документы детсада — положения, регламенты, формы.</p>

  <ul class="space-y-2">
    <!-- Пример: список документов -->
    <li class="border rounded p-3">
      <a href="/files/polozhenie.pdf" class="underline">Положение о внутреннем распорядке (PDF)</a>
    </li>
  </ul>
</section>
