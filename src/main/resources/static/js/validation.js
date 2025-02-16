document.addEventListener("DOMContentLoaded", function() {
    let courseNameInput = document.getElementById("courseName");
    let startDateInput = document.getElementById("startDate");
    let endDateInput = document.getElementById("endDate");
    let lectureHoursInput = document.getElementById("lectureHours");
    let labCountInput = document.getElementById("labCount");

    courseNameInput.addEventListener("input", function() {
        let value = this.value.trim().replace(/\s+/g, " "); // Normalize spaces
        this.value = value;
        validateCourseName();
    });

    startDateInput.addEventListener("change", function() {
        validateStartDate();
        validateEndDate(); // Also validate end date when start date changes
    });

    endDateInput.addEventListener("change", function() {
        validateEndDate();
    });

    lectureHoursInput.addEventListener("input", function() {
        validateLectureHours();
    });

    labCountInput.addEventListener("input", function() {
        validateLabCount();
    });

    function validateCourseName() {
        let value = courseNameInput.value.trim();
        let errorElement = document.getElementById("courseNameError");

        if (value.length === 0) {
            errorElement.style.display = "block";
            courseNameInput.classList.add("error-border");
            courseNameInput.classList.remove("valid-border");
        } else {
            errorElement.style.display = "none";
            courseNameInput.classList.remove("error-border");
            courseNameInput.classList.add("valid-border");
        }
    }

    function validateStartDate() {
        let value = startDateInput.value;
        let errorElement = document.getElementById("startDateError");

        if (!value) {
            errorElement.style.display = "block";
            startDateInput.classList.add("error-border");
            startDateInput.classList.remove("valid-border");
        } else {
            errorElement.style.display = "none";
            startDateInput.classList.remove("error-border");
            startDateInput.classList.add("valid-border");
        }
    }

    function validateEndDate() {
        let startDate = new Date(startDateInput.value);
        let endDate = new Date(endDateInput.value);
        let errorElement = document.getElementById("endDateError");

        if (!endDateInput.value || endDate <= startDate) {
            errorElement.style.display = "block";
            endDateInput.classList.add("error-border");
            endDateInput.classList.remove("valid-border");
        } else {
            errorElement.style.display = "none";
            endDateInput.classList.remove("error-border");
            endDateInput.classList.add("valid-border");
        }
    }

    function validateLectureHours() {
        let value = parseFloat(lectureHoursInput.value);
        let errorElement = document.getElementById("lectureHoursError");

        if (isNaN(value) || value <= 0) {
            errorElement.style.display = "block";
            lectureHoursInput.classList.add("error-border");
            lectureHoursInput.classList.remove("valid-border");
        } else {
            errorElement.style.display = "none";
            lectureHoursInput.classList.remove("error-border");
            lectureHoursInput.classList.add("valid-border");
        }
    }

    function validateLabCount() {
        let value = parseInt(labCountInput.value);
        let errorElement = document.getElementById("labCountError");

        if (isNaN(value) || value < 0) {
            errorElement.style.display = "block";
            labCountInput.classList.add("error-border");
            labCountInput.classList.remove("valid-border");
        } else {
            errorElement.style.display = "none";
            labCountInput.classList.remove("error-border");
            labCountInput.classList.add("valid-border");
        }
    }
});
