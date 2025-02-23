<?php

// Database connection parameters
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "repair";

// Create a connection to the database
$conn = new mysqli($servername, $username, $password, $dbname);

// Check for connection errors
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to fetch all employees
$sql = "SELECT 
            employees.employee_id, 
            employees.employee_name, 
            employees.phone_number, 
            employees.authentication_code_id, 
            user.email, 
            user.photo 
        FROM employees
        LEFT JOIN user ON employees.userid = user.user_id";

// Execute the query
$result = $conn->query($sql);

// Check if there are any results
if ($result->num_rows > 0) {
    $employees = array();

    // Fetch each row and add it to the array
    while ($row = $result->fetch_assoc()) {
        $employees[] = array(
            "employee_id" => $row['employee_id'],
            "employee_name" => $row['employee_name'],
            "phone_number" => $row['phone_number'],
            "authentication_code_id" => $row['authentication_code_id'],
            "email" => $row['email'],
            "photo" => $row['photo']
        );
    }

    // Return the data in JSON format
    echo json_encode($employees);
} else {
    // Return an empty array if no employees found
    echo json_encode(array());
}

// Close the connection
$conn->close();
?>
