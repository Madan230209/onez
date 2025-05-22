-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2025 at 07:02 AM
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
-- Database: `onez`
--

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `address_id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`address_id`, `name`) VALUES
(1, 'Kathmandu'),
(2, 'Lalitpur'),
(3, 'Bhaktapur');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `total_items` int(11) NOT NULL,
  `total_price` bigint(11) NOT NULL,
  `createdAt` date NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`cart_id`, `total_items`, `total_price`, `createdAt`, `user_id`) VALUES
(5, 0, 0, '2025-05-22', 2);

-- --------------------------------------------------------

--
-- Table structure for table `cartitem`
--

CREATE TABLE `cartitem` (
  `cartItem_id` int(11) NOT NULL,
  `productQuantity` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_order` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `price_at_order`) VALUES
(23, 35, 65, 1, 5500.00);

-- --------------------------------------------------------

--
-- Table structure for table `order_table`
--

CREATE TABLE `order_table` (
  `order_id` int(11) NOT NULL,
  `order_date` date NOT NULL,
  `order_status` varchar(50) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `paymentMethod` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_table`
--

INSERT INTO `order_table` (`order_id`, `order_date`, `order_status`, `cart_id`, `user_id`, `paymentMethod`) VALUES
(35, '2025-05-22', 'Processing', 5, 2, 'IME Pay');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL,
  `productName` varchar(150) NOT NULL,
  `description` varchar(2555) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `category` varchar(50) NOT NULL,
  `productImage` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`product_id`, `productName`, `description`, `price`, `quantity`, `category`, `productImage`) VALUES
(15, 'ZOWIE XL2540K', 'The ZOWIE XL2540K is a 24.5\" esports gaming monitor built for pure speed. With a blazing 240Hz refresh rate, ultra-low input lag, and pro-level features like Black eQualizer and Color Vibrance, it delivers smooth, responsive gameplay that keeps you ahead in every match.', 50000, 75, 'Monitor', '01-xl2540k-xl-.png'),
(16, 'Acer Nitro 27-inch', 'With refresh rates up to 180Hz and response times as low as 1ms, you’ll enjoy fluid motion and razor-sharp accuracy, giving you a competitive edge in every match. AMD FreeSync support ensures tear-free gaming, while customizable display modes optimize visuals for every scenario—from gaming to streaming and design work.', 33000, 62, 'Monitor', 'acer Nitro 27 inch.png'),
(17, 'ASUS XG27AQDMG', 'The ASUS ROG Strix OLED XG27AQDMG is a premium 26.5-inch gaming monitor that combines cutting-edge OLED technology with a sleek design, tailored for enthusiasts seeking top-tier performance and visual fidelity', 65000, 70, 'Monitor', 'ASUS XG27AQDMG.png'),
(18, 'BenQ EX240N', 'The BenQ MOBIUZ EX240N is a 23.8-inch Full HD gaming monitor designed to deliver smooth and immersive gameplay experiences. With its high refresh rate and rapid response time, it\'s tailored for gamers seeking performance without breaking the bank.', 40000, 65, 'Monitor', 'BenQ EX240N.png'),
(19, 'BenQ PD2725U', 'The BenQ PD2725U is a 27-inch 4K UHD monitor designed for creative professionals who demand exceptional color accuracy and versatile connectivity. With its factory-calibrated IPS panel, Thunderbolt 3 support, and HDR capabilities, it stands out as a top choice for designers, photographers, and video editors.', 40000, 55, 'Monitor', 'BenQ PD2725U.png'),
(20, 'LG 27GS93QE', 'The LG 27GS93QE UltraGear OLED Gaming Monitor is a 26.5-inch QHD display engineered for gamers seeking top-tier performance and visual fidelity. With its OLED panel, ultra-fast refresh rate, and rapid response time, it delivers an immersive and responsive gaming experience.', 60000, 55, 'Monitor', 'LG 27GS93QE.png'),
(21, 'Samsung 27 T35F', 'The Samsung 27\" T35F Series is a budget-friendly 27-inch Full HD (1920x1080) monitor designed for everyday computing, casual gaming, and multimedia consumption. Featuring an IPS panel, it offers vibrant colors and wide viewing angles, making it suitable for various tasks.\r\n\r\n', 55000, 59, 'Monitor', 'Samsung 27 T35F.png'),
(22, 'SAMSUNG G93SC', 'The Samsung Odyssey OLED G9 (G93SC) is a premium 49-inch super ultrawide gaming monitor designed to deliver an unparalleled immersive experience. With its QD-OLED panel, ultra-fast refresh rate, and expansive screen real estate, it\'s tailored for gamers and professionals seeking top-tier performance and visual fidelity.', 100000, 65, 'Monitor', 'SAMSUNG G93SC.png'),
(23, 'ASUS Spatha X', 'The ASUS ROG Spatha X is a premium wireless gaming mouse designed for gamers seeking high performance and customization. With its dual-mode connectivity, high-precision sensor, and ergonomic design, it\'s tailored for both MMO and FPS enthusiasts.\r\n\r\n', 4000, 65, 'Mouse', 'ASUS ROG Spatha X.png'),
(24, 'Razer DeathAdder', 'The Razer DeathAdder V3 Pro is a high-performance wireless gaming mouse designed for competitive esports players and enthusiasts seeking precision, comfort, and speed. Building upon the legacy of the iconic DeathAdder series, the V3 Pro introduces significant upgrades to meet the demands of modern gaming.', 4000, 55, 'Mouse', 'DeathAdder V3 Pro.png'),
(25, 'Logitech G Pro X Superlight', 'The Logitech G Pro X Superlight is a top-tier wireless gaming mouse, renowned for its ultra-lightweight design and exceptional performance, making it a favorite among esports professionals and competitive gamers.', 13000, 55, 'Mouse', 'Logitech g pro x.png'),
(26, 'Logitech G502 HERO', 'The Logitech G502 HERO is a highly regarded wired gaming mouse, celebrated for its precision, customization options, and ergonomic design. It\'s a popular choice among gamers seeking a versatile and reliable mouse for various gaming genres.', 50000, 55, 'Mouse', 'Logitech G502 HERO.png'),
(27, 'Pulsar X2H', 'The Pulsar Gaming Gears X2H Wireless Gaming Mouse is a high-performance esports mouse designed for competitive gamers, particularly those who favor a claw or palm-claw grip. Building upon the success of the original X2H, this version introduces several enhancements to elevate the gaming experience.', 10000, 55, 'Mouse', 'Pulsar Gaming Gears X2H Wireless Gaming Mouse.png'),
(28, 'Razer Viper', 'The Razer Viper V3 Pro is a premier wireless esports gaming mouse, engineered for competitive play and trusted by professional gamers. Weighing just 54g (55g for the white edition), it offers a lightweight design that enhances agility and precision.', 15000, 70, 'Mouse', 'razer viper v3 pro.png'),
(29, 'Samsung 9100 PRO', 'The Samsung 9100 PRO 4TB NVMe M.2 SSD offers ultra-fast PCIe Gen 5 speeds up to 14,800 MB/s.\r\nIt delivers massive storage and reliable performance for gaming and professional tasks.\r\nPerfect for heavy workloads like video editing and data transfer.', 80000, 55, 'Storage', '9100 PRO 4 TB SSD NVMe™ M.2.png'),
(30, 'Crucial P310', 'The Crucial P310 500GB PCIe Gen4 NVMe M.2 SSD is a budget-friendly storage solution offering impressive performance.', 3000, 70, 'Storage', 'Crucial P310 500GB 2280 PCIe Gen4 3D NAND NVMe M.2 SSD.jpg'),
(31, 'MSI SPATIUM M371', 'The MSI SPATIUM M371 1TB NVMe M.2 SSD is a budget-friendly storage solution designed for mainstream users seeking reliable performance without breaking the bank.', 4000, 70, 'Storage', 'MSI SPATIUM M371 NVMe M.2 1TB - PCIe 3x4 NVMe M.2 Internal Solid State Drive.jpg'),
(32, 'Seagate One Touch ', 'The Seagate One Touch 2TB External Hard Drive is a compact and stylish portable storage solution designed for users seeking reliable data backup and easy file management.', 3000, 55, 'Storage', 'Seagate One Touch 2Tb External HDD.jpg'),
(33, 'WD My Passport Ultra', 'The WD My Passport Ultra 6TB Portable Hard Drive offers a substantial storage capacity in a sleek and durable design. Its USB-C™ compatibility, combined with robust security features and backup software, makes it a reliable choice for users seeking to store and protect large amounts of data.', 3000, 60, 'Storage', 'Western Digital My Passport Ultra 6TB.jpg'),
(34, 'WD BLACK SN850X', 'The WD_BLACK SN850X 1TB NVMe SSD stands out as a top-tier storage solution for gamers and professionals seeking high-speed performance and reliability. Its impressive read/write speeds, gaming-centric features, and optional thermal management make it a compelling choice for enhancing system responsiveness and reducing load times.', 7000, 65, 'Storage', 'Western Digital WD Black SN850X NVMe 1TB.jpg'),
(35, 'Ajazz AK820', 'The Ajazz AK820 is a 75% compact mechanical keyboard that combines functionality with a sleek design, making it an excellent choice for both gamers and typists.', 3000, 55, 'Keyboard', 'ajazzx ak820.jpg'),
(36, 'ASUS ROG Falchion', 'The ASUS ROG Falchion Wireless 65% Mechanical Gaming Keyboard is a compact, feature-rich keyboard designed for gamers who value performance and portability. Its 65% layout retains essential keys, including arrow and navigation keys, within a minimal footprint, making it ideal for streamlined setups.', 8000, 70, 'Keyboard', 'Rog.png'),
(37, 'AULA F75 Glacier', 'The AULA F75 Glacier is a 75% wireless mechanical keyboard that combines a compact layout with versatile connectivity options and a premium typing experience.', 5000, 65, 'Keyboard', 'Aula F75 Glacie.png'),
(38, 'Wooting 60HE', 'The Wooting 60HE is a groundbreaking 60% analog mechanical keyboard that has set a new standard in competitive gaming peripherals. Its innovative use of Hall Effect switches and rapid trigger technology offers unparalleled responsiveness and customization.\r\n\r\n', 10000, 55, 'Keyboard', 'wooting 60he.png'),
(39, 'IQUNIX EZ63', 'The IQUNIX EZ63 is a premium 60% mechanical keyboard equipped with Hall Effect magnetic switches, offering advanced features tailored for competitive gaming and a refined typing experience.', 10000, 55, 'Keyboard', 'iqunix 68 he.jpg'),
(40, 'Keychron K8', 'The Keychron K8 is a versatile tenkeyless (TKL) mechanical keyboard that seamlessly blends wireless convenience with mechanical performance, catering to both Mac and Windows users.', 3500, 40, 'Keyboard', 'Keychron K8.jpg'),
(41, 'Keychron Q1', 'The Keychron Q1 HE is a premium 75% wireless mechanical keyboard featuring Hall Effect magnetic switches, designed for gamers and professionals seeking precision and customization.', 6000, 55, 'Keyboard', 'Keychron-Q1-HE.jpg'),
(42, 'HyperX Cloud', 'The HyperX Cloud II is a well-regarded gaming headset known for its comfort, sound quality, and multi-platform compatibility. Whether you\'re gaming on PC, console, or mobile, it offers a reliable audio experience.', 5000, 55, 'Headset', 'hyperx cloud ii.jpg'),
(43, 'Logitech G733', 'The Logitech G733 LIGHTSPEED Wireless RGB Gaming Headset is a stylish and comfortable wireless headset designed for gamers and streamers seeking performance and customization.', 4000, 55, 'Headset', 'Logitech G G733.png'),
(44, 'Razer BlackShark', 'The Razer BlackShark V2 series offers a range of gaming headsets tailored to various preferences and needs. Whether you\'re a competitive gamer seeking top-tier performance or someone looking for comfort during extended sessions, there\'s a model for you.', 5000, 60, 'Headset', 'razer blackshark.jpg'),
(45, 'Redragon E200', 'The Redragon Thunder Pro E200 Gaming & Music Earbuds are designed for gamers and music enthusiasts seeking a portable, in-ear audio solution without compromising on sound quality.', 1000, 50, 'Headset', 'Redragon E200.png'),
(46, 'Sony WF-C500', 'The Sony WF-C500 are compact and affordable true wireless earbuds designed to deliver quality audio for everyday use. They offer a comfortable fit, customizable sound, and reliable performance, making them a solid choice for budget-conscious consumers.', 7000, 55, 'Headset', 'Sony WF-C500.png'),
(47, 'Truthear ZERO', 'The Truthear x Crinacle ZERO is a budget-friendly in-ear monitor (IEM) that combines impressive audio performance with a sleek design. Developed in collaboration with Crinacle, a well-known figure in the audio community, this IEM aims to deliver a balanced and engaging listening experience.', 4000, 50, 'Headset', 'Truthear Zero IEM.jpg'),
(48, 'NVIDIA RTX 5070 Ti', 'The NVIDIA GeForce RTX 5070 Ti is a high-performance graphics card launched on February 20, 2025, as part of NVIDIA\'s RTX 50 series, based on the Blackwell architecture. It offers significant improvements over its predecessors, making it a compelling choice for gamers and creators seeking top-tier performance at a more accessible price point.', 70000, 30, 'CPU/GPU', '5070 TI.png'),
(49, 'AMD Ryzen 9 9900X', 'The AMD Ryzen 9 9900X is a high-performance desktop processor launched in August 2024, part of AMD\'s Ryzen 9000 series. Built on the advanced Zen 5 architecture and manufactured using a 4nm process, it offers significant improvements in both gaming and productivity tasks.', 50000, 55, 'CPU/GPU', 'AMD Ryzen 9 9900X.png'),
(50, 'Intel i9-14900K', 'The Intel Core i9-14900K is a flagship 14th Gen desktop processor, launched in October 2023, designed for high-end gaming and productivity tasks.', 60000, 40, 'CPU/GPU', 'intel  i9-14900K.png'),
(51, 'Intel Arc B570', 'The Intel Arc B570 is a mid-range desktop graphics card from Intel\'s second-generation Battlemage lineup, launched in January 2025. It targets 1440p gaming and creative workloads.', 3700, 55, 'CPU/GPU', 'Intel Arc B570.png'),
(52, 'AMD RX 6700 XT', 'The AMD Radeon RX 6700 XT is a mid-range graphics card launched in March 2021, designed to deliver strong performance at 1440p resolution. Built on AMD\'s RDNA 2 architecture, it offers a compelling balance between price and performance for gamers and content creators alike.', 50000, 40, 'CPU/GPU', 'Radeon RX 6700 XT.png'),
(53, 'NVIDIA RTX 4060', 'The NVIDIA GeForce RTX 4060 is a mid-range graphics card launched in June 2023, designed to deliver excellent 1080p gaming performance with support for modern features like ray tracing and DLSS 3.\r\n\r\n', 40000, 55, 'CPU/GPU', 'rtx 4060.png'),
(54, 'NVIDIA RTX 5090', 'The NVIDIA GeForce RTX 5090 is the flagship GPU of the RTX 50 series, officially launched on January 30, 2025. Built on the cutting-edge Blackwell architecture, it represents a significant leap in performance and AI capabilities over its predecessor, the RTX 4090.', 160000, 20, 'CPU/GPU', 'rtx 5090.png'),
(55, 'NVIDIA RTX 4070', 'The NVIDIA GeForce RTX 4070 is a mid-range graphics card launched in April 2023, designed to deliver strong performance at 1440p resolution. Built on NVIDIA\'s Ada Lovelace architecture, it offers a compelling balance between price and performance for gamers and content creators alike.\r\n\r\n', 48000, 45, 'CPU/GPU', 'trx 4070.png'),
(56, 'PS 5', 'The PlayStation 5, developed by Sony Interactive Entertainment, is the latest generation of the PlayStation gaming console. It was officially released on November 12, 2020.', 40000, 55, 'Consoles', 'ps5.png'),
(57, 'PS Controller', 'The DualSense Wireless Controller is Sony\'s revolutionary gamepad designed exclusively for the PlayStation 5 (PS5). It introduces a completely new level of immersion, combining cutting-edge technology with ergonomic comfort.', 5000, 30, 'Consoles', 'ps5_controller.png'),
(58, 'Fantech Shooter II GP13', 'The Fantech Shooter II GP13 is a budget-friendly, wired gaming controller designed for PC and PS3 gaming. It offers a console-like experience with essential features suitable for casual gamers.', 1200, 55, 'Consoles', 'SHOOTER-II-GP13-Gaming-Controller-Gamepad.png'),
(59, 'Fantech Nova WGP14', 'The Fantech Nova WGP14 is a versatile, multi-platform gaming controller designed to enhance your gaming experience across various devices. It combines advanced features like anti-drift technology and customizable controls, making it a compelling choice for gamers seeking both performance and affordability.', 2000, 55, 'Consoles', 'WGP14-PUTIH.png'),
(60, 'Xbox Series X', 'The Xbox Series X is Microsoft\'s flagship console, offering top-tier performance with features like true 4K gaming, up to 120 FPS, and a 1TB SSD for rapid load times.', 50000, 55, 'Consoles', 'xbox_console.png'),
(61, 'Xbox Controller', 'The Xbox Wireless Controller is the official gamepad designed by Microsoft for the Xbox Series X|S and Xbox One consoles. It offers a comfortable, ergonomic design optimized for long gaming sessions, with textured grips on the triggers and bumpers to enhance control and precision.', 5000, 33, 'Consoles', 'xbox_controller.png'),
(62, 'Artisan FX Zero', 'The Artisan FX Zero is a premium gaming mousepad from Japan, renowned for its exceptional build quality, precise control, and versatility across various gaming styles. It\'s particularly favored by competitive players and enthusiasts seeking a balance between speed and control.', 5000, 20, 'Other', 'artison_fxzero.png'),
(63, 'Gateron Blue switches', 'Gateron Blue switches are clicky mechanical switches known for their satisfying tactile bump and audible click with each keystroke. They are often compared to Cherry MX Blue switches but are generally more affordable.', 200, 55, 'Other', 'Getron_blue_switch.png'),
(64, 'Gateron Red switch', 'The Gateron Red switch is a popular linear mechanical keyboard switch known for its smooth keystrokes and quiet operation, making it a favored choice for gamers and typists seeking a responsive and unobtrusive typing experience.', 225, 55, 'Other', 'getron_redSwitch.png'),
(65, 'Zowie G-SR II', 'he Zowie G-SR II is a premium gaming mousepad designed for esports enthusiasts and competitive gamers seeking a balance between control and glide. Building upon the legacy of the original G-SR, the G-SR II introduces several enhancements to improve performance and durability.', 5500, 43, 'Other', 'GSR II.png'),
(66, ' HyperX QuadCast', 'The HyperX QuadCast series comprises USB condenser microphones designed for gamers, streamers, and content creators, offering high-quality audio recording with user-friendly features.', 2500, 18, 'Other', 'HyperX QuadCast.png');

-- --------------------------------------------------------

--
-- Table structure for table `product_user`
--

CREATE TABLE `product_user` (
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(150) NOT NULL,
  `last_name` varchar(150) NOT NULL,
  `username` varchar(150) NOT NULL,
  `email` varchar(150) NOT NULL,
  `number` bigint(20) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `dob` date NOT NULL,
  `userRole` varchar(50) NOT NULL DEFAULT 'customer',
  `profilePic` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `address_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `first_name`, `last_name`, `username`, `email`, `number`, `gender`, `dob`, `userRole`, `profilePic`, `password`, `address_id`) VALUES
