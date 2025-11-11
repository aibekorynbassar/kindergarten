<script>
  // @ts-nocheck
  import { submitFeedback } from '$lib/api/feedbackAPI.js';
  import { lang, t } from '$lib/lang/index.js';

  let formData = {
    name: '',
    phone: '',
    subject: '',
    message: ''
  };

  let isSubmitting = false;
  let showSuccess = false;
  let errorMessage = '';

  // локальные текстовые переменные (чтобы шаблон обновлялся реактивно)
  let titleText = '';
  let note = '';
  let nameLabel = '';
  let phoneLabel = '';
  let subjectLabel = '';
  let messageLabel = '';
  let namePlaceholder = '';
  let phonePlaceholder = '';
  let subjectPlaceholder = '';
  let messagePlaceholder = '';
  let sendLabel = '';
  let sendingLabel = '';
  let successText = '';
  let defaultSelectOption = '';
  let options = [];

  // Подтверждающие/ошибочные тексты для валидации и отправки
  let errNameRequired = '';
  let errPhoneRequired = '';
  let errSubjectRequired = '';
  let errMessageRequired = '';
  let errSendFailed = '';

  // Фильтрация: в имени только буквы и пробел
  function onNameInput(event) {
    formData.name = event.target.value.replace(/[^A-Za-zА-Яа-яЁё\s]/g, '');
  }

  // Фильтрация: в телефоне только цифры, +, (), пробел и дефис
  function onPhoneInput(event) {
    formData.phone = event.target.value.replace(/[^0-9()+\s-]/g, '');
  }

  // Реактивно обновляем все тексты при смене языка
  $: {
    $lang; // явная зависимость чтобы Svelte пересчитывал блок при смене языка

    note = t('feedback.note') || '* Поля, обязательные для заполнения';
    titleText = t('feedback.title') || 'Свяжитесь с нами';
    nameLabel = t('feedback.name') || 'Ваше имя *';
    phoneLabel = t('feedback.phone') || 'Номер телефона *';
    subjectLabel = t('feedback.subject') || 'Тема сообщения *';
    messageLabel = t('feedback.message') || 'Ваше сообщение *';

    namePlaceholder = t('feedback.placeholder.name') || 'Введите ваше имя';
    phonePlaceholder = t('feedback.placeholder.phone') || '+7 (___) ___-__-__';
    subjectPlaceholder = t('feedback.placeholder.subject') || 'Выберите тему';
    messagePlaceholder = t('feedback.placeholder.message') || 'Подробно опишите ваш вопрос или предложение...';

    sendLabel = t('feedback.send') || 'Отправить сообщение';
    sendingLabel = t('feedback.sending') || 'Отправляем...';
    successText = t('feedback.success') || '✅ Ваше сообщение успешно отправлено! Мы свяжемся с вами в ближайшее время.';

    defaultSelectOption = t('feedback.select_default') || 'Выберите тему';

    // Опции тем (массив объектов { value, label })
    options = [
      { value: 'Запись в детский сад', label: t('feedback.options.enroll') || 'Запись в детский сад' },
      { value: 'Вопрос о программе обучения', label: t('feedback.options.program') || 'Вопрос о программе обучения' },
      { value: 'Расписание и режим работы', label: t('feedback.options.schedule') || 'Расписание и режим работы' },
      { value: 'Жалоба', label: t('feedback.options.complaint') || 'Жалоба' },
      { value: 'Другое', label: t('feedback.options.other') || 'Другое' }
    ];

    // Ошибочные / служебные тексты
    errNameRequired = t('feedback.errors.name_required') || 'Пожалуйста, укажите ваше имя';
    errPhoneRequired = t('feedback.errors.phone_required') || 'Пожалуйста, укажите ваш номер телефона';
    errSubjectRequired = t('feedback.errors.subject_required') || 'Пожалуйста, укажите тему сообщения';
    errMessageRequired = t('feedback.errors.message_required') || 'Пожалуйста, напишите ваше сообщение';
    errSendFailed = t('feedback.errors.send_failed') || 'Ошибка при отправке формы. Пожалуйста, попробуйте позже.';
  }

  async function handleSubmit() {
    // Сбрасываем ошибку
    errorMessage = '';

    if (!formData.name.trim()) {
      errorMessage = errNameRequired;
      return;
    }
    if (!formData.phone.trim()) {
      errorMessage = errPhoneRequired;
      return;
    }
    if (!formData.subject.trim()) {
      errorMessage = errSubjectRequired;
      return;
    }
    if (!formData.message.trim()) {
      errorMessage = errMessageRequired;
      return;
    }

    isSubmitting = true;
    try {
      await submitFeedback(formData);

      showSuccess = true;

      formData = {
        name: '',
        phone: '',
        subject: '',
        message: ''
      };

      setTimeout(() => {
        showSuccess = false;
      }, 5000);
    } catch (error) {
      console.error('Error submitting feedback:', error);
      errorMessage = errSendFailed;
    } finally {
      isSubmitting = false;
    }
  }
