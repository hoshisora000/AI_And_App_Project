<?php
session_start();
if ($_SESSION["level"] != 9) {
	header("Location:login.php?url=admin.php");
}

$id = $_POST['t_id'];

$severname = "192.168.2.200"; //SQL位置
$username = "hoshiso1_system"; //帳號
$password = "system123456"; //密碼
$dbname = "hoshiso1_project"; //SQL名稱
$link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

// 送出查詢的SQL指令
$result = mysqli_query($link, "DELETE FROM `winning_numbers` WHERE `period`='" . $id . "'");

mysqli_free_result($result); // 釋放佔用的記憶體

mysqli_close($link); // 關閉資料庫連結
?>