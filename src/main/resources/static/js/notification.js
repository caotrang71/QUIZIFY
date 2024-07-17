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

function makeAsRead(id) {
    const url = `http://localhost:8081/Quizify/notifications/mark-as-read/${id}`;
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                const notificationItem = document.querySelector(`[data-id='${id}']`);
                if (notificationItem) {
                    notificationItem.classList.remove('new');
                }
            } else {
                console.error('Failed to mark as read:', response.statusText);
            }
        })
        .catch(error => console.error('Error:', error));
}


function makeAsNew(id) {
    const url = `http://localhost:8081/Quizify/quiz-banks/notifications/mark-as-new/${id}`;
    fetch(url, {
        method: 'PUT',
        headers:{
            'Content-Type':'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                document.querySelector(`[th\\:onclick="makeAsRead(${id})"]`).parentElement.classList.add('new');
            }
        })
        .catch(error => console.error('Error:', error));
}