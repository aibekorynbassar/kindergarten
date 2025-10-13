<script>
	// @ts-nocheck
	import { submitFeedback } from '$lib/api/feedbackAPI.js';

	let formData = {
		name: '',
		phone: '',
		subject: '',
		message: ''
	};

	let isSubmitting = false;
	let showSuccess = false;
	let errorMessage = '';
	// Фильтрация: в имени только буквы и пробел

	function onNameInput(event) {
		// заменяем всё, что не буквы или пробелы, на пустую строку
		formData.name = event.target.value.replace(/[^A-Za-zА-Яа-яЁё\s]/g, '');
	}

	// Фильтрация: в телефоне только цифры, +, (), пробел и дефис
	function onPhoneInput(event) {
		formData.phone = event.target.value.replace(/[^0-9()+\s-]/g, '');
	}

	async function handleSubmit() {
		if (!formData.name.trim()) {
			errorMessage = 'Пожалуйста, укажите ваше имя';
			return;
		}
		if (!formData.phone.trim()) {
			errorMessage = 'Пожалуйста, укажите ваш номер телефона';
			return;
		}
		if (!formData.subject.trim()) {
			errorMessage = 'Пожалуйста, укажите тему сообщения';
			return;
		}
		if (!formData.message.trim()) {
			errorMessage = 'Пожалуйста, напишите ваше сообщение';
			return;
		}

		isSubmitting = true;
		errorMessage = '';

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
			errorMessage = 'Ошибка при отправке формы. Пожалуйста, попробуйте позже.';
		} finally {
			isSubmitting = false;
		}
	}
</script>

<div class="feedback-form rounded-lg bg-white p-6 shadow-md">
	<h3 class="mb-4 text-2xl font-bold text-gray-800">Свяжитесь с нами</h3>

	<!-- Сообщение об успешной отправке -->
	{#if showSuccess}
		<div class="mb-4 rounded-lg border border-green-300 bg-green-100 p-4 text-green-700">
			✅ Ваше сообщение успешно отправлено! Мы свяжемся с вами в ближайшее время.
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
			<label for="name" class="mb-2 block text-sm font-medium text-gray-700"> Ваше имя * </label>
			<input
				id="name"
				type="text"
				bind:value={formData.name}
                on:input={onNameInput}
				disabled={isSubmitting}
				placeholder="Введите ваше имя"
				class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
				required
			/>
		</div>

		<!-- Номер телефона -->
		<div class="mb-4">
			<label for="phone" class="mb-2 block text-sm font-medium text-gray-700">
				Номер телефона *
			</label>
			<input
				id="phone"
				type="tel"
				bind:value={formData.phone}
                on:input={onPhoneInput}
				disabled={isSubmitting}
				placeholder="+7 (___) ___-__-__"
				class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
				required
			/>
		</div>

		<!-- Тема сообщения -->
		<div class="mb-4">
			<label for="subject" class="mb-2 block text-sm font-medium text-gray-700">
				Тема сообщения *
			</label>
			<select
				id="subject"
				bind:value={formData.subject}
				disabled={isSubmitting}
				class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:border-blue-500 focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100"
				required
			>
				<option value="">Выберите тему</option>
				<option value="Запись в детский сад">Запись в детский сад</option>
				<option value="Вопрос о программе обучения">Вопрос о программе обучения</option>
				<option value="Расписание и режим работы">Расписание и режим работы</option>
				<option value="Жалоба">Жалоба</option>
				<option value="Другое">Другое</option>
			</select>
		</div>

		<!-- Комментарии -->
		<div class="mb-6">
			<label for="message" class="mb-2 block text-sm font-medium text-gray-700">
				Ваше сообщение *
			</label>
			<textarea
				id="message"
				bind:value={formData.message}
				disabled={isSubmitting}
				placeholder="Подробно опишите ваш вопрос или предложение..."
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
					Отправляем...
				</span>
			{:else}
				Отправить сообщение
			{/if}
		</button>
	</form>

	<p class="mt-4 text-sm text-gray-500">* Поля, обязательные для заполнения</p>
</div>

<style>
	.feedback-form {
		max-width: 600px;
	}
</style>
