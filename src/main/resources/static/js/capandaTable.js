jQuery(function($){
    $("#capanda-table").DataTable({
        lengthChange: false,
        // searching: false,
        displayLength: 50,
        order: [ [ 1, "asc" ] ]
    });
});
