import {writable} from 'svelte/store';

const initial = (typeof localStorage !== 'undefined' && localStorage.getItem('lang')) || 'ru';

export const lang = writable(initial);

if(typeof localStorage !== 'undefined') {
        lang.subscribe((value) => localStorage.setItem('lang', value));
}