-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 192.168.2.200:3306
-- 產生時間： 2023 年 05 月 29 日 14:54
-- 伺服器版本： 10.6.13-MariaDB
-- PHP 版本： 8.1.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `hoshiso1_project`
--
CREATE DATABASE IF NOT EXISTS `hoshiso1_project` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `hoshiso1_project`;

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

CREATE TABLE `member` (
  `uid` varchar(60) NOT NULL,
  `nickname` varchar(10) NOT NULL,
  `mobile_barcode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `member`
--

INSERT INTO `member` (`uid`, `nickname`, `mobile_barcode`) VALUES
('hAonkFh8d3WLtpebxHtJKlQD5w92', '7666', ''),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', '炫寶', '87878740');

-- --------------------------------------------------------

--
-- 資料表結構 `member_invoice`
--

CREATE TABLE `member_invoice` (
  `uid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `invoice_number` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `money` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- 傾印資料表的資料 `member_invoice`
--

INSERT INTO `member_invoice` (`uid`, `invoice_number`, `date`, `time`, `money`) VALUES
('59815151AS5D1A5dSA5DSADSAD', 'ZZ12345678', '2023-05-22', '10:22:33', 542),
('59815151AS5D1A5dSA5DSADSAD2', 'ZZ12345678', '2023-05-22', '10:22:33', 542),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'DF74745218', '2023-05-27', '08:38:00', 888),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'RE63195867', '2023-04-27', '08:57:00', 52),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'AL54321986', '2023-04-19', '12:15:00', 65),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'GV32795473', '2023-05-22', '01:35:00', 66),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'LL73110635', '2023-05-22', '22:38:00', 65),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'NQ12345678', '2023-05-12', '21:31:00', 100),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'ZR46856298', '2023-05-02', '12:09:00', 98);

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`uid`);

--
-- 資料表索引 `member_invoice`
--
ALTER TABLE `member_invoice`
  ADD PRIMARY KEY (`uid`,`invoice_number`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
