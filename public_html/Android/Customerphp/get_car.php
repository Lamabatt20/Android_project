<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "repair";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

if (isset($_GET['car_number'])) {
    $car_number = $_GET['car_number'];
} else {
    die(json_encode(["error" => "Car number not provided"]));
}


$stmt_car = $conn->prepare("SELECT * FROM cars WHERE car_id = ?");
$stmt_car->bind_param("i", $car_number);
$stmt_car->execute();
$result_car = $stmt_car->get_result();

if ($result_car->num_rows > 0) {
    $car_data = $result_car->fetch_assoc();

    $stmt_history = $conn->prepare("SELECT notes, date FROM history WHERE car_id = ?");
    $stmt_history->bind_param("i", $car_number);
    $stmt_history->execute();
    $result_history = $stmt_history->get_result();

    $history_data = array();
    while ($row = $result_history->fetch_assoc()) {
        $history_data[] = $row;
    }

   
    $stmt_service_details = $conn->prepare(
        "SELECT service.service_name, service.estimated_time, orders.order_id 
         FROM orders 
         INNER JOIN service ON orders.service_id = service.service_id 
         WHERE orders.car_id = ? AND orders.state = ?"
    );
    $state = "Yet";
    $stmt_service_details->bind_param("is", $car_number, $state);
    $stmt_service_details->execute();
    $result_services = $stmt_service_details->get_result();

    $services = [];
    while ($row = $result_services->fetch_assoc()) {
        $services[] = [
            'order_id' => $row['order_id'],
            'service_name' => $row['service_name'],
            'estimated_time' => $row['estimated_time']
        ];
    }


    $car_data['history'] = $history_data;
    $car_data['services'] = $services;


    echo json_encode($car_data);
} else {
    echo json_encode(["error" => "Car not found"]);
}

$conn->close();
?>
