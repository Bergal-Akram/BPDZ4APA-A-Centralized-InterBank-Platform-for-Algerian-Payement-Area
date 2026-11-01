document.addEventListener('DOMContentLoaded', function () {
  const toggleDarkMode = document.getElementById('toggleDarkMode');
  const body = document.body;

  // تحديث الترجمة عند تغيير السمة
  function updateDarkModeTranslation() {
    if (body.classList.contains('dark-mode')) {
      toggleDarkMode.setAttribute('data-i18n', 'light_mode');
    } else {
      toggleDarkMode.setAttribute('data-i18n', 'dark_mode');
    }

    // تحديث النص باستخدام كود الترجمة الخاص بك
    if (typeof updateTranslations === 'function') {
      updateTranslations(); // نعيد تشغيل دالة الترجمة لتحديث النص حسب data-i18n
    }
  }

  // تحميل التفضيل المحفوظ
  const savedMode = localStorage.getItem('darkMode');
  if (savedMode === 'enabled') {
    body.classList.add('dark-mode');
  }

  updateDarkModeTranslation();

  toggleDarkMode.addEventListener('click', function () {
    body.classList.toggle('dark-mode');

    if (body.classList.contains('dark-mode')) {
      localStorage.setItem('darkMode', 'enabled');
    } else {
      localStorage.setItem('darkMode', 'disabled');
    }

    updateDarkModeTranslation();
  });
});
// ddd
