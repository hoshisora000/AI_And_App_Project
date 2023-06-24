<?php
session_start();

if ($_SESSION["level"] != 9) {
	header("Location:login.php?url=admin.php");
}

$severname = "192.168.2.200"; //SQL位置
$username = "hoshiso1_system"; //帳號
$password = "system123456"; //密碼
$dbname = "hoshiso1_project"; //SQL名稱
$link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

if ($result = mysqli_query($link, "SELECT * FROM winning_numbers")) {
      while ($row = mysqli_fetch_assoc($result)) {
          //$a['data'][] = array($row["uid"],$row["nickname"], $row["mobile_barcode"], "<a href='mailto:" . $row["member_email"] . "'>" . $row["member_email"] . "</a>","<button class='m_btn_delete btn btn-danger btn-xs' id='m_btn_delete'><i class='glyphicon glyphicon-pencil'></i>刪除</button>");
          $a['data'][] = array($row["period"],$row["super_special"],$row["special"], $row["head1"], $row["head2"], $row["head3"],"<button class='m_btn_edit btn btn-warning btn-xs' id='m_btn_edit'><i class='glyphicon glyphicon-pencil'></i>修改</button>　<button class='m_btn_delete btn btn-danger btn-xs' id='m_btn_delete'><i class='glyphicon glyphicon-pencil'></i>刪除</button>");
    }
    mysqli_free_result($result); // 釋放佔用的記憶體
}
mysqli_close($link); // 關閉資料庫連結

echo json_encode($a);

?>