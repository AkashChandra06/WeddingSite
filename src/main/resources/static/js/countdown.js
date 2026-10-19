/**
 * countdown.js — premium glass countdown to the wedding date.
 *
 * Reads the ISO date/time from #countdownTimer's data-wedding-date attribute
 * (populated server-side from application.yaml's wedding.date). This is the single
 * source of truth — the date is never hardcoded here.
 *
 * Features / edge cases handled:
 *   - Single setInterval (no duplicate timers).
 *   - Subtle cross-fade only on the digits that actually change (no full-card flip).
 *   - Fixed-width digit containers to avoid layout shift.
 *   - Invalid date  -> shows a graceful message.
 *   - Already completed -> "The celebration has begun."
 *   - JS disabled -> static "00" placeholders remain visible (in the HTML).
 */
(function () {
  'use strict';

  var reduceMotion = window.matchMedia &&
    window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  document.addEventListener('DOMContentLoaded', function () {
    var root = document.getElementById('countdownTimer');
    if (!root) return;

    var weddingDateStr = root.getAttribute('data-wedding-date');
    var completeMsg = document.getElementById('countdownComplete');

    if (!weddingDateStr) {
      showComplete(root, completeMsg, 'Wedding date coming soon.');
      return;
    }

    var weddingDate = new Date(weddingDateStr).getTime();
    if (isNaN(weddingDate)) {
      showComplete(root, completeMsg, 'Wedding date coming soon.');
      return;
    }

    var units = {
      days: document.getElementById('cd-days'),
      hours: document.getElementById('cd-hours'),
      minutes: document.getElementById('cd-minutes'),
      seconds: document.getElementById('cd-seconds')
    };

    var last = { days: null, hours: null, minutes: null, seconds: null };
    var timer = null;

    function render() {
      var distance = weddingDate - Date.now();

      if (distance <= 0) {
        setDigit(units.days, '00');
        setDigit(units.hours, '00');
        setDigit(units.minutes, '00');
        setDigit(units.seconds, '00');
        if (timer) { clearInterval(timer); timer = null; }
        showComplete(root, completeMsg, 'The celebration has begun.');
        return;
      }

      var days = Math.floor(distance / 86400000);
      var hours = Math.floor((distance % 86400000) / 3600000);
      var minutes = Math.floor((distance % 3600000) / 60000);
      var seconds = Math.floor((distance % 60000) / 1000);

      updateUnit('days', pad(days));
      updateUnit('hours', pad(hours));
      updateUnit('minutes', pad(minutes));
      updateUnit('seconds', pad(seconds));
    }

    function updateUnit(key, value) {
      if (last[key] === value) return; // only touch DOM when it changes
      last[key] = value;
      setDigit(units[key], value);
    }

    function setDigit(el, value) {
      if (!el) return;
      el.textContent = value;
      if (!reduceMotion) {
        el.classList.remove('countdown-digit-swap');
        void el.offsetWidth; // force reflow so the animation retriggers
        el.classList.add('countdown-digit-swap');
      }
    }

    render();
    timer = setInterval(render, 1000);

    // Pause the ticking when the tab is hidden; resume + resync when visible.
    document.addEventListener('visibilitychange', function () {
      if (document.hidden) {
        if (timer) { clearInterval(timer); timer = null; }
      } else if (!timer) {
        render();
        timer = setInterval(render, 1000);
      }
    });
  });

  function pad(n) { return String(n).padStart(2, '0'); }

  function showComplete(root, msgEl, message) {
    if (root) root.style.display = 'none';
    if (msgEl) {
      msgEl.textContent = message;
      msgEl.hidden = false;
    }
  }
})();


