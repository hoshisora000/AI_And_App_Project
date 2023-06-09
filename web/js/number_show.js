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
            { responsivePriority: 1, targets: [0] },
            { responsivePriority: 2, targets: [-1] }
        ],
        "ajax": './ajax/number_show_ajax.php'
    });
    $('tbody').on('click', '#m_btn_delete', function() {
        var id = tb2.row($(this).closest('tr')).data();
        var t_id = id[0]
        $.ajax({
            url: "delete_number.php",
            data: {
                t_id,
            },
            type: "POST",
            dataType: 'text',
            success: function() {
                
            },
            error: function(xhr, ajaxOptions, thrownError) {

            }
        });
        tb2.ajax.reload();
    });
    $('tbody').on('click', '#m_btn_edit', function() {
        var id = tb2.row($(this).closest('tr')).data();
        var t_id = id[0];
        var url = "edit_number.php";
        url=url.concat("?id=",t_id);
        window.location.href=url;
    });
});