<?php

$servername = "localhost";
$username = "root"; 
$password = ""; 
$dbname = "repair"; 


$conn = new mysqli($servername, $username, $password, $dbname);


if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}


$user_id = isset($_GET['user_id']) ? $_GET['user_id'] : null;


if ($user_id === null) {
    echo json_encode(array("error" => "User ID is required"));
    exit();
}


$sql = "
    SELECT 
        user.username, 
        user.email, 
        user.photo, 
        user.role,
     
        CASE
            WHEN user.role = 'Employee' THEN employees.phone_number
            WHEN user.role = 'Customer' THEN customers.phone_number
            WHEN user.role = 'Admin' THEN employees.phone_number
            ELSE NULL
        END AS phone_number,
   
        CASE
            WHEN user.role = 'Admin' THEN employees.authentication_code_id
            ELSE NULL
        END AS authentication_code_id
    FROM user
    LEFT JOIN employees ON user.user_id = employees.userid
    LEFT JOIN customers ON user.user_id = customers.userid
    WHERE user.user_id = ?";


$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $user_id); 


$stmt->execute();


$result = $stmt->get_result();


if ($result->num_rows > 0) {
    $user_data = $result->fetch_assoc();
    
    
    $response = array(
        "username" => $user_data['username'],
        "email" => $user_data['email'],
        "phone_number" => $user_data['phone_number'],
        "photo" => $user_data['photo'],
        "role" => $user_data['role'],
        "authentication_code_id" => $user_data['authentication_code_id'] ? $user_data['authentication_code_id'] : null
    );

    
    echo json_encode($response);
} else {
    echo json_encode(array("error" => "User not found"));
}


$stmt->close();
$conn->close();
?>