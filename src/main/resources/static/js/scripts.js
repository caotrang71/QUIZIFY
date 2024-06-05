document.addEventListener('DOMContentLoaded', function () {
    let questionCount = 2; // Initial two questions

    document.getElementById('add-question').addEventListener('click', function () {
        questionCount++;
        const container = document.getElementById('questions-container');

        const questionDiv = document.createElement('div');
        questionDiv.classList.add('question');

        const questionLabel = document.createElement('label');
        questionLabel.setAttribute('for', `questionContent${questionCount}`);
        questionLabel.textContent = `Question ${questionCount}`;

        const questionTextarea = document.createElement('textarea');
        questionTextarea.classList.add('form-control');
        questionTextarea.setAttribute('id', `questionContent${questionCount}`);
        questionTextarea.setAttribute('name', `questions[${questionCount - 1}].content`);
        questionTextarea.setAttribute('placeholder', 'Enter question');
        questionTextarea.required = true;

        const choicesLabel = document.createElement('label');
        choicesLabel.setAttribute('for', `questionChoices${questionCount}`);
        choicesLabel.textContent = 'Choices';

        const choicesTextarea = document.createElement('textarea');
        choicesTextarea.classList.add('form-control');
        choicesTextarea.setAttribute('id', `questionChoices${questionCount}`);
        choicesTextarea.setAttribute('name', `questions[${questionCount - 1}].choices[0].content`);
        choicesTextarea.setAttribute('placeholder', 'Enter choices separated by commas');
        choicesTextarea.required = true;

        questionDiv.appendChild(questionLabel);
        questionDiv.appendChild(questionTextarea);
        questionDiv.appendChild(choicesLabel);
        questionDiv.appendChild(choicesTextarea);

        container.appendChild(questionDiv);
    });

    document.querySelector('form').addEventListener('submit', function (event) {
        if (questionCount < 2) {
            event.preventDefault();
            alert('Please add at least 2 questions.');
        }
    });

    document.addEventListener('DOMContentLoaded', function () {
        let questionCount = 1; // Initialize question count

        // Add question
        document.getElementById('add-question').addEventListener('click', function () {
            questionCount++;
            const container = document.getElementById('questions-container');
            const questionDiv = document.createElement('div');
            questionDiv.classList.add('question');
            questionDiv.innerHTML = `
            <div class="form-group">
                <label for="questionContent${questionCount}">Question ${questionCount}</label>
                <textarea class="form-control" id="questionContent${questionCount}" name="questions[${questionCount - 1}].content" placeholder="Enter question" required></textarea>
            </div>
            <div class="options">
                <div class="option">
                    <input type="text" class="form-control" name="questions[${questionCount - 1}].options[0].content" placeholder="Enter option" required>
                    <input type="checkbox" name="questions[${questionCount - 1}].options[0].correct"> Correct Answer
                    <button type="button" class="btn btn-danger btn-sm delete-option">Delete Option</button>
                </div>
            </div>
            <button type="button" class="btn btn-secondary add-option">Add Option</button>
            <button type="button" class="btn btn-danger btn-sm delete-question">Delete Question</button>
        `;
            container.appendChild(questionDiv);
        });

        // Add option
        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('add-option')) {
                const optionsContainer = event.target.parentElement.querySelector('.options');
                const optionDiv = document.createElement('div');
                optionDiv.classList.add('option');
                optionDiv.innerHTML = `
                <input type="text" class="form-control" name="${event.target.previousElementSibling.name.replace('.content', '.options[' + optionsContainer.children.length + '].content')}" placeholder="Enter option" required>
                <input type="checkbox" name="${event.target.previousElementSibling.name.replace('.content', '.options[' + optionsContainer.children.length + '].correct')}"> Correct Answer
                <button type="button" class="btn btn-danger btn-sm delete-option">Delete Option</button>
            `;
                optionsContainer.appendChild(optionDiv);
            }
        });

        // Delete option
        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('delete-option')) {
                event.target.parentElement.remove();
            }
        });

        // Delete question
        document.addEventListener('click', function (event) {
            if (event.target.classList.contains('delete-question')) {
                event.target.parentElement.remove();
            }
        });
    });

});
