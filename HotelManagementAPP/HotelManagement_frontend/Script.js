/* -------Index.js Start ----------*/



/* -------Index.js End ------------*/




/* -------Login.js Start ----------*/


/* -------Login.js End ------------*/





/* -------Register.js Start ----------*/

document.querySelector("form").addEventListener("submit", function(e){
    e.preventDefault();

    alert("Registered Successfully 🚀");
});

/* -------Register.js End ------------*/


/* -------Rooms.js Start ----------*/

document.querySelectorAll("button").forEach(btn => {
    btn.addEventListener("click", () => {
        alert("Room Booked ");
    });
});

/* -------Rooms.js End ------------*/


/* -------Booking.js Start ------------*/

document.querySelector("form").addEventListener("submit", function(e){
    e.preventDefault();

    alert("Booking Confirmed ");
});

/* -------Booking.js Start ----------*/



/* ------- My-Booking.js Start ------------*/


document.querySelectorAll(".cancel-btn").forEach(btn => {
    btn.addEventListener("click", () => {
        alert("Booking Cancelled ");
    });
});

/* ------- My-Booking.js End ------------*/