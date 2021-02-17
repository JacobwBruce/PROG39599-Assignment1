window.onload = () => {
    const slider = document.getElementById('ageRange');
    const ageText = document.getElementById('ageText');

    ageText.innerText = slider.value;

    slider.addEventListener('change', (e) => {
        ageText.innerText = e.target.value;
    });
};
