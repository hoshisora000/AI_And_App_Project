-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 192.168.2.200:3306
-- 產生時間： 2023 年 06 月 24 日 22:48
-- 伺服器版本： 10.6.13-MariaDB
-- PHP 版本： 8.1.19

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
-- 資料表結構 `bulletin_board`
--

CREATE TABLE IF NOT EXISTS `bulletin_board` (
  `id` varchar(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(40) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `message` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `bulletin_board`
--

INSERT INTO `bulletin_board` (`id`, `name`, `email`, `phone`, `message`) VALUES
('1', '1', '1', '1', '1'),
('2', '2', '2', '2', '2');

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

CREATE TABLE IF NOT EXISTS `member` (
  `uid` varchar(60) NOT NULL,
  `nickname` varchar(10) NOT NULL,
  `mobile_barcode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `member`
--

INSERT INTO `member` (`uid`, `nickname`, `mobile_barcode`) VALUES
('AJ4OmieRGnWB2cerCjhGQexnOeo1', 'RRR', ''),
('AjyC4CzqnKfSgo0LczuqxavveRH2', 'RR', ''),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'CHIH-HSUAN', '/-OEQAHR'),
('Jl1T46BWGZZdD3ktj490368EVN73', 'WSD', '/-HSUWOL'),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'test', '7777777'),
('test1', 'test1', ''),
('test2', 'test2', ''),
('test4', 'test4', ''),
('u0lPWzCeDBhNvRGAYdOw80wVQ352', 'OWO', '1111');

-- --------------------------------------------------------

--
-- 資料表結構 `member_invoice`
--

CREATE TABLE IF NOT EXISTS `member_invoice` (
  `uid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `invoice_number` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `money` int(11) NOT NULL,
  PRIMARY KEY (`uid`,`invoice_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- 傾印資料表的資料 `member_invoice`
--

INSERT INTO `member_invoice` (`uid`, `invoice_number`, `date`, `time`, `money`) VALUES
('AJ4OmieRGnWB2cerCjhGQexnOeo1', 'PD24148306', '2023-05-05', '06:23:00', 139),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'DF74745218', '2023-05-27', '08:38:00', 888),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'DV54386942', '2022-12-14', '12:49:00', 85),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'FX52694745', '2023-06-11', '08:17:00', 5611),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'GF71143355', '2023-03-02', '00:06:59', 68),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'MR63999145', '2023-03-02', '00:05:59', 600),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'NW53231310', '2023-06-11', '13:06:00', 38),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'PD24149306', '2023-06-11', '04:09:00', 139),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'PF24148306', '2023-06-11', '19:21:00', 139),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'QK20783987', '2023-03-02', '00:04:59', 100),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'QQ64795654', '2023-06-12', '06:40:00', 22),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'RE63195867', '2023-04-27', '08:57:00', 52),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'SX74742579', '2023-05-17', '00:20:00', 52),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'VR72558852', '2023-05-13', '02:21:00', 43),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'WH73647968', '2023-06-11', '07:36:00', 145692),
('hAonkFh8d3WLtpebxHtJKlQD5w92', 'ZA53447908', '2023-06-11', '15:31:00', 5234),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'AL54321986', '2023-04-19', '12:15:00', 65),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'GV32795473', '2023-05-22', '01:35:00', 66),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'LL73110635', '2023-05-22', '22:38:00', 65),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'NQ12345678', '2023-05-12', '21:31:00', 100),
('O2g8sZF0nMh1ZJaq8M6xh6w5CnD2O2g8sZF0nMh1ZJaq8M6xh6', 'ZR46856298', '2023-05-02', '12:09:00', 98),
('u0lPWzCeDBhNvRGAYdOw80wVQ352', 'LJ93347024', '2023-04-12', '23:33:00', 468),
('u0lPWzCeDBhNvRGAYdOw80wVQ352', 'NN47360325', '2023-05-26', '01:01:00', 84);

-- --------------------------------------------------------

--
-- 資料表結構 `winning_numbers`
--

CREATE TABLE IF NOT EXISTS `winning_numbers` (
  `period` int(7) NOT NULL,
  `super_special` varchar(8) NOT NULL,
  `special` varchar(8) NOT NULL,
  `head1` varchar(8) NOT NULL,
  `head2` varchar(8) NOT NULL,
  `head3` varchar(8) NOT NULL,
  PRIMARY KEY (`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 傾印資料表的資料 `winning_numbers`
--

INSERT INTO `winning_numbers` (`period`, `super_special`, `special`, `head1`, `head2`, `head3`) VALUES
(1120102, '06634385', '66882140', '25722152', '93412693', '16957025'),
(1120304, '20783987', '04135859', '94899145', '71143793', '41055355');

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `member_invoice`
--
ALTER TABLE `member_invoice`
  ADD CONSTRAINT `member_invoice_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `member` (`uid`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
