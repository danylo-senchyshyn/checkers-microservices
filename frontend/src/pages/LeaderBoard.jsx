import React, { useEffect, useState } from 'react';
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import "../styles/leaderboard.css";

export default function LeaderBoard() {
    const [scores, setScores] = useState([]);
    const maxPlayers = 10;

    // Загружаем игроков из localStorage
    const player1 = localStorage.getItem("player1");
    const player2 = localStorage.getItem("player2");
    const avatar1 = localStorage.getItem("avatar1");
    const avatar2 = localStorage.getItem("avatar2");

    // Подгружаем данные с сервера
    useEffect(() => {
        fetch("http://localhost:8080/api/leaderboard")
            .then((res) => res.json())
            .then(setScores)
            .catch((err) => console.error("Failed to load leaderboard:", err));
    }, []);

    return (
        <>
            <Header />

            <div className="leaderboard-container">
                <h2 className="leaderboard-title">🏆 Leaderboard</h2>

                {Array.from({ length: maxPlayers }, (_, i) => {
                    const score = i < scores.length ? scores[i] : null;

                    if (!score)
                        return <div key={i} className={`player shade-${i}`} />;

                    const avatarSrc =
                        score.player === player1
                            ? avatar1
                            : score.player === player2
                                ? avatar2
                                : "/images/board/empty_black.png";

                    return (
                        <div key={i} className={`player shade-${i}`}>
                            <img className="avatar" src={avatarSrc} alt="avatar" />
                            <span className="nickname">{score.player}</span>
                            <span className="points">{score.points}</span>
                        </div>
                    );
                })}
            </div>

            <Footer />
        </>
    );
}