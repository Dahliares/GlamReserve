window.onload = function () {

    const contactForm = document.getElementById("contact-form");

    contactForm.addEventListener("submit", (event) => {
        event.preventDefault();
        const formData = new FormData(contactForm);
        const userData = {
            name: formData.get("name"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            message: formData.get("message")
        };
        fetch("/contact", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(userData),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    alert("No se ha podido enviar mensaje.");
                }
            })
            .then((data) => {

                alert("tu mensaje ha sido enviado");
                window.location.href = '/contact';


            })
            .catch((error) => {
                console.error("Error:", error);
            });
    });

}