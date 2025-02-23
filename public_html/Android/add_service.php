<?php
// Database connection settings
$servername = "localhost";
$port = "3306";
$username = "root"; 
$password = "";   
$dbname = "repair";

try {
    // Create PDO connection
    $conn = new PDO("mysql:host=$servername;port=$port;dbname=$dbname", $username, $password);
    // Set PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    // Handle connection errors
    die("Connection failed: " . $e->getMessage());
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Retrieve data from POST request
    $service_name = $_POST['service_name'];
    $price = $_POST['price'];
    $estimated_time = $_POST['estimated_time'];
    $employeeid = empty($_POST['employeeid']) ? nill : $_POST['employeeid']; 

    try {
        // Prepare SQL statement to prevent SQL injection
        $stmt = $conn->prepare("INSERT INTO service (service_name, price, estimated_time, employeeid) VALUES (?, ?, ?, ?)");
        $stmt->bindParam(1, $service_name);
        $stmt->bindParam(2, $price);
        $stmt->bindParam(3, $estimated_time);
        $stmt->bindParam(4, $employeeid);

        // Execute the statement
        $stmt->execute();

        echo "New service added successfully.";
    } catch (PDOException $e) {
        // Handle SQL errors
        echo "Error: " . $e->getMessage();
    }
}

// PDO connection is automatically closed when the script ends or when the $conn variable is destroyed
?>
