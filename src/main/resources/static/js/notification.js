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


//make as read
function markAsRead(notificationId) {
    const url = `http://localhost:8081/Quizify/quiz-banks/notifications/mark-as-read/${notificationId}`;
    fetch(url, {
        method: 'PUT',
        headers:{
            'Content-Type':'application/json'
        }
    })
        .then(response => {
            if (response.ok){
                alert('make as read success');
                location.reload();
            }else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .catch(error=> {
            console.error('Error:' ,error);
            alert('failed to make as read')
        })
}
// when click link
function markAsReadLink(notificationId) {
    const url = `http://localhost:8081/Quizify/quiz-banks/notifications/mark-as-read/${notificationId}`;
    fetch(url, {
        method: 'PUT',
        headers:{
            'Content-Type':'application/json'
        }
    })
        .then(response => {
            if (response.ok){

            }else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .catch(error=> {
            console.error('Error:' ,error);
            alert('failed to make as read')
        })
}

//make as new
function markAsNew(notificationId) {
    const url = `http://localhost:8081/Quizify/quiz-banks/notifications/mark-as-new/${notificationId}`;
    fetch(url, {
        method: 'PUT',
        headers:{
            'Content-Type':'application/json'
        }
    })
        .then(response => {
            if (response.ok){
                alert('make as new success');
                location.reload();
            }else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .catch(error=> {
            console.error('Error:' ,error);
            alert('failed to make as read')
        })
}

//delete notification
function deleteNotification(notificationId) {
    if (confirm("Are you sure want to delete this notification?")) {
        const url = `http://localhost:8081/Quizify/quiz-banks/notifications/delete/${notificationId}`;
        fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert('delete notification successfully.');
                    location.reload();
                } else {
                    return response.text().then(text => {
                        throw new Error(text)
                    });
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('failed to delete this notification')
            })
    }
}