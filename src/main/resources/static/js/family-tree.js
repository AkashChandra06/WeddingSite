/**
 * Interactive Family Tree.
 * Clicking a tree node opens a Bootstrap modal populated with that member's details.
 * Works recursively for any generation depth (grandparents -> parents -> couple -> children...).
 */
(function () {
  function initFamilyTree() {
    const nodes = document.querySelectorAll('.tree-node');
    const modalEl = document.getElementById('familyMemberModal');
    if (!modalEl || nodes.length === 0) return;

    const modal = new bootstrap.Modal(modalEl);
    const modalPhoto = document.getElementById('familyModalPhoto');
    const modalName = document.getElementById('familyModalName');
    const modalRelationship = document.getElementById('familyModalRelationship');
    const modalSide = document.getElementById('familyModalSide');
    const modalDescription = document.getElementById('familyModalDescription');

    nodes.forEach(function (node) {
      node.addEventListener('click', function () {
        modalPhoto.src = node.getAttribute('data-photo') || '/images/family/placeholder.jpg';
        modalName.textContent = node.getAttribute('data-name') || '';
        modalRelationship.textContent = node.getAttribute('data-relationship') || '';
        modalSide.textContent = node.getAttribute('data-side') || '';
        modalDescription.textContent = node.getAttribute('data-description') || '';
        modal.show();
      });
    });
  }

  document.addEventListener('DOMContentLoaded', initFamilyTree);
})();

