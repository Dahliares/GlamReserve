function myData (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-myData')
}

function myReserves (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-myReserves')
}

function mySchedule (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-mySchedule')
}

function myServices (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-myServices')
}
function addMyServices (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-add-myServices')
}
function mySubscription (ref) {
    const localThis = ref
    localThis.blur()
    mostrarDiv('area-mySubscription')
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

var inputs = document.querySelectorAll('.validar-input');
inputs.forEach(function(input) {
  input.addEventListener('blur', function() {
    if (input.value !== '' && !input.checkValidity()) {
      input.style.borderColor = 'orange';
      input.style.borderWidth = '2px';
    } else {
      input.style.borderColor = '';
      input.style.borderWidth = '';
    }
  });
});

function toggleClosed(checkbox, dayIteration, isClosedDay) {
    const closed = checkbox.checked;
    const inputSchedules = document.getElementById('inputSchedules-' + dayIteration);

    let timesToToggle;
    if (isClosedDay) {
        timesToToggle = inputSchedules.querySelectorAll(`input[type="time"]`);
    } else {
        timesToToggle = inputSchedules.querySelectorAll(`input[data-afternoon="true"]`);
    }

    timesToToggle.forEach(input => {
        input.disabled = closed;
        input.required = !closed;
        if (closed) {
            input.value = '';
        }
    });
}

function initializeCheckboxes() {
    const schedules = document.querySelectorAll('[id^="inputSchedules-"]');
    schedules.forEach((schedule, index) => {
        const dayIteration = index + 1;
        const timeInputs = schedule.querySelectorAll(`input[type="time"]`);
        const closedDayCheckbox = document.querySelector(`#closed-day${dayIteration}`);
        const closedAfternoonCheckbox = document.querySelector(`#closed-afternoon-day${dayIteration}`);

        let openTime2 = null;
        let closeTime2 = null;
        let allEmpty = true;

        timeInputs.forEach(input => {
            if (input.value === '') {
                if (input.getAttribute('data-afternoon') === "true") {
                    openTime2 = input;
                    closeTime2 = input;
                }
            } else {
                allEmpty = false;
            }
        });

        if (allEmpty) {
            closedDayCheckbox.checked = true;
        } else if (openTime2 !== null && closeTime2 !== null && openTime2.value === '' && closeTime2.value === '') {
            closedAfternoonCheckbox.checked = true;
        }

        if (closedDayCheckbox.checked) {
            toggleClosed(closedDayCheckbox, dayIteration, true);
        }
        if (closedAfternoonCheckbox.checked) {
            toggleClosed(closedAfternoonCheckbox, dayIteration, false);
        }
    });
}


document.addEventListener("DOMContentLoaded", initializeCheckboxes);


  function saveSchedule() {
     const fetchPromises = [];
    for (let dayIteration = 1; dayIteration <= 7; dayIteration++) {

      const openTime1Input = document.querySelector(`#inputSchedules-${dayIteration} input[name='openTime1-day${dayIteration}']`);
      const closeTime1Input = document.querySelector(`#inputSchedules-${dayIteration} input[name='closeTime1-day${dayIteration}']`);
      const openTime2Input = document.querySelector(`#inputSchedules-${dayIteration} input[name='openTime2-day${dayIteration}']`);
      const closeTime2Input = document.querySelector(`#inputSchedules-${dayIteration} input[name='closeTime2-day${dayIteration}']`);

      const openTime1 = openTime1Input.value ? openTime1Input.value + ':00' : null;
        const closeTime1 = closeTime1Input.value ? closeTime1Input.value + ':00' : null;
        const openTime2 = openTime2Input.value ? openTime2Input.value + ':00' : null;
        const closeTime2 = closeTime2Input.value ? closeTime2Input.value + ':00' : null;

      const scheduleId = openTime1Input.dataset.scheduleId;
      const companyId = openTime1Input.dataset.companyId;

      const requestBody = {
        scheduleId: scheduleId,
        dayOfWeek: dayIteration,
        company:{
        id: companyId
        },
        openTime1: openTime1,
        closeTime1: closeTime1,
        openTime2: openTime2,
        closeTime2: closeTime2
      };

      const requestOptions = {
        method: 'PUT',
        headers: {
        'Authorization': localStorage.getItem('authToken'),
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      };

      const fetchPromise = fetch(`/schedule/${scheduleId}`, requestOptions)
            .then(response => {
              if (!response.ok) {
                throw new Error('Failed to save schedule for day ' + dayIteration);
              }
              return response;
            });

      fetchPromises.push(fetchPromise);
    }
    Promise.all(fetchPromises)
        .then(() => {
          alert('Horarios actualizados correctamente');
          window.location.href = '/main';
        })
        .catch(error => console.error('Error:', error));

  }

    let input = document.getElementById("file-input");
    input.addEventListener("change", fileRead, false);
    let base64String;
    function fileRead(event) {
        const file = event.target.files[0];
        const fileReader = new FileReader();

        fileReader.onload = function(event) {
            base64String  = event.target.result
        }

        fileReader.readAsDataURL(file);

    }

  function saveCompany() {
    const companyId = document.getElementById('companyId').value;
    console.log(companyId);
    const name = document.getElementById('name').value;
    const category = document.getElementById('category').value;
    const address = document.getElementById('address').value;
    const description = document.getElementById('description').value;
    const owner = localStorage.getItem('userid');
    const data = {
      id: companyId,
      name: name,
      category: category,
      address: address,
      description: description,
      image: base64String,
      owner: {
        id: owner
      }
    };

    fetch('/company/edit', {
      method: 'PUT',
      headers: {
           'Authorization': localStorage.getItem('authToken'),
          'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => {
          if (response.ok) {
              alert('Datos actualizados correctamente');
              window.location.href = '/main';
          } else {
              alert('Error al actualizar los datos, por favor intente nuevamente.');
          }
      })
    .then(data => {
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al actualizar los datos, por favor intente nuevamente.');
    });
  }

  document.addEventListener('DOMContentLoaded', () => {
    const fetchReserves = (dateStr) => {
        const companyIdElem = document.getElementById('companyId');
        const companyId= companyIdElem ? companyIdElem.value : '';

        if (companyId && dateStr) {
            const url = `/reserve/companyDate/${companyId}/${dateStr}`;

            fetch(url)
                .then((response) => response.json())
                .then((reserves) => {
                    const tbody = document.getElementById('reserves-tbody');
                    tbody.innerHTML = '';
                    reserves.sort((a, b) => new Date(a.date) - new Date(b.date));

                    reserves.forEach((reserve) => {
                        const row = document.createElement('tr');
                        const dateObj = new Date(reserve.date);

                        row.innerHTML = `
                            <td>${dateObj.getHours()}:${dateObj.getMinutes().toString().padStart(2, '0')}</td>
                            <td>${reserve.service.name}</td>
                            <td>${reserve.user ? reserve.user.name + ' ' + reserve.user.lastname :reserve.userName}</td>
                            <td>${reserve.user ? reserve.user.phone : reserve.userPhone}</td>
                            <td>
                                <button type="button" class="btn btn-danger btn-sm" onclick="deleteReserve(${reserve.id})">
                                    <i class="fas fa-times"></i>
                                </button>
                            </td>
                        `;
                        row.setAttribute('data-reserve-id', reserve.id);
                        tbody.appendChild(row);
                    });
                })
                .catch((error) => {
                    console.log('Error fetching reserves:', error);
                });
        }
    };

    const submitReserve = async (data) => {
        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": localStorage.getItem('authToken'),
            },
            body: JSON.stringify(data),
        };

        try {
          const response = await fetch("/reserve", requestOptions);
          if (response.ok) {
            alert("La reserva se ha creado correctamente");
            fetchReserves(document.getElementById('selected-date').innerText);
          } else {
            alert("Error al crear la reserva");
          }
        } catch (error) {
          console.error("Error:", error);
          alert("Error al crear la reserva");
        }
    };

    const calendar = flatpickr('#calendar', {
        locale: "es",
        inline: true,
        defaultDate: "today",
        onReady: function (dateObj, dateStr, instance) {
            document.getElementById('selected-date').innerText = dateStr;
            fetchReserves(document.getElementById('selected-date').innerText);
        },
        onChange: (selectedDates, dateStr) => {
            document.getElementById('selected-date').innerText = dateStr;
            fetchReserves(document.getElementById('selected-date').innerText);
        }
    });

    const externalDateTime = flatpickr('#external-date-time', {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        locale: "es",
        inline: true,
    });

    const form = document.getElementById('add-external-reserve-form');
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const companyId = document.getElementById('companyId').value;
        const selectedDate = document.getElementById('external-date-time').value;
        const dateObj = new Date(selectedDate);
        const newDate = dateObj.toISOString().replace(/\.\d{3}Z$/, '');

        const data = {
            company:{
             id: companyId,
             owner:{
                id: localStorage.getItem("userid")
             }
            },
            date: newDate,
            userPhone: form['external-phone'].value,
            userName: form['external-name'].value,
            service:{
            id: form['external-service'].value
            }
        };
        submitReserve(data);
    });
});

