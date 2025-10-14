// REVIEWS.JS
function openModal() {
    document.getElementById('reviewModal').style.display = 'flex';
}

function closeReviewModal() {
    document.getElementById('reviewModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('reviewModal');
    if (event.target == modal) {
        closeReviewModal();
    }
}


document.addEventListener('DOMContentLoaded', function () {
    const addButton = document.querySelector('.add-review-button');
    if (addButton) {
        addButton.addEventListener('click', function (event) {
            event.preventDefault();
            localStorage.setItem("scrollToReviews", "true");
            openModal();
        });
    }

    if (localStorage.getItem("scrollToReviews") === "true") {
        localStorage.removeItem("scrollToReviews");
        const section = document.getElementById("reviews-section");
        if (section) {
            section.scrollIntoView({ behavior: "instant" });
        }
    }

    const paginationLinks = document.querySelectorAll('.pagination a');
    paginationLinks.forEach(link => {
        link.addEventListener('click', function() {
            localStorage.setItem("scrollToReviews", "true");
        });
    });
});