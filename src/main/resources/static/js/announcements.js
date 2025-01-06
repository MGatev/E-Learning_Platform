document.addEventListener("DOMContentLoaded", () => {
    let announcements = [];
    let currentAnnouncement = 0;

    fetchAnnouncements(); // Fetch announcements when the page loads
    setInterval(rotateAnnouncements, 5000); // Rotate every 5 seconds

    function fetchAnnouncements() {
        fetch('/announcements')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(fetchedAnnouncements => {
                console.log("Fetched Announcements:", fetchedAnnouncements);
                announcements = fetchedAnnouncements
                rotateAnnouncements(announcements);
            })
            .catch(error => {
                console.error("Error fetching announcements:", error);
            });
    }

    function rotateAnnouncements() {
        const bannerText = document.getElementById('announcementText');
        const banner = document.getElementById('announcementBanner');
        if (announcements.length > 0) {
            banner.classList.remove('hide');
            bannerText.classList.add('fade-out');
            setTimeout(() => {
                bannerText.textContent = announcements[currentAnnouncement];
                bannerText.classList.remove('fade-out');
                bannerText.style.opacity = "1";
                currentAnnouncement = (currentAnnouncement + 1) % announcements.length;
            }, 1000);
        } else {
            banner.classList.add('hide');
        }
    }
});