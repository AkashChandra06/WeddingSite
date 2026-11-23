/**
 * animations.js — consolidated premium animation & interaction engine.
 *
 * Replaces the old main.js (fade-in/stars/smooth-scroll/AOS) and petals.js
 * (global emoji petals). Everything here is native (no AOS/GSAP), transform/opacity
 * based, respects prefers-reduced-motion, and pauses when the tab is hidden.
 *
 * Modules:
 *   1. Scroll-reveal system (single IntersectionObserver)
 *   2. Smooth scroll + mobile navbar collapse
 *   3. Hero star field (subtle)
 *   4. Hero flower petals (CSS shapes, hero-scoped, pausable)
 *   5. Wedding journey progress navigation (IntersectionObserver active-state)
 *   6. Luxury invitation reveal
 *   7. Memory Wall polaroid styling + first-reveal
 *   8. Optional music toggle
 */
(function () {
  'use strict';

  var reduceMotion = window.matchMedia &&
    window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  var isMobile = window.matchMedia('(max-width: 768px)').matches;

  document.addEventListener('DOMContentLoaded', function () {
    initScrollReveal();
    initSmoothScroll();
    initHeroStars();
    initPetalShower();
    initJourneyNav();
    initInvitationReveal();
    initMemoryWall();
    initMusicToggle();
  });

  /* ----------------------------------------------------------------------
   * 1. SCROLL-REVEAL SYSTEM
   * Reveals both legacy .fade-in-up elements and new [data-reveal] elements
   * using ONE IntersectionObserver. Each element animates only once.
   * -------------------------------------------------------------------- */
  function initScrollReveal() {
    var revealEls = document.querySelectorAll('.fade-in-up, [data-reveal]');
    if (revealEls.length === 0) return;

    // No-JS / no-IO fallback + reduced motion: show everything immediately.
    if (!('IntersectionObserver' in window) || reduceMotion) {
      revealEls.forEach(function (el) {
        el.classList.add('visible');
        el.classList.add('reveal-visible');
      });
      return;
    }

    var observer = new IntersectionObserver(function (entries) {
      entries.forEach(function (entry) {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');       // legacy
          entry.target.classList.add('reveal-visible'); // new
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15, rootMargin: '0px 0px -40px 0px' });

    revealEls.forEach(function (el) { observer.observe(el); });
  }

  /* ----------------------------------------------------------------------
   * 2. SMOOTH SCROLL + MOBILE NAVBAR COLLAPSE
   * -------------------------------------------------------------------- */
  function initSmoothScroll() {
    document.querySelectorAll('a.nav-link[href^="#"], a.smooth-scroll[href^="#"]').forEach(function (link) {
      link.addEventListener('click', function () {
        var navbarCollapse = document.getElementById('navbarContent');
        if (navbarCollapse && navbarCollapse.classList.contains('show') && window.bootstrap) {
          bootstrap.Collapse.getOrCreateInstance(navbarCollapse).hide();
        }
      });
    });
  }

  /* ----------------------------------------------------------------------
   * 3. HERO STAR FIELD (subtle, opacity 0.15–0.5, fewer on mobile)
   * -------------------------------------------------------------------- */
  function initHeroStars() {
    var container = document.getElementById('starsContainer');
    if (!container || reduceMotion) return;

    var count = isMobile ? 14 : 26;
    var frag = document.createDocumentFragment();
    for (var i = 0; i < count; i++) {
      var star = document.createElement('span');
      star.className = 'hero-star';
      var size = 1.2 + Math.random() * 2.2;
      star.style.width = size + 'px';
      star.style.height = size + 'px';
      star.style.top = Math.random() * 55 + '%';   // keep away from lower faces/ceremony
      star.style.left = Math.random() * 100 + '%';
      star.style.animationDuration = (2.5 + Math.random() * 3.5) + 's';
      star.style.animationDelay = (Math.random() * 3) + 's';
      frag.appendChild(star);
    }
    container.appendChild(frag);
  }

  /* ----------------------------------------------------------------------
   * 4. FLOWER PETALS — global full-viewport shower (CSS shapes)
   * Petals fall across the whole page (fixed overlay). Capped pool, recycled,
   * paused when the tab is hidden, and fully disabled for reduced motion.
   * -------------------------------------------------------------------- */
  function initPetalShower() {
    if (reduceMotion) return;
    if (document.getElementById('globalPetals')) return;

    var layer = document.createElement('div');
    layer.className = 'global-petals-layer';
    layer.id = 'globalPetals';
    layer.setAttribute('aria-hidden', 'true');
    document.body.appendChild(layer);

    var maxPetals = isMobile ? 7 : 12;
    var petalTimer = null;

    function makePetal() {
      if (layer.childElementCount >= maxPetals) return;

      var petal = document.createElement('span');
      var isRose = Math.random() > 0.5;
      petal.className = 'hero-petal ' + (isRose ? 'petal-rose' : 'petal-marigold');

      var size = 8 + Math.random() * 10;
      petal.style.width = size + 'px';
      petal.style.height = (size * 0.8) + 'px';
      petal.style.left = Math.random() * 100 + '%';
      petal.style.setProperty('--petal-drift-x', (Math.random() * 120 - 20) + 'px');
      petal.style.setProperty('--petal-rotate', (140 + Math.random() * 220) + 'deg');
      petal.style.opacity = String(0.3 + Math.random() * 0.35);
      var duration = 9 + Math.random() * 7;
      petal.style.animationDuration = duration + 's';

      // Remove after one fall to avoid unbounded DOM growth / memory leaks.
      petal.addEventListener('animationiteration', function () { petal.remove(); });
      window.setTimeout(function () { if (petal.isConnected) petal.remove(); }, duration * 1000 + 500);

      layer.appendChild(petal);
    }

    function start() {
      if (petalTimer) return;
      petalTimer = window.setInterval(makePetal, 1400);
    }
    function stop() {
      if (petalTimer) { window.clearInterval(petalTimer); petalTimer = null; }
    }

    // Pause generation when the tab is hidden (performance / battery).
    document.addEventListener('visibilitychange', function () {
      if (document.hidden) stop(); else start();
    });

    start();
  }

  /* ----------------------------------------------------------------------
   * 5. WEDDING JOURNEY PROGRESS NAVIGATION
   * Builds a subtle dot rail; active state driven by IntersectionObserver.
   * Does NOT replace the existing navbar.
   * -------------------------------------------------------------------- */
  function initJourneyNav() {
    var sections = [
      { id: 'hero', label: 'Home' },
      { id: 'invitation', label: 'Invitation' },
      { id: 'events', label: 'Events' },
      { id: 'venue', label: 'Venue' },
      { id: 'family', label: 'Family' },
      { id: 'gallery', label: 'Gallery' },
      { id: 'memory-wall', label: 'Memory Wall' }
    ].filter(function (s) { return document.getElementById(s.id); });

    if (sections.length === 0) return;

    var nav = document.createElement('nav');
    nav.className = 'journey-nav';
    nav.setAttribute('aria-label', 'Wedding journey navigation');

    var line = document.createElement('span');
    line.className = 'journey-nav-line';
    line.setAttribute('aria-hidden', 'true');
    nav.appendChild(line);

    var dotById = {};
    sections.forEach(function (s) {
      var dot = document.createElement('button');
      dot.type = 'button';
      dot.className = 'journey-dot';
      dot.setAttribute('aria-label', 'Go to ' + s.label);
      dot.dataset.target = s.id;

      var label = document.createElement('span');
      label.className = 'journey-label';
      label.textContent = s.label;
      label.setAttribute('aria-hidden', 'true');
      dot.appendChild(label);

      dot.addEventListener('click', function () {
        var target = document.getElementById(s.id);
        if (target) {
          target.scrollIntoView({ behavior: reduceMotion ? 'auto' : 'smooth', block: 'start' });
        }
      });

      nav.appendChild(dot);
      dotById[s.id] = dot;
    });

    document.body.appendChild(nav);

    if ('IntersectionObserver' in window) {
      var spy = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
          if (entry.isIntersecting) {
            Object.keys(dotById).forEach(function (id) {
              dotById[id].classList.toggle('active', id === entry.target.id);
            });
          }
        });
      }, { threshold: 0.5 });

      sections.forEach(function (s) { spy.observe(document.getElementById(s.id)); });
    }
  }

  /* ----------------------------------------------------------------------
   * 6. LUXURY INVITATION REVEAL
   * Progressive enhancement: seals the invitation only when JS is present.
   * -------------------------------------------------------------------- */
  function initInvitationReveal() {
    var wrapper = document.getElementById('invitationWrapper');
    var openBtn = document.getElementById('openInvitationBtn');
    if (!wrapper || !openBtn) return;

    // With JS available, seal the card behind its cover.
    wrapper.classList.add('js-sealed');

    openBtn.addEventListener('click', function () {
      wrapper.classList.remove('js-sealed');
      wrapper.classList.add('js-opened');
      // Move focus into the revealed content for accessibility.
      var content = wrapper.querySelector('.invitation-content');
      if (content) {
        content.setAttribute('tabindex', '-1');
        content.focus({ preventScroll: true });
      }
    });
  }

  /* ----------------------------------------------------------------------
   * 7. MEMORY WALL — polaroid styling + gentle first reveal
   * -------------------------------------------------------------------- */
  function initMemoryWall() {
    var cards = document.querySelectorAll('#memory-wall .memory-card');
    cards.forEach(function (card) {
      card.classList.add('polaroid');
      if (!card.hasAttribute('data-reveal')) {
        card.setAttribute('data-reveal', 'scale');
      }
    });
  }

  /* ----------------------------------------------------------------------
   * 8. OPTIONAL MUSIC TOGGLE
   * Only wires up if the button + audio element exist (server-gated).
   * Never autoplays. Remembers choice for the session. Handles audio errors.
   * -------------------------------------------------------------------- */
  function initMusicToggle() {
    var btn = document.getElementById('musicToggle');
    var audio = document.getElementById('bgMusic');
    if (!btn || !audio) return;

    var SESSION_KEY = 'musicPlaying';
    var icon = btn.querySelector('.music-icon');

    function setPlayingState(playing) {
      btn.classList.toggle('playing', playing);
      btn.setAttribute('aria-pressed', String(playing));
      btn.setAttribute('aria-label', playing ? 'Pause music' : 'Play music');
      if (icon) icon.textContent = playing ? '♫' : '♪';
    }

    // Show the button only after the visitor enters the celebration.
    function revealButton() { btn.classList.add('visible'); }
    if (document.body.classList.contains('intro-lock-scroll')) {
      document.addEventListener('celebration:entered', revealButton, { once: true });
    } else {
      revealButton();
    }

    audio.addEventListener('error', function () {
      // Missing/broken audio: hide control gracefully, never block the site.
      audio.pause();
      btn.classList.remove('visible', 'playing');
    });

    btn.addEventListener('click', function () {
      if (audio.paused) {
        audio.play().then(function () {
          setPlayingState(true);
          try { sessionStorage.setItem(SESSION_KEY, 'true'); } catch (e) {}
        }).catch(function () {
          setPlayingState(false);
        });
      } else {
        audio.pause();
        setPlayingState(false);
        try { sessionStorage.setItem(SESSION_KEY, 'false'); } catch (e) {}
      }
    });

    // Restore prior in-session choice, but only resume playback after entering.
    setPlayingState(false);
    var wanted = sessionStorage.getItem(SESSION_KEY) === 'true';
    if (wanted) {
      document.addEventListener('celebration:entered', function () {
        audio.play().then(function () { setPlayingState(true); }).catch(function () {});
      }, { once: true });
    }

    // Pause music when the tab is hidden; do not auto-resume.
    document.addEventListener('visibilitychange', function () {
      if (document.hidden && !audio.paused) {
        audio.pause();
        setPlayingState(false);
      }
    });
  }
})();



