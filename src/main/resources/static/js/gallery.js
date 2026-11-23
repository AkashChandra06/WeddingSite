/**
 * Photo Gallery: category filtering + click-to-enlarge lightbox.
 */
(function () {
  function initGalleryFilter() {
    const filterButtons = document.querySelectorAll('.gallery-filter-btn');
    const galleryItems = document.querySelectorAll('.gallery-item');

    filterButtons.forEach(function (btn) {
      btn.addEventListener('click', function () {
        filterButtons.forEach(function (b) { b.classList.remove('active'); });
        btn.classList.add('active');

        const category = btn.getAttribute('data-category');
        galleryItems.forEach(function (item) {
          if (category === 'all' || item.getAttribute('data-category') === category) {
            item.style.display = '';
          } else {
            item.style.display = 'none';
          }
        });
      });
    });
  }

  function initLightbox() {
    const images = document.querySelectorAll('.gallery-item img');
    const lightboxModalEl = document.getElementById('galleryLightboxModal');
    if (!lightboxModalEl || images.length === 0) return;

    const lightboxModal = new bootstrap.Modal(lightboxModalEl);
    const lightboxImage = document.getElementById('lightboxImage');
    const lightboxCaption = document.getElementById('lightboxCaption');

    images.forEach(function (img) {
      img.addEventListener('click', function () {
        lightboxImage.src = img.src;
        lightboxCaption.textContent = img.getAttribute('data-caption') || '';
        lightboxModal.show();
      });
    });
  }

  document.addEventListener('DOMContentLoaded', function () {
    initGalleryFilter();
    initLightbox();
  });
})();

