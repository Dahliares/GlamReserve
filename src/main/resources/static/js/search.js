var latitude;
var longitude;
var map;
window.onload= function () {

if ("geolocation" in navigator) {
    } else {
        alert("Tu navegador no soporta la geolocalización");
    }

    function getLocation() {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(handleSuccess, handleError);
        } else {
            alert("Tu navegador no soporta la geolocalización");
        }
    }

    function handleSuccess(position) {
        latitude = position.coords.latitude;
        longitude = position.coords.longitude;
        map = L.map('map').setView([latitude, longitude], 13);
        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);

        addMarkers();
    }

    function handleError(error) {
        console.error("Error obteniendo localización: ", error);
        map = L.map('map').setView([41.39292, 2.16834], 10);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        }).addTo(map);

        addMarkers();
    }

    getLocation();

   function addMarkers(){

        var ids = document.getElementsByClassName('ids');
        var nombres = document.getElementsByClassName('name');
        var direcciones = document.getElementsByClassName('adr');
        var lats = document.getElementsByClassName('lat');
        var longs = document.getElementsByClassName('lon');
        var lat = 0;
        var lon = 0;
        var marker

        for (let i = 0; i < lats.length; i++){
            lat = lats[i].textContent;
           lon = longs[i].textContent;

            marker = L.marker([lat, lon]).addTo(map);
            marker.bindPopup('<b>' + nombres[i].textContent +'</b><br>'+ direcciones[i].textContent + '<br><a href="/viewCompany/' + ids[i].textContent +'">Ver Local</a>').openPopup();


        }

    }


}

document.getElementById('search-form').addEventListener('submit', function (event) {
    let latInput = document.getElementById('latitude-input');
    let lonInput = document.getElementById('longitude-input');

    if (typeof latitude === 'undefined' || typeof longitude === 'undefined') {
        latInput.value = null;
        lonInput.value = null;
    } else {
        latInput.value = latitude;
        lonInput.value = longitude;
    }


});