function deleteReserve(reserveId) {
    if (confirm("¿Estás seguro de que deseas eliminar la reserva?")) {
    const url = `/reserve/${reserveId}`;

    fetch(url, {
        'method': 'DELETE',
        headers: {
                'Authorization': localStorage.getItem('authToken')
            }
    })
        .then((response) => {
            if (response.ok) {
                const reserveRow = document.querySelector(`[data-reserve-id="${reserveId}"]`);
                if (reserveRow) {
                    reserveRow.remove();
                }
            } else {
                console.log('Error deleting reserve:', response.status);
                alert('Error al eliminar la reserva');
            }
        })
        .catch((error) => {
            console.log('Error deleting reserve:', error);
            alert('Error al eliminar la reserva');
        });
        }
}


function saveService(serviceId, counter) {

    for (let i = 0; i < counterServices; i++) {
        if (counter == i) {
            const nombre = document.querySelector(`input[name="nombre-${i}"]`).value;
            const precio = document.querySelector(`input[name="precio-${i}"]`).value;
            const duracion = document.querySelector(`input[name="duracion-${i}"]`).value;
            const descripcion = document.querySelector(`textarea[name="descripcion-${i}"]`).value;
            const companyId = document.getElementById('companyId').value;

            const data = {
                id: serviceId,
                name: nombre,
                price: precio,
                duration: duracion,
                description: descripcion,
                company:{
                id: companyId
                }
            };

            const requestOptions = {
                method: 'PUT',
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': localStorage.getItem('authToken'),
                },
                body: JSON.stringify(data)
            };

            fetch(`/service/${serviceId}`, requestOptions)
            .then(response => {
              if (response.ok) {
                  alert('Servicio actualizado correctamente');
                  window.location.href = '/main';
              } else {
                  alert('Error al actualizar el servicio, por favor intente nuevamente.');
              }
            })
            .then(data => {
            })
            .catch(error => {
            console.error('Error:', error);
            alert('Error al actualizar el servicio, por favor intente nuevamente.');
            });
        }
    }
}

