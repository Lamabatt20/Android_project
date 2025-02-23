<?php
header('Content-Type: application/json');

$host = 'localhost'; 
$db_name = 'repair'; 
$username = 'root'; 
$password = ''; 


$conn = new mysqli($host, $username, $password, $db_name);

if ($conn->connect_error) {
    die(json_encode(['error' => 'Database connection failed: ' . $conn->connect_error]));
}

$sql = "SELECT service_id, service_name, price, estimated_time FROM service";
$result = $conn->query($sql);

$service = [];

if ($result->num_rows > 0) {
    // Fetch all service
    while ($row = $result->fetch_assoc()) {
        $service[] = [
            'service_id' => $row['service_id'],
            'service_name' => $row['service_name'],
            'price' => $row['price'],
            'estimated_time' => $row['estimated_time']
        ];
    }
} else {
    echo json_encode([]);
}

$conn->close();

echo json_encode($service);
?>