document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("[data-password-toggle]").forEach((button) => {
    button.addEventListener("click", () => {
      const targetSelector = button.getAttribute("data-password-toggle");
      const input = document.querySelector(targetSelector);
      if (!input) {
        return;
      }

      const nextType = input.type === "password" ? "text" : "password";
      input.type = nextType;
      button.textContent = nextType === "password" ? "Show" : "Hide";
      button.setAttribute("aria-pressed", String(nextType === "text"));
    });
  });

  document.querySelectorAll("form.js-submit-loading").forEach((form) => {
    form.addEventListener("submit", () => {
      const submitButton = form.querySelector("[type='submit']");
      if (!submitButton || submitButton.dataset.loadingApplied === "true") {
        return;
      }

      submitButton.dataset.loadingApplied = "true";
      submitButton.dataset.originalText = submitButton.innerHTML;
      submitButton.disabled = true;
      submitButton.innerHTML = "<span class=\"spinner-border spinner-border-sm me-2\" aria-hidden=\"true\"></span>Signing in...";
    });
  });

  document.querySelectorAll("[data-filter-target]").forEach((input) => {
    const selector = input.getAttribute("data-filter-target");
    if (!selector) {
      return;
    }

    const rows = Array.from(document.querySelectorAll(`${selector} [data-filter-row]`));
    const emptyState = document.querySelector(`[data-filter-empty='${selector}']`);

    const applyFilter = () => {
      const query = input.value.trim().toLowerCase();
      let visibleCount = 0;

      rows.forEach((row) => {
        const haystack = (row.getAttribute("data-search") || row.textContent || "").toLowerCase();
        const isMatch = !query || haystack.includes(query);
        row.classList.toggle("d-none", !isMatch);
        if (isMatch) {
          visibleCount++;
        }
      });

      if (emptyState) {
        emptyState.classList.toggle("d-none", visibleCount !== 0);
      }
    };

    input.addEventListener("input", applyFilter);
    applyFilter();
  });
});
