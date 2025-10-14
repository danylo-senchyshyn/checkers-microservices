// game.js
let firstCoordinates = null;
let possibleMoves = [];

let player1Block, player2Block;

const LS_KEYS = {
    player1: 'player1',
    player2: 'player2',
    avatar1: 'avatar1',
    avatar2: 'avatar2'
};

document.addEventListener('DOMContentLoaded', function () {
    setTimeout(() => {
        if (winnerFromServer === null)
            sessionStorage.removeItem('gameOverModalShown');
        console.log('winnerFromServer:', winnerFromServer);

        player1Block = document.getElementById('player1-block');
        player2Block = document.getElementById('player2-block');

        console.log("До updatePlayerInfo — gameOverModalShown =", sessionStorage.getItem('gameOverModalShown'));
        updatePlayerInfo();

        const closeButton = document.querySelector('.close-button');
        if (closeButton) {
            closeButton.addEventListener('click', closeGameOverModal);
        }

        const scrollFlag = localStorage.getItem('scrollToGame');
        if (scrollFlag === 'true') {
            const gameBoard = document.getElementById('game-board');
            if (gameBoard) {
                gameBoard.scrollIntoView({ behavior: 'instant', block: 'start' });
                window.scrollBy(0, -500);
            }
            localStorage.removeItem('scrollToGame');
            handleGameOverModal();
        }

    }, 0);
});


async function executeMove(row, col) {
    const url = `/checkers?fr=${firstCoordinates.row}&fc=${firstCoordinates.col}&tr=${row}&tc=${col}`;
    try {
        const response = await fetch(url, { method: 'GET' });
        if (response.ok) {
            localStorage.setItem('scrollToGame', 'true');
            window.location.href = "/checkers";
        } else {
            console.error("Ошибка при выполнении хода");
        }
    } catch (error) {
        console.error("Ошибка при запросе на выполнение хода:", error);
    }
}

async function selectTile(row, col) {
    console.log(`Tile clicked: Row ${row}, Column ${col}`);
    const selectedTile = document.getElementById(`tile-${row}-${col}`);
    if (!selectedTile) {
        console.error(`Tile with ID tile-${row}-${col} not found.`);
        return;
    }

    const piece = selectedTile.querySelector('.piece-checker, .piece-king');

    if (firstCoordinates && firstCoordinates.row === row && firstCoordinates.col === col) {
        clearSelection();
        return;
    }

    if (firstCoordinates) {
        const isMoveValid = possibleMoves.some(move => move.row === row && move.col === col);
        if (isMoveValid) {
            await executeMove(row, col);
            return;
        } else {
            clearSelection();
        }
    }

    if (!piece) {
        clearSelection();
        console.warn(`No piece found on tile-${row}-${col}.`);
        return;
    }

    const moves = await getPossibleMoves(row, col);
    if (moves.length === 0) {
        console.warn(`No possible moves for tile-${row}-${col}.`);
        return;
    }

    clearSelection();
    firstCoordinates = { row, col };
    possibleMoves = moves;
    selectedTile.classList.add('selected');
    highlightPossibleMoves(moves);
}

async function getPossibleMoves(row, col) {
    try {
        const response = await fetch(`/checkers/moves?row=${row}&col=${col}`);
        if (!response.ok) throw new Error(`Failed to fetch moves for tile-${row}-${col}.`);
        const moves = await response.json();
        return moves.map(move => ({ row: move[0], col: move[1] }));
    } catch (error) {
        console.error('Ошибка при запросе ходов:', error);
        showError("Не удалось получить возможные ходы.");
        return [];
    }
}

function highlightPossibleMoves(moves) {
    moves.forEach(move => {
        const tile = document.getElementById(`tile-${move.row}-${move.col}`);
        if (tile) {
            tile.classList.add('possible-move');
        } else {
            console.warn(`Tile for possible move not found: tile-${move.row}-${move.col}`);
        }
    });
}

function clearSelection() {
    document.querySelectorAll('.tile').forEach(tile => {
        tile.classList.remove('selected', 'possible-move');
    });

    firstCoordinates = null;
    possibleMoves = [];
}

function updatePlayerInfo() {
    const player1 = localStorage.getItem(LS_KEYS.player1);
    const player2 = localStorage.getItem(LS_KEYS.player2);
    const avatar1 = localStorage.getItem(LS_KEYS.avatar1);
    const avatar2 = localStorage.getItem(LS_KEYS.avatar2);

    if (player1 && player2 && avatar1 && avatar2) {
        document.getElementById('player1-name').textContent = player1;
        document.getElementById('player2-name').textContent = player2;
        document.getElementById('player1-avatar').src = avatar1;
        document.getElementById('player2-avatar').src = avatar2;
    } else {
        console.error('Player data is missing in localStorage.');
    }

    fetch('/checkers/save-players', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ player1, player2, avatar1, avatar2 })
    })
        .then(response => {
            if (!response.ok) throw new Error();
            handleGameOverModal();
        })
        .catch(error => {
            console.error("Error saving players:", error);
        });
}

function handleGameOverModal() {
    if (sessionStorage.getItem('gameOverModalShown') === null) {
        sessionStorage.setItem('gameOverModalShown', 'false');
        console.log("gameOverModalShown null > false");
    }
    if (typeof gameOverFromServer !== 'undefined' && gameOverFromServer) {
        const modalAlreadyShown = sessionStorage.getItem('gameOverModalShown');
        console.log("modalAlreadyShown:", modalAlreadyShown);

        if (modalAlreadyShown === 'false') {
            const modal = document.getElementById("gameOverModal");
            const winnerText = document.getElementById("winnerModal");
            const winnerAvatar = document.getElementById("winnerAvatar");

            if (modal) modal.style.display = "block";
            if (winnerText) winnerText.textContent = winnerFromServer;

            const avatar1 = localStorage.getItem(LS_KEYS.avatar1);
            const avatar2 = localStorage.getItem(LS_KEYS.avatar2);
            const player1 = localStorage.getItem(LS_KEYS.player1);

            if (winnerAvatar) {
                winnerAvatar.src = (winnerFromServer === player1) ? avatar1 : avatar2;
            }

            sessionStorage.setItem('gameOverModalShown', 'true');
        } else {
            console.log("Модалка уже была показана.");
        }
    }
}

function closeGameOverModal() {
    const modal = document.getElementById('gameOverModal');
    if (modal) {
        modal.style.display = 'none';
    } else {
        console.error('Элемент gameOverModal не найден.');
    }
}