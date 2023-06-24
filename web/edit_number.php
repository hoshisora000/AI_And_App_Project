<?php
session_start();
if ($_SESSION["level"] != 9) {
	header("Location:login.php?url=admin.php");
}

$id = $_GET['id'];

$severname = "192.168.2.200"; //SQL位置
$username = "hoshiso1_system"; //帳號
$password = "system123456"; //密碼
$dbname = "hoshiso1_project"; //SQL名稱
$link = mysqli_connect($severname, $username, $password, $dbname); // 建立MySQL的資料庫連結

// 送出查詢的SQL指令

$result = mysqli_query($link, "SELECT * FROM `winning_numbers` WHERE `period`='" . $id . "'");
while ($row = mysqli_fetch_assoc($result)) { // 迴圈逐一取得資料列
    $ssp=$row["super_special"]; 
    $sp=$row["special"];
    $head1=$row["head1"];
    $head2=$row["head2"];
    $head3=$row["head3"];
}
mysqli_free_result($result); // 釋放佔用的記憶體

mysqli_close($link); // 關閉資料庫連結
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>發票助手</title>
    <!-- Favicon-->
    <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
    <!-- Bootstrap icons-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
    <!-- Core theme CSS (includes Bootstrap)-->
    <link href="css/styles.css" rel="stylesheet" />
</head>

<body class="d-flex flex-column">
    <main class="flex-shrink-0">
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container px-5">
                <a class="navbar-brand" href="index.html">發票助手</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation"><span
                        class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link" href="index.html">首頁</a></li>
                        <li class="nav-item"><a class="nav-link" href="about.html">關於我們</a></li>
                        <li class="nav-item"><a class="nav-link" href="contact.php">聯絡我們</a></li>
                        <li class="nav-item"><a class="nav-link" href="admin.php">管理員後台</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- Pricing section-->
        <section class="bg-light py-5">
            <div class="container px-5 my-5">
                <div class="text-center mb-5">

                </div>
                <div class="row gx-5 justify-content-center">
                    <!-- Pricing card free-->
                    <!---內容加在裡面-->
                    <div class="col-md-10 mx-auto col-lg-5">
                        <div class="p-5 mb-4 bg-light rounded-3">
                            <h3 class="display-4 fw-bold lh-1">修改發票號碼</h3>
                            <div class="container-fluid py-5">
                                <form class="p-4 p-md-5 border rounded-3 bg-light" name="form1" id="form1" action="./add_number.php"
                                    method="POST" enctype='multipart/form-data'>
                                    <div class="form-floating mb-3">
                                        <input class="form-control" type="text" name="per" size="20"
                                            placeholder="per" value="<?php echo $id;?>">
                                        <label for="floatingInput">發票期數</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control" type="text" name="ssp" size="20"
                                            placeholder="ssp" value="<?php echo $ssp;?>">
                                        <label for="floatingInput">特別獎</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control price_change" id="sp" type="text" name="sp"
                                            size="20" placeholder="sp" value="<?php echo $sp;?>">
                                        <label for="floatingInput">特獎</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control price_change" id="head1" type="text" name="head1"
                                            size="20" placeholder="head1" value="<?php echo $head1;?>">
                                        <label for="floatingInput">頭獎1</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control price_change" id="head2" type="text" name="head2"
                                            size="20" placeholder="head2" value="<?php echo $head2;?>">
                                        <label for="floatingInput">頭獎2</label>
                                    </div>
                                    <div class="form-floating mb-3">
                                        <input class="form-control price_change" id="head3" type="text" name="head3"
                                            size="20" placeholder="head3" value="<?php echo $head3;?>">
                                        <label for="floatingInput">頭獎3</label>
                                    </div>

                                    <button class="btn btn-primary btn-lg px-4 me-md-2" type="submit">送出</button>
                                    <a href="./set_number.php" class="btn btn-warning btn-lg px-4">取消</a>
                                    
                                </form>

                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </section>
    </main>
    <!-- Footer-->
    <footer class="bg-dark py-4 mt-auto">
        <div class="container px-5">
            <div class="row align-items-center justify-content-between flex-column flex-sm-row">
                <div class="col-auto">
                    <div class="small m-0 text-white">Copyright &copy; ncue_ai.app.project 2023</div>
                </div>
                <div class="col-auto">
                    <a class="link-light small" href="#!">隱私宣告</a>
                    <span class="text-white mx-1">&middot;</span>
                    <a class="link-light small" href="#!">開發團隊</a>
                    <span class="text-white mx-1">&middot;</span>
                    <a class="link-light small" href="mailto:ai.app.project.ncue@gmail.com">聯絡我們</a>
                </div>
            </div>
        </div>
    </footer>
    <!-- Bootstrap core JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Core theme JS-->
    <script src="js/scripts.js"></script>
</body>

</html>