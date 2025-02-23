<?php
// Database connection
$host = "localhost";
$user = "root";
$password = "";
$dbname = "repair";

$conn = new mysqli($host, $user, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["error" => "Connection failed: " . $conn->connect_error]));
}

// Retrieve input data
$input = json_decode(file_get_contents('php://input'), true);
if (!$input) {
    die(json_encode(["error" => "Invalid JSON input"]));
}

// Validate input data
$user_id = $input['user_id'] ?? null;
$username = $input['username'] ?? null;
$email = $input['email'] ?? null;
$phone_number = $input['phone_number'] ?? null;
$role = $input['role'] ?? null;
$authentication_code = $input['authentication_code_id'] ?? null;
$photo = $input['photo'] ?? null; 

// Check for missing required fields
$missing_fields = [];
if (!$user_id) $missing_fields[] = "user_id";
if (!$username) $missing_fields[] = "username";
if (!$email) $missing_fields[] = "email";

if (!empty($missing_fields)) {
    die(json_encode([
        "error" => "Missing required user data",
        "missing_fields" => $missing_fields
    ]));
}

// Update user table
$updateUserSql = !empty($photo)
    ? "UPDATE user SET username=?, email=?, photo=? WHERE user_id=?"
    : "UPDATE user SET username=?, email=? WHERE user_id=?";
$updateUserStmt = $conn->prepare($updateUserSql);

if (!empty($photo)) {
    $updateUserStmt->bind_param("sssi", $username, $email, $photo, $user_id);
} else {
    $updateUserStmt->bind_param("ssi", $username, $email, $user_id);
}

if (!$updateUserStmt->execute()) {
    die(json_encode(["error" => "Failed to update user details"]));
}
$updateUserStmt->close();

// Role-specific updates
if ($role == 'Admin') {
    if (!$phone_number || !$authentication_code) {
        die(json_encode(["error" => "Missing phone number or authentication code for admin"]));
    }

    // Update phone number and authentication code for Admin
    $sql = "UPDATE employees SET phone_number=?, authentication_code_id=? WHERE userid=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("ssi", $phone_number, $authentication_code, $user_id);

    if ($stmt->execute()) {
        // Update authentication_code_id for all employees
        $updateAllSql = "UPDATE employees SET authentication_code_id=?";
        $updateAllStmt = $conn->prepare($updateAllSql);
        $updateAllStmt->bind_param("s", $authentication_code);

        if ($updateAllStmt->execute()) {
            echo json_encode(["success" => "Admin profile and authentication code updated for all employees"]);
        } else {
            echo json_encode(["error" => "Failed to update authentication code for all employees"]);
        }

        $updateAllStmt->close();
    } else {
        echo json_encode(["error" => "Failed to update admin details"]);
    }
    $stmt->close();
} elseif ($role == 'Employee') {
    if (!$phone_number) die(json_encode(["error" => "Missing phone number for employee"]));

    $sql = "UPDATE employees SET phone_number=? WHERE userid=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si", $phone_number, $user_id);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Employee profile updated successfully"]);
    } else {
        echo json_encode(["error" => "Failed to update employee details"]);
    }
    $stmt->close();
}  elseif ($role == 'Customer') {
    if (!$phone_number) die(json_encode(["error" => "Missing phone number for customer"]));

    $sql = "UPDATE Customers SET phone_number=? WHERE userid=?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("si", $phone_number, $user_id);

    if ($stmt->execute()) {
        echo json_encode(["success" => "Customer profile updated successfully"]);
    } else {
        echo json_encode(["error" => "Failed to update customer details"]);
    }
    $stmt->close();
} else {
    die(json_encode(["error" => "Invalid role"]));
}

$conn->close();
?>
