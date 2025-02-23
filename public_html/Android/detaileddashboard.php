<?php
$host = "localhost";
$port = "3306";  
$dbname = "repair";
$username = "root";
$password = "";

try {
    $pdo = new PDO("mysql:host=$host;port=$port;dbname=$dbname", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $customer_id = isset($_GET['customer_id']) ? $_GET['customer_id'] : null;
    $order_id = isset($_GET['order_id']) ? $_GET['order_id'] : null;

    if ($customer_id && !$order_id) {
        
        $sql = "SELECT o.order_id, o.customer_id, o.order_date, o.states_date, o.car_id, o.employeeid, 
                o.total_amount, o.service_id, o.state, c.customer_name, c.phone_number
                FROM orders o
                JOIN customers c ON o.customer_id = c.customer_id
                WHERE o.customer_id = :customer_id";
        
        $stmt = $pdo->prepare($sql);
        $stmt->bindValue(':customer_id', $customer_id, PDO::PARAM_INT);
    } elseif ($order_id) {
       
        $sql = "SELECT o.order_id, o.customer_id, o.order_date, o.states_date, o.car_id, o.employeeid, 
                o.total_amount, o.service_id, o.state, c.customer_name, c.phone_number
                FROM orders o
                JOIN customers c ON o.customer_id = c.customer_id
                WHERE o.order_id = :order_id";
        
        $stmt = $pdo->prepare($sql);
        $stmt->bindValue(':order_id', $order_id, PDO::PARAM_INT);
    
    } else {
       
        $sql = "SELECT o.order_id, o.customer_id, o.order_date, o.states_date, o.car_id, o.employeeid, 
                o.total_amount, o.service_id, o.state, c.customer_name, c.phone_number
                FROM orders o
                JOIN customers c ON o.customer_id = c.customer_id";
        
        $stmt = $pdo->prepare($sql);
    }

    $stmt->execute();
    
    $orders = $stmt->fetchAll(PDO::FETCH_ASSOC);
    
    echo json_encode($orders);

} catch (PDOException $e) {
    echo "Error: " . $e->getMessage();
}

?>