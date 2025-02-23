<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
header('Access-Control-Allow-Headers: Content-Type');

$host = "localhost";    
$user = "root";              
$password = "";              
$dbname = "repair";          

$conn = new mysqli($host, $user, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

if (isset($_GET['customer_id']) && !empty($_GET['customer_id'])) {
    $customer_id = intval($_GET['customer_id']);  

    $stmt = $conn->prepare("SELECT * FROM cars WHERE customer_id = ?");
    $stmt->bind_param("i", $customer_id);
    $stmt->execute();
    $result = $stmt->get_result();

    $cars = array();

    if ($result->num_rows > 0) {
        while ($row = $result->fetch_assoc()) {
            $cars[] = $row;
        }
        echo json_encode($cars);  
    } else {
        echo json_encode(["message" => "No cars found for this customer"]);
    }

    $stmt->close();
} else {
    die(json_encode(["error" => "customer_id parameter is required"]));
}

$conn->close();
?>