<script>
// @ts-nocheck
    import '../app.css';  // Глобальные стили (если есть)
    import Header from '$lib/components/Header.svelte';
</script>

<!-- Шапка показывается на всех страницах -->
<Header />

<!-- Содержимое конкретной страницы -->
<main>
    <slot />  <!-- Сюда подставляется содержимое каждой страницы -->
</main>

<style>
    /* Глобальные стили для всего сайта */
    :global(body) {
        font-family: 'Inter', sans-serif;
        background-color: #f8fafc;
        margin: 0;
        padding: 0;
    }
</style>