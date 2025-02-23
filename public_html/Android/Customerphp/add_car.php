<?php
$host = "localhost";    
$user = "root";              
$password = "";              
$dbname = "repair";    


$customerId = $_GET['customer_id'] ?? null;
$licensePlate = $_GET['licensePlate'] ?? '';
$model = $_GET['model'] ?? '';
$make = $_GET['make'] ?? '';
$year = $_GET['year'] ?? '';
$odometer = $_GET['odometer'] ?? '';
$engineSpecification = $_GET['engineSpecification'] ?? '';
$transmission = $_GET['transmission'] ?? '';
$company = $_GET['company'] ?? '';
$carsName = $_GET['cars_name'] ?? '';
$photoPath = $_GET['photo'] ?? ''; 


$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$sql = $conn->prepare("INSERT INTO cars (customer_id, license_plate, model, make, year, odometer, engine_specification, transmission, company, cars_name, photo) 
VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
$sql->bind_param("issssssssss", $customerId, $licensePlate, $model, $make, $year, $odometer, $engineSpecification, $transmission, $company, $carsName, $photoPath);


if ($sql->execute()) {
    echo "Car added successfully";
} else {
    echo "Error: " . $conn->error;
}

$sql->close();
$conn->close();
?>
