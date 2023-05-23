const registerForm = document.getElementById("register-form");
registerForm.addEventListener("submit", (event) => {
    event.preventDefault();
    const formData = new FormData(registerForm);
    const userData = {
        username: formData.get("username"),
        password: formData.get("password"),
        name: formData.get("name"),
        lastname: formData.get("lastname"),
        email: formData.get("email"),
        phone: formData.get("phone"),
        roleId: 1
    };
    fetch("/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData),
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            }else if(response.status == 409){
                alert("Ya hay un usuario con ese nombre de usuario.")
            }else {
                alert("Algo ha salido mal al registrarse.");
            }
        })
        .then((data) => {
            console.log("Registro exitoso:", data);
            let token = data.token;
            localStorage.setItem('authToken', token);
            localStorage.setItem('userid', "tbd");
            window.location.href = '/main';

        })
        .catch((error) => {
            console.error("Error:", error);
        });
});