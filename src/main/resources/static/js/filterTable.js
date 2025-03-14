function filterTable(tableId, colIndex) {
    const table = document.getElementById(tableId);
    const tr = table.getElementsByTagName("tr");

    const input = table.querySelectorAll('thead input')[colIndex];
    const filter = input.value.toLowerCase();

    for (let i = 2; i < tr.length; i++) {
        const td = tr[i].getElementsByTagName("td")[colIndex];
        if (td) {
            let txtValue = td.textContent || td.innerText;
            if (txtValue.toLowerCase().indexOf(filter) !== -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}