(1, 'Madan ', 'Shrestha', 'madmax', 'madmax@gmail.com', 2147483647, 'male', '2005-12-31', 'admin', 'Madan.png', 'V1cXgVQgsEd2n/uAOFKpIHtl9myHyp1WF9zJOyENRQtvrv1Bij+zpzjTVWP60GXqeWUM6w==', 1),
(2, 'Dikshant ', 'Timsina', 'painter', 'painter@gmail.com', 2147483647, 'male', '2005-01-31', 'customer', 'DIkshant.png', 'jDO5+WC1v29Ij6tKmffx8SkwLbJsOpT3b2K13yy70LHNjI4nsfkZFZjvNEqr8BMXFDzn/xI=', 2),
(3, 'Pratyush', 'Badal', 'pattu', 'pattu@gmail.com', 2147483647, 'male', '2004-02-18', 'customer', 'user_3_1747882301481_IMG-20230703-WA0000.jpg', '8CoVFnqGEaC7YwUYoRcI1oPDR3myeRRxxWg0IuILHOJAKQQNXC8XX9NsY50RkAGIb+wKfwI=', 3),
(4, 'Abhinav', 'Paudyal', 'mottu', 'mottu@gmail.com', 2147483647, 'male', '2005-12-31', 'customer', 'user_4_1747882623785_abhi.jpg', '9cUkYyQqcsYiN0Hs10oCIdlx5nfaxCiW3aP9rUQTLO1QlV+CKd6jzqQYbkn+2Qhm8FxTPZE=', 1),
(5, 'Rijan', 'Buddhacharya', 'rizz', 'rizz@gmail.com', 2147483647, 'male', '2005-12-31', 'customer', 'user_5_1747883004001_rizz.png', 'O62PL5+lsRSXu/Y8QB4f4ImnlxrFJxu6ySPUN6NaEabshr3OobiNkTxn+igIbVYUIS3RKg==', 1),
(6, 'Rabina ', 'Lama', 'rabina', 'rabina01@gmail.com', 2147483647, 'female', '1984-12-31', 'customer', 'kum.jpeg', '6d/fNvjGqhnzGaDRrhhpCJoHYyMiR2zGCd0WC8WYYumaw+PrpsPV5h4K3+3W+C14y0AgBAE=', 2),
(19, 'rabinaaaa', 'lama', 'rabina12', 'rabina12@gmail.com', 2147483647, 'female', '1998-02-03', 'customer', 'warehouse.jpeg', 'DfiYi8x7UykIkofbB99sQ0SepcbCSpKFNG87IVdguX6irPOUxkPsyZZ/jplYKWP1UNekGc64', 1),
(20, 'Madan', 'Shrestha', 'madan', 'madan@gmail.com', 9865403210, 'male', '1992-12-31', 'customer', 'abe.png', 'JrMD9mE9H5zkKpLkeZ1fgZ4eXuRkYm49Fb7YRPZoaYVrjgklWBjgSqN2CPfxpMn2q+PHoBC1', 2);

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist` (
  `wishlist_id` int(11) NOT NULL,
  `wishlist_name` varchar(50) NOT NULL,
  `addedAt` date NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlist`
