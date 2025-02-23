<?php
$servername = "localhost"; 
$username = "root"; 
$password = ""; 
$dbname = "repair"; 

$conn = new mysqli($servername, $username, $password, $dbname);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if (isset($_GET['service_name'])) {
    $service_name = $_GET['service_name'];

    $sql = "SELECT estimated_time, price FROM service WHERE service_name = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $service_name);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();
        echo json_encode($row);
    } else {
        echo json_encode(["error" => "Service not found"]);
    }
    $stmt->close();
} else {
    echo json_encode(["error" => "No service name provided"]);
}

$conn->close();
?>
