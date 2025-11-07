import React, { useEffect, useState } from 'react';
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";
import "../styles/reviews.css";

export default function Reviews() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [comment, setComment] = useState("");
    const [rating, setRating] = useState(5);
    const [comments, setComments] = useState([]);
    const [averageRating, setAverageRating] = useState(0);

    // 🧑‍💻 Загружаем текущих игроков
    const player1 = localStorage.getItem("player1");
    const player2 = localStorage.getItem("player2");
    const avatar1 = localStorage.getItem("avatar1");
    const avatar2 = localStorage.getItem("avatar2");

    // 📥 Получение отзывов и рейтинга
    useEffect(() => {
        fetch("http://localhost:8080/api/comments")
            .then((res) => res.json())
            .then(setComments)
            .catch(() => setComments([]));

        fetch("http://localhost:8080/api/rating/average")
            .then((res) => res.json())
            .then(setAverageRating)
            .catch(() => setAverageRating(0));
    }, []);

    // 📝 Отправка нового отзыва
    const handleSubmit = async (e) => {
        e.preventDefault();

        const player = player1 || "Anonymous";
        const avatar = avatar1 || "/images/board/empty_black.png";

        try {
            const response = await fetch("http://localhost:8080/api/review", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({player, comment, rating, avatar}),
            });

            if (response.ok) {
                alert("✅ Review added successfully!");
                setComment("");
                setRating(5);
                setIsModalOpen(false);

                // Обновляем данные
                const newComments = await fetch("http://localhost:8080/api/comments").then((res) =>
                    res.json()
                );
                setComments(newComments);
            } else {
                alert("❌ Failed to add review");
            }
        } catch (err) {
            console.error("Error adding review:", err);
        }
    };

    return (
        <>
            <Header/>

            <main className="reviews-page">
                <div className="reviews-container">
                    {/* 📊 Сводка рейтинга */}
                    <div className="review-summary">
                        <div className="summary-content">
                            <div className="summary-line">
                                <span>
                                    Based on <strong>{comments.length}</strong> reviews
                                </span>
                                <span className="rating-average">
                                    {averageRating.toFixed(1)} / 5
                                </span>
                                <div className="stars-inline">
                                    {Array.from({length: 5}, (_, i) => (
                                        <svg
                                            key={i}
                                            className={`w-4 h-4 ${
                                                i + 1 <= Math.round(averageRating)
                                                    ? "text-yellow-300"
                                                    : "text-gray-300"
                                            }`}
                                            xmlns="http://www.w3.org/2000/svg"
                                            fill="currentColor"
                                            viewBox="0 0 22 20"
                                        >
                                            <path
                                                d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                        </svg>
                                    ))}
                                </div>
                            </div>
                        </div>

                        <button
                            className="add-review-button"
                            onClick={() => setIsModalOpen(true)}
                        >
                            Add Review
                        </button>
                    </div>

                    {/* 💬 Список комментариев */}
                    <section className="reviews-section">
                        {comments.length === 0 ? (
                            <div className="no-comments-wrapper">
                                <p className="no-comments">No comments yet.</p>
                            </div>
                        ) : (
                            <div className="reviews">
                                {comments.map((c, i) => (
                                    <div className="review" key={i}>
                                        <div className="review-header">
                                            <div className="review-info">
                                                <img
                                                    className="avatar"
                                                    src={
                                                        c.player === player1
                                                            ? avatar1
                                                            : c.player === player2
                                                                ? avatar2
                                                                : c.avatar || "/images/board/empty_black.png"
                                                    }
                                                    alt="avatar"
                                                />
                                                <span className="player-name">{c.player}</span>
                                            </div>
                                            <span className="review-date">
                    {new Date(c.commentedOn).toLocaleString()}
                  </span>
                                        </div>
                                        <div className="review-body">{c.comment}</div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </section>
                </div>


                {/* 🪶 Модалка добавления отзыва */}
                {isModalOpen && (
                    <div
                        id="reviewModal"
                        className="modal open"
                        onClick={(e) => e.target.id === "reviewModal" && setIsModalOpen(false)}
                    >
                        <div className="modal-content">
                            <button className="close-button" onClick={() => setIsModalOpen(false)}>
                                ✖
                            </button>
                            <h2>Add Your Review</h2>

                            <form onSubmit={handleSubmit}>
                                <label>Comment:</label>
                                <textarea
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                    required
                                />

                                <label>Rating:</label>
                                <select
                                    value={rating}
                                    onChange={(e) => setRating(parseInt(e.target.value))}
                                >
                                    {[1, 2, 3, 4, 5].map((num) => (
                                        <option key={num} value={num}>
                                            {num} {"★".repeat(num)}
                                        </option>
                                    ))}
                                </select>

                                <button type="submit" className="submit-review-btn">Submit</button>
                            </form>
                        </div>
                    </div>
                )}
            </main>

            <Footer/>
        </>
    );
}