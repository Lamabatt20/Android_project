<?php
// Database connection parameters
$host = "localhost";
$port = "3306";  
$dbname = "repair";
$username = "root";
$password = "";

// Try to establish a PDO connection
try {
    // Create PDO instance and set error mode to exception
    $pdo = new PDO("mysql:host=$host;port=$port;dbname=$dbname", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Query to get order data with customer details and including 'order_id', 'order_date', 'states_date', 'total_amount', 'service_id', 'state'
    $sql = "SELECT o.order_id, c.customer_id, c.customer_name, s.service_name, 
            o.order_date, o.states_date, o.total_amount, o.service_id, o.state
            FROM customers c
            INNER JOIN orders o ON c.customer_id = o.customer_id
            INNER JOIN service s ON o.service_id = s.service_id";
    
    // Prepare and execute the query
    $stmt = $pdo->prepare($sql);
    $stmt->execute();
    
    // Fetch results as an associative array
    $orders = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    // Return the results as JSON
    echo json_encode($orders);
} catch (PDOException $e) {
    // In case of an error, display the error message
    echo "Error: " . $e->getMessage();
}
?>