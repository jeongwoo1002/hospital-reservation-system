// document.addEventListener("DOMContentLoaded", function() {
//     const timeSlots = document.getElementById("timeSlots");
//     const reservationForm = document.querySelector("form");
//     const selectedTimeInput = document.getElementById("selectedTime");
//
//     // Sample time slots (for demonstration purposes)
//     const availableTimes = ["09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
//         "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
//         "16:00", "16:30", "17:00", "17:30", "18:00", "18:30"];
//
//     // Populate time slots
//     availableTimes.forEach(time => {
//         const row = document.createElement("tr");
//         const timeCell = document.createElement("td");
//         const statusCell = document.createElement("td");
//
//         timeCell.textContent = time;
//         // Check if time slot is available (you can implement your logic here)
//         const isAvailable = Math.random() < 0.5; // For demonstration purposes, randomly mark as available or not
//         statusCell.textContent = isAvailable ? "예약 가능" : "예약 불가능";
//         statusCell.className = isAvailable ? "available" : "unavailable";
//
//         row.appendChild(timeCell);
//         row.appendChild(statusCell);
//         timeSlots.appendChild(row);
//
//         // Add click event listener to each time slot
//         row.addEventListener("click", function() {
//             const selectedTime = timeCell.textContent;
//             selectedTimeInput.value = selectedTime;
//             // Change status to indicate selection
//             const previouslySelected = timeSlots.querySelector(".selected");
//             if (previouslySelected) {
//                 const prevStatusCell = previouslySelected.querySelector("td:nth-child(2)");
//                 prevStatusCell.textContent = prevStatusCell.classList.contains("available") ? "예약 가능" : "예약 불가능";
//                 previouslySelected.classList.remove("selected");
//             }
//             row.classList.add("selected");
//             statusCell.textContent = "선택됨";
//         });
//     });
//
//     // Form submission
//     reservationForm.addEventListener("submit", function(event) {
//         event.preventDefault();
//         const selectedTime = selectedTimeInput.value;
//         if (selectedTime) {
//             alert(selectedTime + "에 예약이 완료되었습니다.");
//             // Here you can send the reservation data to the server for processing
//         } else {
//             alert("시간을 선택해주세요.");
//         }
//     });
// });
