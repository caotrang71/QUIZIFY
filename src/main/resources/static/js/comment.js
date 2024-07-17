function toggleDropdown(element) {
    var dropdownContent = element.nextElementSibling;
    dropdownContent.classList.toggle("show");
}

// Đóng menu dropdown nếu người dùng nhấp ra ngoài nó
window.onclick = function(event) {
    if (!event.target.matches('.dropdown-btn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}


// function editComment() {
//     alert("Edit comment functionality here.");
//     // Logic để chỉnh sửa comment
// }
//
// function deleteComment() {
//     if (confirm("Are you sure you want to delete this comment?")) {
//         alert("Delete comment functionality here.");
//         // Logic để xóa comment
//     }
// }