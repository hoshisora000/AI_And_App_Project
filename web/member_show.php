<?php
session_start();
//level a
if ($_SESSION["level"] != 9) {
	header("Location:login.php?url=admin.php");
}
?>



<!DOCTYPE html>
<html lang="en">

<head>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.min.css">

    <script src="//code.jquery.com/jquery-3.3.1.js"></script>
    <script src="//cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
    <script src="//cdn.datatables.net/1.10.16/js/dataTables.bootstrap.min.js"></script>

    <script src="./js/member_show.js"></script>
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
                        <li class="nav-item"><a class="nav-link" href="contact.html">聯絡我們</a></li>
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
                    <div class="container my-5">
                        <div class="row p-4 pb-0 pe-lg-0 pt-lg-5 align-items-center rounded-3 border shadow-lg">
                            <h1 class="display-4 fw-bold lh-1">會員資料檢閱</h1>

                            <div class="col-md-3"></div>
                            <div class="text-center">
                                <table id="example" class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th class="text-center">會員ID</th>
                                            <th class="text-center">暱稱</th>
                                            <th class="text-center">手機條碼</th>
                                            <th class="text-center">刪除</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                            <div class="col-md-3"></div>

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