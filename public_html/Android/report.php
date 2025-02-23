<?php
// Database connection settings
$host = 'localhost';
$user = 'root';
$password = ''; // Use your database password
$database = 'repair';
$port = 3306;

// Connect to the database
$conn = new mysqli($host, $user, $password, $database, $port);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Query to get the number of customers
$customers_query = "SELECT COUNT(*) as total_customers FROM customers";
$customers_result = $conn->query($customers_query);
$total_customers = $customers_result->fetch_assoc()['total_customers'];

// Query to get the number of employees
$employees_query = "SELECT COUNT(*) as total_employees FROM employees";
$employees_result = $conn->query($employees_query);
$total_employees = $employees_result->fetch_assoc()['total_employees'];

// Query to calculate the total money from orders, based on service prices
$orders_query = "
    SELECT SUM(s.price) as total_revenue 
    FROM orders o
    JOIN service s ON o.service_id = s.service_id
";
$orders_result = $conn->query($orders_query);
$total_revenue = $orders_result->fetch_assoc()['total_revenue'];

// Compute profit
$assets_percentage = 30; 
$profit = $total_revenue * ($assets_percentage / 100);

// Query to find the employee with the most orders
$top_employee_query = "
    SELECT e.employee_name, u.photo, COUNT(o.order_id) as total_orders 
    FROM employees e
    JOIN orders o ON e.employee_id = o.employeeid
    JOIN user u ON e.userid = u.user_id
    GROUP BY e.employee_id
    ORDER BY total_orders DESC
    LIMIT 1
";
$top_employee_result = $conn->query($top_employee_query);
$top_employee = $top_employee_result->fetch_assoc();

// Query to find the customer with the most orders
$top_customer_query = "
    SELECT c.customer_name, u.photo, COUNT(o.order_id) as total_orders 
    FROM customers c
    JOIN orders o ON c.customer_id = o.customer_id
    JOIN user u ON c.userid = u.user_id
    GROUP BY c.customer_id
    ORDER BY total_orders DESC
    LIMIT 1
";
$top_customer_result = $conn->query($top_customer_query);
$top_customer = $top_customer_result->fetch_assoc();

// Output the results as JSON
header('Content-Type: application/json');

$response = [
    'total_customers' => $total_customers,
    'total_employees' => $total_employees,
    'total_revenue' => $total_revenue,
    'profit' => $profit,
    'top_employee' => [
        'employee_name' => $top_employee['employee_name'],
        'photo' => $top_employee['photo'],
        'total_orders' => $top_employee['total_orders']
    ],
    'top_customer' => [
        'customer_name' => $top_customer['customer_name'],
        'photo' => $top_customer['photo'],
        'total_orders' => $top_customer['total_orders']
    ]
];

echo json_encode($response);

// Close the database connection
$conn->close();
?>
