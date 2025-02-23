<?php
// Database connection
$servername = "localhost";
$port = "3306";
$username = "root";
$password = "";
$dbname = "repair";

try {
    $conn = new PDO("mysql:host=$servername;port=$port;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Get the authorization code from the request
    $authorization = $_POST['authorization'];

    // Check if the code exists in the employees table
    $stmt = $conn->prepare("SELECT COUNT(*) FROM employees WHERE authentication_code_id = :authorization");
    $stmt->bindParam(':authorization', $authorization);
    $stmt->execute();

    // Return true if the code is valid, otherwise false
    echo $stmt->fetchColumn() > 0 ? "true" : "false";
} catch (Exception $e) {
    echo "Error: " . $e->getMessage();
}
?>
