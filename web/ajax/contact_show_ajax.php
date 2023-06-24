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

if ($result = mysqli_query($link, "SELECT * FROM bulletin_board")) {
      while ($row = mysqli_fetch_assoc($result)) {
        //$a['data'][] = array($row["uid"],$row["nickname"], $row["mobile_barcode"], "<a href='mailto:" . $row["member_email"] . "'>" . $row["member_email"] . "</a>","<button class='m_btn_delete btn btn-danger btn-xs' id='m_btn_delete'><i class='glyphicon glyphicon-pencil'></i>刪除</button>");
        if(!strcmp($row["state"],"unread")){
            $a['data'][] = array($row["id"],$row["name"],"<a href='mailto:" . $row["email"] . "'>" . $row["email"] . "</a>", $row["phone"], $row["message"],"<button class='m_btn_unread btn btn-warning btn-xs' id='m_btn_unread'><i class='glyphicon glyphicon-pencil'></i>未讀</button>");
        }else{
            $a['data'][] = array($row["id"],$row["name"],"<a href='mailto:" . $row["email"] . "'>" . $row["email"] . "</a>", $row["phone"], $row["message"], "<button class='m_btn_unread btn btn-success btn-xs' id='m_btn_unread'><i class='glyphicon glyphicon-pencil'></i>已讀</button>");
        }
    }
    mysqli_free_result($result); // 釋放佔用的記憶體
}
mysqli_close($link); // 關閉資料庫連結

echo json_encode($a);

?>