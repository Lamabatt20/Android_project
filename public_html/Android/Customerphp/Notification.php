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
    die(json_encode(array("message" => "Connection failed: " . $conn->connect_error)));
}


if (!isset($_GET['customer_id']) || !is_numeric($_GET['customer_id'])) {
    echo json_encode(array("message" => "Invalid customer ID"));
    exit;
}

$customer_id = $_GET['customer_id']; 


$sql = "
    SELECT o.order_id, o.customer_id, o.service_id, o.car_id, o.order_date, o.state, 
           s.service_name, c.cars_name 
    FROM orders o
    JOIN service s ON o.service_id = s.service_id
    JOIN cars c ON o.car_id = c.car_id
    WHERE o.customer_id = ?
    ORDER BY o.order_date DESC
";


$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $customer_id); 


$stmt->execute();
$result = $stmt->get_result();


if ($result->num_rows > 0) {
    $orders = array();
    while($row = $result->fetch_assoc()) {
        
        $orderDate = date("Y-m-d", strtotime($row['order_date']));

        
        $orders[] = array(
            'order_id' => $row['order_id'],
            'customer_id' => $row['customer_id'],
            'service_id' => $row['service_id'],
            'car_id' => $row['car_id'],
            'order_date' => $orderDate,
            'state' => $row['state'],
            'service_name' => $row['service_name'],
            'car_name' => $row['cars_name']
        );
    }
    echo json_encode($orders); 
} else {
    echo json_encode(array("message" => "No orders found"));
}


$stmt->close();
$conn->close();
?>