<?php
// Enable error reporting for debugging
ini_set('display_errors', 1);
error_reporting(E_ALL);

// Database connection details
$servername = "localhost";
$port = "3306"; 
$username = "root"; 
$password = ""; 
$dbname = "repair";

try {
    // Create a new PDO connection
    $conn = new PDO("mysql:host=$servername;port=$port;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    echo "Connected successfully<br>"; // Debugging connection

    // Check if the request is POST
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        // Retrieve form data
        $name = $_POST['name'] ?? '';
        $email = $_POST['email'] ?? '';
        $phone = $_POST['phone'] ?? '';
        $authorization = $_POST['authorization'] ?? null; // Optional for non-employees
        $role = $_POST['role'] ?? '';
        $username = $_POST['username'] ?? '';
        $identity = $_POST['identity'] ?? '';
        $password = $_POST['password'] ?? '';


        // Debug: Print retrieved form data
        echo "Name: $name, Email: $email, Phone: $phone, Role: $role, Username: $username, Identity: $identity<br>";

        // Validate required fields
        if (empty($name) || empty($email) || empty($phone) || empty($role) || empty($username) || empty($identity) || empty($password)) {
            throw new Exception("All fields are required.");
        }

        // Check if the email already exists
        $stmt = $conn->prepare("SELECT COUNT(*) FROM user WHERE email = :email");
        $stmt->bindParam(':email', $email);
        $stmt->execute();
        if ($stmt->fetchColumn() > 0) {
            throw new Exception("Email is already registered.");
        }

        // Validate authorization code if role is Employee
        if ($role === "Employee" && !empty($authorization)) {
            $stmt = $conn->prepare("SELECT COUNT(*) FROM employees WHERE authentication_code_id = :authorization");
            $stmt->bindParam(':authorization', $authorization);
            $stmt->execute();
            if ($stmt->fetchColumn() == 0) {
                throw new Exception("Invalid authorization code.");
            }
        }

        // Insert into user table
        $stmt = $conn->prepare("
            INSERT INTO user (username, email, password, role)
            VALUES (:username, :email, :password, :role)
        ");
        $stmt->bindParam(':username', $username);
        $stmt->bindParam(':email', $email);
        $stmt->bindParam(':password', $password);
        $stmt->bindParam(':role', $role);
        
        // Debug: Print the SQL query for the user table insert
        echo "Executing query for user table: " . $stmt->queryString . "<br>";

        if (!$stmt->execute()) {
            throw new Exception("Error inserting into user table.");
        }

        $userId = $conn->lastInsertId(); // Get the inserted user's ID
        echo "Inserted User ID: $userId<br>"; // Debugging

        // Insert into customers or employees table based on role
        if ($role === "Customer") {
            $stmt = $conn->prepare("
                INSERT INTO customers (customer_name, phone_number, userid, identitynum)
                VALUES (:name, :phone, :userid, :identity)
            ");
        } else {
            $stmt = $conn->prepare("
                INSERT INTO employees (employee_name, phone_number, userid, identitynum, authentication_code_id)
                VALUES (:name, :phone, :userid, :identity, :authorization)
            ");
            $stmt->bindParam(':authorization', $authorization);
        }

        // Bind parameters
        $stmt->bindParam(':name', $name);
        $stmt->bindParam(':phone', $phone);
        $stmt->bindParam(':userid', $userId);
        $stmt->bindParam(':identity', $identity);
        
        // Debug: Print the SQL query for customer or employee insert
        echo "Executing query for " . ($role === "Customer" ? "customer" : "employee") . " table: " . $stmt->queryString . "<br>";

        // Execute the statement for inserting customer/employee
        if (!$stmt->execute()) {
            throw new Exception("Error inserting into " . ($role === "Customer" ? "customer" : "employee") . " table.");
        }

        // Success response
        echo json_encode(["success" => true, "message" => "Registration successful!"]);
    } else {
        throw new Exception("Invalid request method.");
    }
} catch (Exception $e) {
    // Error response
    echo json_encode(["success" => false, "message" => $e->getMessage()]);
}
