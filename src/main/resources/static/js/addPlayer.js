window.onload = () => {
    const slider = document.getElementById('ageRange');
    const ageText = document.getElementById('ageText');

    slider.addEventListener('change', (e) => {
        ageText.innerText = e.target.value;
    });
};
