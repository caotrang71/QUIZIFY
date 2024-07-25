document.querySelectorAll('.fa-star').forEach(function(star) {
    star.addEventListener('click', function() {
        var value = star.getAttribute('data-value');
        document.querySelector('input[name="star"]').value = value;

        // Làm sạch tất cả các ngôi sao
        document.querySelectorAll('.fa-star').forEach(function(star) {
            star.classList.remove('checked');
        });

        // Tô màu tất cả các ngôi sao đến ngôi sao đã được nhấn
        for (var i = 1; i <= value; i++) {
            document.querySelector('.fa-star[data-value="' + i + '"]').classList.add('checked');
        }
    });
});


document.addEventListener("DOMContentLoaded", function() {
    const stars = document.querySelectorAll(".rating-box i");

    stars.forEach((star, index) => {
        star.addEventListener("click", () => {
            stars.forEach((s, i) => {
                if (i <= index) {
                    s.classList.add("active");
                } else {
                    s.classList.remove("active");
                }
            });
        });
    });
});
