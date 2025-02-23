-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3307
-- Generation Time: Dec 21, 2024 at 06:39 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `repair`
--

-- --------------------------------------------------------

--
-- Table structure for table `cars`
--

CREATE TABLE `cars` (
  `car_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `license_plate` varchar(15) NOT NULL,
  `model` varchar(15) NOT NULL,
  `make` varchar(15) NOT NULL,
  `year` int(11) NOT NULL,
  `odometer` varchar(15) NOT NULL,
  `engine_specification` varchar(15) NOT NULL,
  `transmission` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cars`
--

INSERT INTO `cars` (`car_id`, `customer_id`, `license_plate`, `model`, `make`, `year`, `odometer`, `engine_specification`, `transmission`) VALUES
(11, 1, 'ABC123', 'Model1', 'Make1', 2020, '15000', 'V6', 'Automatic'),
(12, 2, 'DEF456', 'Model2', 'Make2', 2021, '20000', 'V8', 'Manual'),
(13, 3, 'GHI789', 'Model3', 'Make3', 2022, '25000', 'V4', 'Automatic'),
(14, 4, 'JKL012', 'Model4', 'Make4', 2023, '30000', 'V6', 'Manual'),
(15, 5, 'MNO345', 'Model5', 'Make5', 2024, '35000', 'V8', 'Automatic'),
(16, 6, 'PQR678', 'Model6', 'Make6', 2025, '40000', 'V4', 'Manual'),
(17, 7, 'STU901', 'Model7', 'Make7', 2026, '45000', 'V6', 'Automatic'),
(18, 1, 'VWX234', 'Model8', 'Make8', 2027, '50000', 'V8', 'Manual'),
(19, 4, 'YZA567', 'Model9', 'Make9', 2028, '55000', 'V4', 'Automatic'),
(20, 5, 'BCD890', 'Model10', 'Make10', 2029, '60000', 'V6', 'Manual');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `customer_name` char(50) NOT NULL,
  `phone_number` char(50) NOT NULL,
  `userid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `customer_name`, `phone_number`, `userid`) VALUES
(1, 'Rami', '1234567890', 2),
(2, 'Ali', '2345678901', 5),
(3, 'Fadi', '3456789012', 3),
(4, 'Aya', '4567890123', 7),
(5, 'Hanaa', '5678901234', 8),
(6, 'Doua', '6789012345', 9),
(7, 'Nelly', '7890123456', 4);

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `employee_id` int(11) NOT NULL,
  `employee_name` char(50) NOT NULL,
  `phone_number` char(50) NOT NULL,
  `authentication_code_id` int(11) NOT NULL,
  `userid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`employee_id`, `employee_name`, `phone_number`, `authentication_code_id`, `userid`) VALUES
(1, 'Leen Odeh', '5551234567', 1001, 1),
(2, 'Mannar', '5552345678', 1002, 6),
(3, 'Lama', '5553456789', 1003, 10),
(4, 'Loor', '5554567890', 1004, 11);

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `history_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `message_text` text NOT NULL,
  `notification_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `states_date` date NOT NULL,
  `car_id` int(11) NOT NULL,
  `employeeid` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `service_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `services`
--

CREATE TABLE `services` (
  `service_id` int(11) NOT NULL,
  `service_name` char(50) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `description` char(50) NOT NULL,
  `estimated_time` char(50) NOT NULL,
  `employeeid` int(11) DEFAULT NULL,
  `carid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `services`
--

INSERT INTO `services` (`service_id`, `service_name`, `price`, `description`, `estimated_time`, `employeeid`, `carid`) VALUES
(20, 'Oil Change', 50.00, 'Quick Service', '30 mins', 1, 11),
(21, 'Tire Replacement', 100.00, 'All Tires', '1 hour', 2, 12),
(22, 'Brake Check', 75.00, 'Front Brakes', '45 mins', 3, 13),
(23, 'Battery Replacement', 120.00, 'New Battery', '1 hour', 4, 14),
(24, 'AC Repair', 150.00, 'Fix Cooling', '2 hours', 3, 15),
(25, 'Engine Repair', 500.00, 'Full Service', '5 hours', 4, 16),
(26, 'Transmission Repair', 600.00, 'Fix Issues', '6 hours', 1, 17),
(27, 'Alignment', 80.00, 'Wheel Alignment', '1 hour', 3, 18),
(28, 'Detailing', 200.00, 'Interior and Exterior', '3 hours', 2, 19),
(29, 'Inspection', 40.00, 'Basic Check', '30 mins', 1, 20);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `userName` char(50) NOT NULL,
  `email` char(50) NOT NULL,
  `password` char(50) NOT NULL,
  `role` char(50) NOT NULL,
  `photo` varchar(255) NOT NULL DEFAULT 'public_html/nadroidphotos/Capture.JPG'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `userName`, `email`, `password`, `role`, `photo`) VALUES
(1, 'Leen Odeh', 'example@example.com', 'password123', 'Employee', '/public_html/nadroidphotos/me.jpg'),
(2, 'Rami', 'rami@example.com', 'password1', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(3, 'Fadi', 'fadi@example.com', 'password2', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(4, 'Nelly', 'nelly@example.com', 'password3', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(5, 'Ali', 'ali@example.com', 'password4', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(6, 'Mannar', 'mannar@example.com', 'password5', 'Employee', '/public_html/nadroidphotos/mannar.JPG'),
(7, 'Aya', 'aya@example.com', 'password6', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(8, 'Hanaa', 'hanna@example.com', 'password7', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(9, 'Doua', 'doua@example.com', 'password8', 'Customer', '/public_html/nadroidphotos/Capture.JPG'),
(10, 'Lama', 'lama@example.com', 'password10', 'Employee', '/public_html/nadroidphotos/lama.JPG'),
(11, 'Loor', 'loor@example.com', 'loor123', 'Employee', '/public_html/nadroidphotos/loor.JPG');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cars`
--
ALTER TABLE `cars`
  ADD PRIMARY KEY (`car_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`employee_id`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`history_id`),
  ADD KEY `employee_id` (`employee_id`),
  ADD KEY `service_id` (`service_id`),
  ADD KEY `car_id` (`car_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `car_id` (`car_id`),
  ADD KEY `employeeid` (`employeeid`),
  ADD KEY `service_id` (`service_id`);

--
-- Indexes for table `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`service_id`),
  ADD KEY `employeeid` (`employeeid`),
  ADD KEY `carid` (`carid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cars`
--
ALTER TABLE `cars`
  MODIFY `car_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
  MODIFY `employee_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `history_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `services`
--
ALTER TABLE `services`
  MODIFY `service_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cars`
--
ALTER TABLE `cars`
  ADD CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `customers`
--
ALTER TABLE `customers`
  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  ADD CONSTRAINT `history_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
  ADD CONSTRAINT `history_ibfk_3` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`),
  ADD CONSTRAINT `history_ibfk_4` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`car_id`),
  ADD CONSTRAINT `orders_ibfk_3` FOREIGN KEY (`employeeid`) REFERENCES `employees` (`employee_id`),
  ADD CONSTRAINT `orders_ibfk_4` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`);

--
-- Constraints for table `services`
--
ALTER TABLE `services`
  ADD CONSTRAINT `services_ibfk_1` FOREIGN KEY (`employeeid`) REFERENCES `employees` (`employee_id`),
  ADD CONSTRAINT `services_ibfk_2` FOREIGN KEY (`carid`) REFERENCES `cars` (`car_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