function deleteService(serviceId, counter) {

    if (confirm("¿Estás seguro de que deseas eliminar el servicio?")) {
        for (let i = 0; i < counterServices; i++) {
            if (counter == i) {

                const data = {
                    id: serviceId
                };

                const requestOptions = {
                    method: 'DELETE',
                    headers: {
                      'Content-Type': 'application/json',
                      'Authorization': localStorage.getItem('authToken'),
                    },
                    body: JSON.stringify(data)
                };

                fetch(`/service/${serviceId}`, requestOptions)
                .then(response => {
                  if (response.ok) {
                      alert('Servicio eliminado correctamente');
                      window.location.href = '/main';
                  } else {
                      alert('Error al eliminar el servicio, por favor intente nuevamente.');
                  }
                })
                .then(data => {
                })
                .catch(error => {
                console.error('Error:', error);
                alert('Error al elminar el servicio, por favor intente nuevamente.');
                });
            }
        }
    }
}

function newService() {

    const nombre = document.querySelector(`input[name="new-name"]`).value;
    const precio = document.querySelector(`input[name="new-price"]`).value;
    const duracion = document.querySelector(`input[name="new-duration"]`).value;
    const descripcion = document.querySelector(`textarea[name="new-description"]`).value;
    const companyId = document.getElementById('companyId').value;

    const data = {
        name: nombre,
        price: precio,
        duration: duracion,
        description: descripcion,
        company:{
            id: companyId
        }
    };

    const requestOptions = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('authToken'),
        },
        body: JSON.stringify(data)
    };

    fetch(`/service`, requestOptions)
    .then(response => {
      if (response.ok) {
          alert('Nuevo servicio guardado correctamente');
          window.location.href = '/main';
      } else {
          alert('Error al crear nuevo servicio, por favor intente nuevamente.');
      }
    })
    .then(data => {
    })
    .catch(error => {
    console.error('Error:', error);
    alert('Error al crear nuevo servicio, por favor intente nuevamente.');
    });
}

function deleteUser() {
        const deleteConfirmation = confirm('Seguro que quieres eliminar tu usuario y todos sus datos asociados? También se eliminará tu empresa.');
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