--

INSERT INTO `wishlist` (`wishlist_id`, `wishlist_name`, `addedAt`, `user_id`) VALUES
(5, 'My Wishlist', '2025-05-22', 2);

-- --------------------------------------------------------

--
-- Table structure for table `wishlist_product`
--

CREATE TABLE `wishlist_product` (
  `wishlist_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlist_product`
--

INSERT INTO `wishlist_product` (`wishlist_id`, `product_id`) VALUES
(5, 60);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`address_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`,`user_id`),
  ADD KEY `user_fk1` (`user_id`);

--
-- Indexes for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD PRIMARY KEY (`cartItem_id`),
  ADD KEY `cart_fk` (`cart_id`),
  ADD KEY `product_fk1` (`product_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `order_table`
--
ALTER TABLE `order_table`
  ADD PRIMARY KEY (`order_id`,`cart_id`,`user_id`),
  ADD KEY `cart_fk1` (`cart_id`),
  ADD KEY `user_fk2` (`user_id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `product_user`
--
ALTER TABLE `product_user`
  ADD PRIMARY KEY (`product_id`,`user_id`),
  ADD KEY `user_fk` (`user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `address_fk` (`address_id`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`wishlist_id`,`user_id`),
  ADD KEY `user_fk3` (`user_id`);

--
-- Indexes for table `wishlist_product`
--
ALTER TABLE `wishlist_product`
  ADD PRIMARY KEY (`wishlist_id`,`product_id`),
  ADD KEY `product_fk2` (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `address_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `cartitem`
--
ALTER TABLE `cartitem`
  MODIFY `cartItem_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `order_table`
--
ALTER TABLE `order_table`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `wishlist`
--
ALTER TABLE `wishlist`
  MODIFY `wishlist_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `user_fk1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `cartitem`
--
ALTER TABLE `cartitem`
  ADD CONSTRAINT `cart_fk` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
  ADD CONSTRAINT `product_fk1` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_table` (`order_id`),
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);

--
-- Constraints for table `order_table`
--
ALTER TABLE `order_table`
  ADD CONSTRAINT `cart_fk1` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
  ADD CONSTRAINT `user_fk2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `product_user`
--
ALTER TABLE `product_user`
  ADD CONSTRAINT `product_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  ADD CONSTRAINT `user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `address_fk` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`);

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `user_fk3` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `wishlist_product`
--
ALTER TABLE `wishlist_product`
  ADD CONSTRAINT `product_fk2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`),
  ADD CONSTRAINT `wishlist_fk` FOREIGN KEY (`wishlist_id`) REFERENCES `wishlist` (`wishlist_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
