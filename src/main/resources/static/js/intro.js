/**
 * intro.js — Om opening splash experience.
 *
 * Responsibilities:
 *  - Show a full-screen Om splash once per browser tab/session (sessionStorage).
 *  - Generate a small number of lightweight golden floating particles.
 *  - Handle "Enter Celebration" and "Skip intro" — both perform the same clean exit:
 *      fade + slight scale up, restore scrolling, remove from AT/keyboard nav,
 *      reveal the hero underneath, WITHOUT reloading the page.
 *  - Respect prefers-reduced-motion (no particle animation, near-instant reveal).
 *  - Dev switch to disable the "show once per session" behaviour while testing.
 *
 * No page reload is ever triggered.
 */
(function () {
  'use strict';

  // ---- Development switch -------------------------------------------------
  // Set to true while testing to force the intro to show on EVERY load.
  var DEV_ALWAYS_SHOW_INTRO = false;

  var SESSION_KEY = 'omIntroSeen';

  var reduceMotion = window.matchMedia &&
    window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  document.addEventListener('DOMContentLoaded', function () {
    var splash = document.getElementById('omSplash');
    if (!splash) {
      // No splash element in DOM — nothing to do; ensure scrolling is enabled.
      document.body.classList.remove('intro-lock-scroll');
      return;
    }

    var alreadySeen = !DEV_ALWAYS_SHOW_INTRO &&
      sessionStorage.getItem(SESSION_KEY) === 'true';

    if (alreadySeen) {
      // Skip straight to the hero on refresh within the same session.
      removeSplashImmediately(splash);
      return;
    }

    // Show the splash: lock scrolling while it is visible.
    document.body.classList.add('intro-lock-scroll');

    if (!reduceMotion) {
      spawnParticles(splash);
    }

    var enterBtn = document.getElementById('enterCelebrationBtn');
    var skipBtn = document.getElementById('skipIntroBtn');

    if (enterBtn) enterBtn.addEventListener('click', function () { closeSplash(splash); });
    if (skipBtn) skipBtn.addEventListener('click', function () { closeSplash(splash); });

    // Allow Escape to skip the intro for keyboard users.
    document.addEventListener('keydown', function onEsc(e) {
      if (e.key === 'Escape' && !splash.classList.contains('om-splash-hidden')) {
        closeSplash(splash);
        document.removeEventListener('keydown', onEsc);
      }
    });

    // Move focus to the primary action for keyboard users.
    if (enterBtn && typeof enterBtn.focus === 'function') {
      window.setTimeout(function () { enterBtn.focus(); }, 100);
    }
  });

  /**
   * Creates a limited number of soft golden particles that drift slowly upward.
   * Fewer particles on small screens for performance. Particles are removed
   * from the DOM when the splash closes (see closeSplash).
   */
  function spawnParticles(splash) {
    var layer = splash.querySelector('.om-particles');
    if (!layer) return;

    var isMobile = window.matchMedia('(max-width: 768px)').matches;
    var count = isMobile ? 8 : 16;

    for (var i = 0; i < count; i++) {
      var p = document.createElement('span');
      p.className = 'om-particle';
      var size = 3 + Math.random() * 4;
      p.style.width = size + 'px';
      p.style.height = size + 'px';
      p.style.left = Math.random() * 100 + '%';
      p.style.setProperty('--particle-drift', (Math.random() * 60 - 30) + 'px');
      var duration = 7 + Math.random() * 6;
      p.style.animationDuration = duration + 's';
      p.style.animationDelay = (Math.random() * duration) + 's';
      layer.appendChild(p);
    }
  }

  /**
   * Cleanly closes the splash: fade + slight scale up, then hide entirely.
   * Restores scrolling, records the session flag, and removes it from AT/keyboard nav.
   */
  function closeSplash(splash) {
    if (splash.classList.contains('om-splash-exit')) return; // already closing

    try { sessionStorage.setItem(SESSION_KEY, 'true'); } catch (e) { /* ignore */ }

    splash.classList.add('om-splash-exit');
    splash.setAttribute('aria-hidden', 'true');

    var finalize = function () {
      splash.classList.add('om-splash-hidden');
      document.body.classList.remove('intro-lock-scroll');
      // Remove particles to avoid running animations / memory leaks.
      var layer = splash.querySelector('.om-particles');
      if (layer) layer.innerHTML = '';
      // Notify the rest of the app that the celebration has been entered.
      document.dispatchEvent(new CustomEvent('celebration:entered'));
    };

    if (reduceMotion) {
      finalize();
    } else {
      var done = false;
      var onEnd = function () {
        if (done) return;
        done = true;
        splash.removeEventListener('transitionend', onEnd);
        finalize();
      };
      splash.addEventListener('transitionend', onEnd);
      // Fallback in case transitionend doesn't fire.
      window.setTimeout(onEnd, 1300);
    }
  }

  /** Removes the splash with no animation (used when already seen this session). */
  function removeSplashImmediately(splash) {
    splash.classList.add('om-splash-hidden');
    splash.setAttribute('aria-hidden', 'true');
    document.body.classList.remove('intro-lock-scroll');
    var layer = splash.querySelector('.om-particles');
    if (layer) layer.innerHTML = '';
    // Still signal entered so music button etc. can appear.
    document.dispatchEvent(new CustomEvent('celebration:entered'));
  }
})();

