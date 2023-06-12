$(function() {
    tb2 = $('#example').DataTable({
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
});