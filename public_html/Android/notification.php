<?php
// Database connection
$servername = "localhost:3306";
$username = "root";
$password = "";
$dbname = "repair";

$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Fetch customer orders
$sql = "SELECT orders.order_id, orders.order_date, orders.description, orders.state, cars.car_id, cars.cars_name,cars.model
        FROM orders 
        INNER JOIN cars ON orders.car_id = cars.car_id

        WHERE orders.state = 'Yet'"; // Only fetch pending orders
$result = $conn->query($sql);

$notifications = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $notifications[] = $row;
    }
}

$conn->close();

// Return data as JSON for use in the Android app
header('Content-Type: application/json');
echo json_encode($notifications);
?>
