function toggleForms() {
    const actionType = document.getElementById("actionType").value;
    document.getElementById("formSection").classList.toggle("hidden", actionType !== "form");
    document.getElementById("uploadSection").classList.toggle("hidden", actionType !== "upload");
}

function showSelectedForm() {
    const selected = document.getElementById("pacsType").value;
    document.querySelectorAll(".pacs-form").forEach(form => {
        form.classList.add("hidden");
    });
    document.getElementById(selected).classList.remove("hidden");
}

function submitForm() {
    const actionType = document.getElementById("actionType").value;
    if (actionType === "form") {
        const pacsType = document.getElementById("pacsType").value;
    } else {
        const file = document.getElementById("uploadFile").files[0];
        if (!file) {
            alert("Veuillez sélectionner un fichier à uploader.");
            return;
        }
    }
}

document.addEventListener("DOMContentLoaded", showSelectedForm);