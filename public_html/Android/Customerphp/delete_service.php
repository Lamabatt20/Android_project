<?php
$servername = "localhost";
$username = "root";       
$password = "";            
$dbname = "repair"; 

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]));
}

$input = file_get_contents("php://input");
$data = json_decode($input, true);

if (isset($data['order_id'])) {
    $order_id = $conn->real_escape_string($data['order_id']);
    
    $sql = "DELETE FROM orders WHERE order_id = $order_id";
    if ($conn->query($sql) === TRUE) {
        echo json_encode(["success" => true, "message" => "Service deleted successfully"]);
    } else {
        echo json_encode(["success" => false, "message" => "Error deleting service: " . $conn->error]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Invalid request"]);
}

$conn->close();
?>
