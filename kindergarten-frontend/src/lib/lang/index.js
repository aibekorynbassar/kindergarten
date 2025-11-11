// src/lib/lang/index.js
// @ts-nocheck
import { get, derived } from 'svelte/store';
import { lang } from './langStore.js';

// Импортируем все json-файлы (статически) — Vite включит их в бандл.
// ПРИМЕЧАНИЕ: поправь пути если структура отличается.
import ru_header from './ru/header.json';
import ru_feedback from './ru/feedback.json';
import ru_home from './ru/home.json';

import kk_header from './kk/header.json';
import kk_feedback from './kk/feedback.json';
import kk_home from './kk/home.json';

// Helper: глубокое слияние (простая реализация, для наших JSON отлично)
function deepMerge(target, source) {
  for (const k of Object.keys(source)) {
    if (
      source[k] &&
      typeof source[k] === 'object' &&
      !Array.isArray(source[k])
    ) {
      target[k] = deepMerge(target[k] || {}, source[k]);
    } else {
      target[k] = source[k];
    }
  }
  return target;
}

// Собираем словари по языкам
const ru = deepMerge({}, {});
deepMerge(ru, ru_header);
deepMerge(ru, ru_feedback);
deepMerge(ru, ru_home);

const kk = deepMerge({}, {});
deepMerge(kk, kk_header);
deepMerge(kk, kk_feedback);
deepMerge(kk, kk_home);

// Собираем в объект dict
const dict = { ru, kk };

// экспортируем derived-store, содержащий объект переводов для текущего языка
export const translations = derived(lang, ($lang) => dict[$lang] || dict.ru);

// Простая функция t(key) — точечный ключ 'feedback.title'

export function t(key) {
  const l = get(lang) || 'ru';
  const part = key.split('.');
  let cur = dict[l];
  for (const p of part) {
    if (!cur || typeof cur !== 'object') {
      // если путь сломан — возвращаем undefined, чтобы компонент мог подставить запасной вариант
      return undefined;
    }
    cur = cur[p];
  }
  // Если нашли значение — возвращаем его (может быть string, array, object и т.д.)
  return cur;
}

export function setLang(l) {
  if (!['ru', 'kk'].includes(l)) return;
  lang.set(l);
}

// экспортируем store lang, чтобы можно писать import { lang } ...
export { lang };
