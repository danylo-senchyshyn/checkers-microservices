import React from 'react';
import Footer from "../components/Footer.jsx";
import "../styles/about.css";
import Header from "../components/Header.jsx";

export default function About() {
    return (
        <>
            <Header />

            <div className="about">
                <h2>About This Project</h2>

                <p>
                    This project was developed by <strong>Danylo Senchyshyn</strong>, a student of the Technical University of Košice (TUKE), for the
                    course <em>"Komponentové Programovanie"</em> (Component Programming).
                </p>

                <h3>Project Aim</h3>
                <p>
                    The goal of this project is to create an interactive checkers game that allows users to enjoy a strategic game of
                    checkers online. The project showcases various programming concepts such as object-oriented programming, UI
                    design, and backend integration.
                </p>

                <h3>Technologies Used</h3>
                <p>
                    The project was developed using <strong>Java</strong> for the backend and frontend, and integrated with <strong>Spring Boot</strong> for the
                    server-side functionality. <strong>PostgreSQL</strong> is used for storing data such as user scores and ratings.
                </p>

                <h3>Key Features</h3>
                <p>
                    The game includes features such as user authentication, real-time gameplay, player rankings, and an intuitive UI
                    designed to enhance the user experience.
                </p>
            </div>

            <Footer />
        </>
    );
}