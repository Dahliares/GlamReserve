function myData (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-myData')
}

function myReserves (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-reserves-content')
}

function mostrarDiv(divAMostrar) {
    const divs = document.querySelectorAll('div[id^="area"]');
    for (let i = 0; i < divs.length; i++) {
        if (divs[i].id === divAMostrar) {
          divs[i].style.display = 'block';
        } else {
          divs[i].style.display = 'none';
        }
    }
}


function deleteReserve(reserveId) {
        const deleteConfirmation = confirm('Seguro que quieres eliminar la reserva?');
        if (deleteConfirmation) {
            const url = `/reserve/${reserveId}`;
            fetch(url, {
                method: 'DELETE',
                headers: {
                    "Authorization": localStorage.getItem('authToken')
                  },
            })
            .then(response => {
                if (response.ok) {
                    location.reload();
                } else {
                    alert('Error al eliminar la reserva');
                }
            })
        }
    }


function deleteUser() {
        const deleteConfirmation = confirm('Seguro que quieres eliminar tu usuario y todos sus datos asociados?');
        if (deleteConfirmation) {
            const user = localStorage.getItem("userid");
            const url = `/user/` + user;
            fetch(url, {
                method: 'DELETE',
                headers: {
                    "Authorization": localStorage.getItem('authToken')
                  },
            })
            .then(response => {
                if (response.ok) {
                    localStorage.clear();
                    window.location.href = '/';
                } else {
                    alert('Error al eliminar el usuario');
                }
            })
        }
    }