-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 12, 2022 at 06:38 PM
-- Server version: 5.7.24
-- PHP Version: 7.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `car_rental`
--

-- --------------------------------------------------------

--
-- Table structure for table `car`
--

CREATE TABLE `car` (
  `id` int(5) NOT NULL,
  `brand` varchar(15) NOT NULL,
  `model` varchar(15) NOT NULL,
  `production_year` year(4) NOT NULL,
  `fee` int(5) NOT NULL,
  `shop_id` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `car`
--

INSERT INTO `car` (`id`, `brand`, `model`, `production_year`, `fee`, `shop_id`) VALUES
(1, 'Wolksvagen', 'Passat', 2020, 300, 1),
(2, 'Wolksvagen', 'Golf', 2019, 200, 1),
(3, 'Audi', 'A3', 2020, 300, 1),
(4, 'Audi', 'A4', 2021, 400, 1),
(5, 'Tesla', 'Model S', 2018, 500, 1),
(6, 'Tesla', 'Model 3', 2020, 250, 1),
(7, 'Tesla', 'Model X', 2019, 400, 1),
(8, 'Tesla', 'Model Y', 2020, 350, 1),
(9, 'BMW', 'X3', 2021, 300, 2),
(10, 'BMW', 'X5', 2019, 400, 2),
(11, 'BMW', 'X6', 2020, 500, 2),
(12, 'Peugeot', '3008', 2020, 500, 2),
(13, 'Peugeot', '2008', 2018, 350, 2),
(14, 'Volvo', 'XC60', 2019, 250, 2),
(15, 'Volvo', 'XC40', 2019, 400, 2),
(16, 'Volvo', 'S90', 2019, 500, 2),
(17, 'Honda', 'Civic', 2019, 350, 3),
(18, 'Honda', 'Jazz', 2018, 150, 3),
(19, 'Scoda', 'Octavia', 2018, 300, 3),
(20, 'Scoda', 'Superb', 2019, 350, 3),
(21, 'Citroën', 'C3', 2020, 250, 3),
(22, 'Citroën', 'C5', 2021, 400, 3),
(23, 'Volvo', 'S90', 2021, 600, 3);

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

CREATE TABLE `client` (
  `personal_number` int(10) NOT NULL,
  `firstname` varchar(15) NOT NULL,
  `lastname` varchar(15) NOT NULL,
  `address` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `client`
--

INSERT INTO `client` (`personal_number`, `firstname`, `lastname`, `address`) VALUES
(1111, 'asfg', 'asdf', 'sdff'),
(1312, 'qew', 'wqe', 'eqewe'),
(2222, 'dsfsd', 'sdfsd', 'fasfa'),
(123456, 'Lucas', 'Nilson', 'Stallvägen'),
(124531, 'qweqwr', 'fasdf', 'fasdfa'),
(246843068, 'Pauline', 'Lindgren', 'Hogvaktsterrassen 32'),
(742837211, 'Lovisa', 'Nordlund', 'PG 12'),
(969696969, 'Maël', 'Romanin Bluteau', 'Stallvägen 24'),
(1213634587, 'Tobias', 'Andersson', 'Stallvagen 18'),
(1631732182, 'Johan', 'Henriksson', 'Skeppsbron 33A'),
(1782921445, 'Sonja', 'Sundberg,', 'Framtidsvagen 4A'),
(2084290432, 'Lars', 'Gustafsson', 'Myntgatan 11');

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE `employee` (
  `id` int(4) NOT NULL,
  `firstname` varchar(15) NOT NULL,
  `lastname` varchar(15) NOT NULL,
  `title` varchar(15) NOT NULL,
  `shop_id` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`id`, `firstname`, `lastname`, `title`, `shop_id`) VALUES
(1, 'Simon', 'Kreuzberger', 'salesman', 1),
(2, 'Philip Juan', 'Grolleman', 'salesman', 1),
(3, 'Alina', 'Steindl', 'manager', 1),
(4, 'Aydın', 'Fırıldak', 'salesman', 2),
(5, 'Ege', 'Kayıhan', 'salesman', 2),
(6, 'Emre', 'Astarcıoğlu', 'salesman', 2),
(7, 'Daniel', 'Zeiler', 'manager', 2),
(8, 'Sara', 'Bak', 'salesman', 3),
(9, 'Bilge', 'Öztürk', 'salesman', 3),
(10, 'Lucie', 'Bauer', 'salesman', 3),
(11, 'Adam', 'Anderson', 'manager', 3);

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `id` int(5) NOT NULL,
  `starting_date` date NOT NULL,
  `ending_date` date NOT NULL,
  `cost` int(6) NOT NULL,
  `cl_personal_number` int(10) NOT NULL,
  `emp_id` int(4) NOT NULL,
  `car_id` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`id`, `starting_date`, `ending_date`, `cost`, `cl_personal_number`, `emp_id`, `car_id`) VALUES
(2, '2022-01-02', '2022-01-06', 1000, 246843068, 6, 6),
(5, '2022-02-10', '2022-02-15', 400, 1631732182, 5, 3),
(7, '2021-10-29', '2021-11-10', 400, 1631732182, 6, 7),
(8, '2022-03-15', '2022-04-15', 400, 1782921445, 6, 15),
(9, '2022-01-01', '2022-03-20', 500, 1213634587, 10, 11),
(11, '2022-01-01', '2022-01-15', 7500, 2084290432, 10, 5),
(12, '2022-02-01', '2022-03-01', 11600, 123456, 2, 10),
(13, '2022-01-01', '2022-01-15', 6000, 1111, 6, 15),
(14, '2021-12-20', '2022-01-20', 11200, 2222, 10, 20);

-- --------------------------------------------------------

--
-- Stand-in structure for view `renting_history`
-- (See below for the actual view)
--
CREATE TABLE `renting_history` (
`firstname` varchar(15)
,`lastname` varchar(15)
,`title` varchar(15)
,`brand` varchar(15)
,`model` varchar(15)
,`starting_date` date
,`ending_date` date
,`cost` int(6)
,`shop_id` int(3)
,`invoice_id` int(5)
);

-- --------------------------------------------------------

--
-- Table structure for table `shop`
--

CREATE TABLE `shop` (
  `id` int(3) NOT NULL,
  `city` varchar(15) NOT NULL,
  `address` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `shop`
--

INSERT INTO `shop` (`id`, `city`, `address`) VALUES
(1, 'Växjö', 'Lyan 62'),
(2, 'Kalmar', 'Kungsgatan 31'),
(3, 'Alvesta', 'Järnvägsgatan 3');

-- --------------------------------------------------------

--
-- Structure for view `renting_history`
--
DROP TABLE IF EXISTS `renting_history`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `renting_history`  AS SELECT `employee`.`firstname` AS `firstname`, `employee`.`lastname` AS `lastname`, `employee`.`title` AS `title`, `car`.`brand` AS `brand`, `car`.`model` AS `model`, `invoice`.`starting_date` AS `starting_date`, `invoice`.`ending_date` AS `ending_date`, `invoice`.`cost` AS `cost`, `employee`.`shop_id` AS `shop_id`, `invoice`.`id` AS `invoice_id` FROM ((`employee` left join `invoice` on((`employee`.`id` = `invoice`.`emp_id`))) left join `car` on((`invoice`.`car_id` = `car`.`id`))) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `car`
--
ALTER TABLE `car`
  ADD PRIMARY KEY (`id`),
  ADD KEY `shop_id` (`shop_id`) USING BTREE;

--
-- Indexes for table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`personal_number`);

--
-- Indexes for table `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`),
  ADD KEY `shop_id` (`shop_id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cl_personal_number` (`cl_personal_number`) USING BTREE,
  ADD KEY `car_id` (`car_id`) USING BTREE,
  ADD KEY `emp_id` (`emp_id`) USING BTREE;

--
-- Indexes for table `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `car`
--
ALTER TABLE `car`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `shop`
--
ALTER TABLE `shop`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `car`
--
ALTER TABLE `car`
  ADD CONSTRAINT `car_ibfk_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`cl_personal_number`) REFERENCES `client` (`personal_number`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `invoice_ibfk_3` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
