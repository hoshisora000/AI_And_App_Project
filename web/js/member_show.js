$(function() {
    tb2 = $('#example').DataTable({
        "language": {
            "processing": "處理中...",
            "loadingRecords": "載入中...",
            "lengthMenu": "顯示 _MENU_ 項結果",
            "zeroRecords": "沒有符合的結果",
            "info": "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
            "infoEmpty": "顯示第 0 至 0 項結果，共 0 項",
            "infoFiltered": "(從 _MAX_ 項結果中過濾)",
            "infoPostFix": "",
            "search": "搜尋:",
            "paginate": {
                "first": "第一頁",
                "previous": "上一頁",
                "next": "下一頁",
                "last": "最後一頁"
            },
            "aria": {
                "sortAscending": ": 升冪排列",
                "sortDescending": ": 降冪排列"
            }
        },
        responsive: true,
        columnDefs: [
            { "width": "20%", "targets": 0 },
            { responsivePriority: 10009, targets: [0] },
            { responsivePriority: 1, targets: [1] },
            { responsivePriority: 10005, targets: [2] },
            { responsivePriority: 2, targets: [3] }
        ],
        "ajax": './ajax/member_show_ajax.php'
    });
    $('tbody').on('click', '#m_btn_delete', function() {
        var id = tb2.row($(this).closest('tr')).data();
        var t_id = id[0]
        $.ajax({
            url: "delete_member.php",
            data: {
                t_id,
            },
            type: "POST",
            dataType: 'text',
            success: function() {
                console.log("會員ID " + t_id + " 刪除成功");
            },
            error: function(xhr, ajaxOptions, thrownError) {

            }
        });
        tb2.ajax.reload();
    });
});