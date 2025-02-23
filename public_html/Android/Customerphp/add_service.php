<?php
$servername = "localhost"; 
$username = "root"; 
$password = ""; 
$dbname = "repair"; 
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$input = file_get_contents("php://input");
$data = json_decode($input, true);

if (isset($data['date'], $data['description'], $data['carID'], $data['price'], $data['estimated_time'], $data['service_name'])) {
    $date = $data['date'];
    $description = $data['description'];
    $carID = $data['carID'];
    $price = $data['price'];
    $estimated_time = $data['estimated_time'];
    $service_name = $data['service_name']; 

    // service_id
    $sql_service_id = "SELECT service_id FROM service WHERE service_name = ?";
    $stmt_service_id = $conn->prepare($sql_service_id);
    $stmt_service_id->bind_param("s", $service_name);
    $stmt_service_id->execute();
    $result_service_id = $stmt_service_id->get_result()->fetch_assoc()['service_id'];
    
    // employee_id
    $sql_employee_id = "SELECT employeeid FROM service WHERE service_name = ?";
    $stmt_employee_id = $conn->prepare($sql_employee_id);
    $stmt_employee_id->bind_param("s", $service_name);
    $stmt_employee_id->execute();
    $result_employee_id = $stmt_employee_id->get_result()->fetch_assoc()['employeeid'];

    // customer_id
    $sql_customer_id = "SELECT customer_id FROM cars WHERE car_id = ?";
    $stmt_customer_id = $conn->prepare($sql_customer_id);
    $stmt_customer_id->bind_param("i", $carID);
    $stmt_customer_id->execute();
    $result_customer_id = $stmt_customer_id->get_result()->fetch_assoc()['customer_id'];

    $sql = "INSERT INTO orders (order_date, description, car_id, customer_id, service_id, state, employeeid, states_date, total_amount) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    $stmt = $conn->prepare($sql);
    $state = "Yet";
    $states_date = "0000-00-00";

    $stmt->bind_param(
        "ssiiissss",
        $date,
        $description,
        $carID,
        $result_customer_id,
        $result_service_id,
        $state,
        $result_employee_id,
        $states_date,
        $price
    );

    if ($stmt->execute()) {
        $order_id = $conn->insert_id;
        echo json_encode(["result" => "success", "order_id" => $order_id]);
    } else {
        echo json_encode(["result" => "error", "message" => $stmt->error]);
    }

    $stmt->close();
} else {
    echo json_encode(["result" => "error", "message" => "Missing parameters"]);
}

$conn->close();
?>
