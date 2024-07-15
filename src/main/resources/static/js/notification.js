// document.addEventListener('DOMContentLoaded', function() {
//     const button = document.getElementById('notificationButton');
//     const panel = document.getElementById('notificationPanel');
//
//     button.addEventListener('click', function(event) {
//         panel.classList.toggle('active');
//         event.stopPropagation(); // Ngăn chặn sự kiện click từ việc bong bóng lên document
//     });
//
//     document.addEventListener('click', function(event) {
//         if (!panel.contains(event.target) && !button.contains(event.target)) {
//             panel.classList.remove('active');
//         }
//     });
//
//     panel.addEventListener('click', function(event) {
//         event.stopPropagation(); // Ngăn chặn sự kiện click từ việc bong bóng lên document
//     });
// });

document.addEventListener('DOMContentLoaded', function() {
    const notificationButton = document.getElementById('notificationButton');
    const notificationPanel = document.getElementById('notificationPanel');
    const closeIcon = document.getElementById('closeIcon');

    notificationButton.addEventListener('click', function(event) {
        notificationPanel.classList.toggle('active');
        event.stopPropagation();
    });

    closeIcon.addEventListener('click', function(event) {
        notificationPanel.classList.remove('active');
        event.stopPropagation();
    });

    document.addEventListener('click', function(event) {
        if (!notificationPanel.contains(event.target) && !notificationButton.contains(event.target)) {
            notificationPanel.classList.remove('active');
        }
    });

    notificationPanel.addEventListener('click', function(event) {
        event.stopPropagation();
    });
});