$(document).ready(function () {
    $.ajax({
        url: 'http://localhost:8080/restapi/menus',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            let ulHTML = '<ul>';
            ulHTML += buildMenuHTML(data);
            ulHTML += '</ul>';

            $('#menu-table-body').html(ulHTML); // Replace the content of #menu-table-body with ulHTML
        },
    error: function (xhr, status, error) {
            console.error('Error fetching books:', status, error);
        }
    });
    function buildMenuHTML(items) {
        let html = '';
        $.each(items, function (i, item) {
            html += '<li>';
            html += '<a href="' + item.url + '">' + item.name + '</a>';

            // Check if there are children menus
            if (item.children && item.children.length > 0) {
                html += '<ul>';
                html += buildMenuHTML(item.children); // Recursive call for children
                html += '</ul>';
            }

            html += '</li>';
        });
        return html;
    }
});
