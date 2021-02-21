window.onload = () => {
    const teamOneSelect = document.getElementById('team-one-select');
    const teamOneValueInput = document.getElementById('player-one-value');
    const teamOnePlayers = document.getElementsByClassName('team-one-players');

    teamOneSelect.addEventListener('change', () => {
        for (let i = 0; i < teamOnePlayers.length; i++) {
            if (teamOnePlayers[i].id === teamOneSelect.value) {
                teamOnePlayers[i].classList.remove('hidden');

                teamOnePlayers[i].addEventListener('change', (e) => {
                    teamOneValueInput.value = e.target.value;
                });
            } else {
                teamOnePlayers[i].classList.add('hidden');
            }
        }
    });

    const teamTwoSelect = document.getElementById('team-two-select');
    const teamTwoValueInput = document.getElementById('player-two-value');
    const teamTwoPlayers = document.getElementsByClassName('team-two-players');
    teamTwoSelect.addEventListener('change', () => {
        for (let i = 0; i < teamTwoPlayers.length; i++) {
            if (teamTwoPlayers[i].id === teamTwoSelect.value) {
                teamTwoPlayers[i].classList.remove('hidden');
                teamTwoPlayers[i].addEventListener('change', (e) => {
                    teamTwoValueInput.value = e.target.value;
                });
            } else {
                teamTwoPlayers[i].classList.add('hidden');
            }
        }
    });

    const moveTab = document.getElementById('move-tab');
    const swapTab = document.getElementById('swap-tab');
    const action = document.getElementById('action');

    moveTab.addEventListener('click', () => {
        action.value = 'move';
    });
    swapTab.addEventListener('click', () => {
        action.value = 'swap';
    });
};
