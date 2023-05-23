/* Demanar dades user per JSON */
async function verifyToken(token) {
    let userProfile=null;
    fetch("/user/verify", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ token }),
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
            })
            .then((data) => {
                userProfile=('userProfile:', data);
                localStorage.setItem("userid", userProfile.id);
            })
            .catch((error) => {
            });
}
/* Crea una constant per trobat el token i altre constant, per carregar les dades al main */
async function onMainPageLoad() {
    try {
        if(window.location.pathname != "/login"){
            const token = localStorage.getItem('authToken');
            const userData = await verifyToken(token);
        }
    }catch(error){
    }
}
window.addEventListener('load', onMainPageLoad);
function logout(){
    if (confirm("¿Deseas cerrar sesión?")) {
        /* Tanca sessió i neteja cookies */
        localStorage.clear();
        fetch('/user/logout', { method: 'GET' })
          .then(() => {
            console.log('Cookie borrada');
            location.href="/";
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    }
}

function profile(){
    location.href="/main/" + localStorage.getItem("userid");
}

function login(){
    location.href="/login";
}

function adjustDropdownItems() {
        if (localStorage.getItem("userid")) {
            document.getElementById("profile").style.display = "block";
            document.getElementById("logout").style.display = "block";
            document.getElementById("login").style.display = "none";
        } else {
            document.getElementById("profile").style.display = "none";
            document.getElementById("logout").style.display = "none";
            document.getElementById("login").style.display = "block";
        }
    }
adjustDropdownItems();