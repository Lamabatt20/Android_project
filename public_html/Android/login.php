<?php
// Database connection
$conn = new mysqli("localhost:3306", "root", "", "repair");

// Check if the connection was successful
if ($conn->connect_error) {
    echo json_encode(['status' => 'failure', 'message' => 'Connection failed']);
    exit;
}

// Get the username and password from the request (POST method expected)
$data = json_decode(file_get_contents("php://input"), true);

// Check if username and password are provided
if (isset($data['username']) && isset($data['password'])) {
    $username = $data['username'];
    $password = $data['password'];

    // Query to check user details
    $sql = "SELECT u.user_id, u.role, u.password, c.customer_id 
            FROM user u 
            LEFT JOIN customers c ON u.user_id = c.userid
            WHERE u.username = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($dbUserId, $dbRole, $dbPassword, $dbCustomerId);
        $stmt->fetch();

        if ($password === $dbPassword) {
            $response = [
                'status' => 'success',
                'role' => $dbRole,
            ];

            // Add role-specific data
            if ($dbRole === 'Customer') {
                $response['customer_id'] = $dbCustomerId;
                $response['user_id'] = $dbUserId; 
            } elseif ($dbRole === 'Employee') {
                $response['employee_id'] = $dbUserId; // Add additional employee-specific data if needed
                $response['user_id'] = $dbUserId; 
            } elseif ($dbRole === 'Admin') {
                $response['admin_id'] = $dbUserId; // Admin-specific data
                $response['user_id'] = $dbUserId; 
            }

            echo json_encode($response);
        } else {
            echo json_encode(['status' => 'failure', 'message' => 'Invalid credentials']);
        }
    } else {
        echo json_encode(['status' => 'failure', 'message' => 'Invalid username']);
    }

    $stmt->close();
} else {
    echo json_encode(['status' => 'failure', 'message' => 'Username or password is missing']);
}

$conn->close();
?>