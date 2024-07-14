document.addEventListener('DOMContentLoaded', function() {
    const button = document.getElementById('notificationButton');
    const panel = document.getElementById('notificationPanel');

    button.addEventListener('click', function(event) {
        panel.classList.toggle('active');
        event.stopPropagation(); // Ngăn chặn sự kiện click từ việc bong bóng lên document
    });

    document.addEventListener('click', function(event) {
        if (!panel.contains(event.target) && !button.contains(event.target)) {
            panel.classList.remove('active');
        }
    });

    panel.addEventListener('click', function(event) {
        event.stopPropagation(); // Ngăn chặn sự kiện click từ việc bong bóng lên document
    });
});