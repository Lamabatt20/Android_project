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

// Get the raw POST data (JSON)
$input = file_get_contents('php://input');

// Decode the JSON to an associative array
$data = json_decode($input, true);

// Check if order_id and user_id are provided in the request
if (isset($data['order_id']) && isset($data['user_id'])) {
    $order_id = $data['order_id'];
    $user_id = $data['user_id'];

    // Fetch the corresponding employee_id using the user_id
    $employeeQuery = "SELECT employee_id FROM employees WHERE userid = ?";
    $stmt = $conn->prepare($employeeQuery);
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows == 0) {
        echo "Error: User ID does not correspond to an employee!";
        exit();
    }

    $employee = $result->fetch_assoc();
    $employee_id = $employee['employee_id'];

    // Begin transaction
    $conn->begin_transaction();

    try {
        // Fetch order details (including customer_id) to insert into the history table
        $orderQuery = "SELECT service_id, car_id, order_date, description, customer_id FROM orders WHERE order_id = ?";
        $stmt = $conn->prepare($orderQuery);
        $stmt->bind_param("i", $order_id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $order = $result->fetch_assoc();

            $service_id = $order['service_id'];
            $car_id = $order['car_id'];
            $date = $order['order_date'];
            $notes = $order['description'];
            $customer_id = $order['customer_id'];

            // Insert into the history table
            $historyQuery = "INSERT INTO history (employee_id, service_id, car_id, date, notes, customer_id) VALUES (?, ?, ?, ?, ?, ?)";
            $stmt = $conn->prepare($historyQuery);
            $stmt->bind_param("iiisss", $employee_id, $service_id, $car_id, $date, $notes, $customer_id);
            $stmt->execute();

            // Update the order state
            $updateQuery = "UPDATE orders SET state = 'Done' WHERE order_id = ?";
            $stmt = $conn->prepare($updateQuery);
            $stmt->bind_param("i", $order_id);
            $stmt->execute();

            // Commit transaction
            $conn->commit();
            echo "success";
        } else {
            throw new Exception("Order not found!");
        }
    } catch (Exception $e) {
        $conn->rollback(); // Rollback transaction on failure
        echo "Error: " . $e->getMessage();
    }
} else {
    echo "Invalid request! order_id or user_id not provided.";
}

$conn->close();
?>
