window.onload = function () {
    
    const registerForm = document.getElementById("register2-form");
    let input = document.getElementById("file");
    input.addEventListener("change", fileRead, false);
    let imgurl;


    registerForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const formData = new FormData(registerForm);

        let owner = parseInt(localStorage.getItem("userid"));
        const address = formData.get("calle") + ", " + formData.get("numero") + ", " + formData.get("cp") + " " + formData.get("localidad");


        const userData = {
            name: formData.get("name"),
            category: formData.get("category"),
            owner: {id: owner},
            description: formData.get("description"),
            image: imgurl,
            address: address
        };
        fetch("/company/register", {
            method: "POST",
            headers: {"Content-Type": "application/json" , "Authorization": localStorage.getItem('authToken')},
            body: JSON.stringify(userData),
        })
            .then((response) => {
                if (response.ok) {
                    return response;
                } else if (response.status == 404) {
                    alert("Dirección no encontrada.")
                } else {
                    alert("Algo ha salido mal al registrar la empresa.");
                }
            })
            .then((data) => {
                window.location.href = '/main/' + localStorage.getItem("userid");
            });
    });



    function fileRead(event) {
        const file = event.target.files[0];
        const fileReader = new FileReader();

        fileReader.onload = function(event) {
            imgurl =event.target.result
            console.log(fileReader.result);
        }
        fileReader.readAsDataURL(file);

    }



}