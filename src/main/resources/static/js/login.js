const loginForm = document.getElementById("login-form");
loginForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const formData = new FormData(loginForm);
    const userData = {
        username: formData.get("username"),
        password: formData.get("password"),
    };
    fetch("/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData),
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                alert("Algo ha salido mal al hacer login.");
            }
        })
        .then((data) => {

            console.log("Login exitoso:", data);
            let token = data.token;
            localStorage.setItem('authToken', token);
            localStorage.setItem('userid', "tbd");
            window.location.href = '/main';
        })
        .catch((error) => {
            console.error("Error:", error);
        });
});