</script>

<div class="feedback-form rounded-lg bg-white p-6 shadow-md">
  <h3 class="mb-4 text-2xl font-bold text-gray-800">{titleText}</h3>

  <!-- Сообщение об успешной отправке -->
  {#if showSuccess}
    <div class="mb-4 rounded-lg border border-green-300 bg-green-100 p-4 text-green-700">
      {@html successText}
    </div>
  {/if}

  <!-- Сообщение об ошибке -->
  {#if errorMessage}
    <div class="mb-4 rounded-lg border border-red-300 bg-red-100 p-4 text-red-700">
      ❌ {errorMessage}
    </div>
  {/if}

  <form on:submit|preventDefault={handleSubmit}>
    <!-- Имя -->
    <div class="mb-4">
      <label for="name" class="mb-2 block text-sm font-medium text-gray-700">{nameLabel}</label>
      <input
        id="name"
        type="text"
        bind:value={formData.name}
        on:input={onNameInput}
        disabled={isSubmitting}
        placeholder={namePlaceholder}
        class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
        required
      />
    </div>

    <!-- Номер телефона -->
    <div class="mb-4">
      <label for="phone" class="mb-2 block text-sm font-medium text-gray-700">{phoneLabel}</label>
      <input
        id="phone"
        type="tel"
        bind:value={formData.phone}
        on:input={onPhoneInput}
        disabled={isSubmitting}
        placeholder={phonePlaceholder}
        class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
        required
      />
    </div>

    <!-- Тема сообщения -->
    <div class="mb-4">
      <label for="subject" class="mb-2 block text-sm font-medium text-gray-700">{subjectLabel}</label>
      <select
        id="subject"
        bind:value={formData.subject}
        disabled={isSubmitting}
        class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
        required
      >
        <option value="">{defaultSelectOption}</option>
        {#each options as opt}
          <option value={opt.value}>{opt.label}</option>
        {/each}
      </select>
    </div>

    <!-- Комментарии -->
    <div class="mb-6">
      <label for="message" class="mb-2 block text-sm font-medium text-gray-700">{messageLabel}</label>
      <textarea
        id="message"
        bind:value={formData.message}
        disabled={isSubmitting}
        placeholder={messagePlaceholder}
        rows="5"
        class="resize-vertical w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
        required
      ></textarea>
    </div>

    <!-- Кнопка отправки -->
    <button
      type="submit"
      disabled={isSubmitting}
      class="w-full rounded-lg bg-blue-500 px-6 py-3 font-semibold text-white
             transition-colors hover:bg-blue-600 focus:ring-2 focus:ring-blue-500
             focus:ring-offset-2 disabled:cursor-not-allowed disabled:bg-gray-400"
    >
      {#if isSubmitting}
        <span class="flex items-center justify-center">
          <div class="mr-2 h-4 w-4 animate-spin rounded-full border-b-2 border-white"></div>
          {sendingLabel}
        </span>
      {:else}
        {sendLabel}
      {/if}
    </button>
  </form>

  <p class="mt-4 text-sm text-gray-500">{note}</p>
</div>

<style>
  .feedback-form {
    max-width: 600px;
  }
</style>
