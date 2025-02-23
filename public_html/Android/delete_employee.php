<?php
// Database connection variables
$host = 'localhost';
$dbname = 'repair';
$username = 'root';
$password = '';

// Create a new PDO instance
try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    // Set PDO error mode to exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Could not connect to the database: " . $e->getMessage());
}

// Check if the 'id' parameter is provided in the URL (GET request)
if (isset($_GET['id'])) {
    $employeeId = $_GET['id']; // Employee ID to be deleted

    // Prepare the DELETE SQL statement
    $sql = "DELETE FROM employees WHERE employee_id = :id";  // Updated column name

    $stmt = $pdo->prepare($sql);

    // Bind the employee ID to the SQL statement
    $stmt->bindParam(':id', $employeeId, PDO::PARAM_INT);

    try {
        // Execute the query
        $stmt->execute();

        // Check if any rows were affected (i.e., employee was deleted)
        if ($stmt->rowCount() > 0) {
            echo json_encode(["success" => true, "message" => "Employee deleted successfully."]);
        } else {
            echo json_encode(["success" => false, "message" => "Employee not found."]);
        }
    } catch (PDOException $e) {
        // Handle any errors during query execution
        echo json_encode(["success" => false, "message" => "Error deleting employee: " . $e->getMessage()]);
    }
} else {
    // If 'id' parameter is not set in the URL
    echo json_encode(["success" => false, "message" => "No employee ID provided."]);
}
?>
