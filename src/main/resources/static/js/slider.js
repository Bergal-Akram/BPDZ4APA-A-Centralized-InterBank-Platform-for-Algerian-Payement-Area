document.addEventListener("DOMContentLoaded", function () {
    const track = document.getElementById("slider-track");
    if (!track) return; // في حال لم يوجد العنصر لتجنب الخطأ
  
    const slides = Array.from(track.children);
    for (let i = 0; i < slides.length; i++) {
      const clone = slides[i].cloneNode(true);
      track.appendChild(clone);
    }
  
    const totalSlides = slides.length * 2;
    const speedSeconds = totalSlides * 2;
    track.style.animationDuration = `${speedSeconds}s`;
  });
  