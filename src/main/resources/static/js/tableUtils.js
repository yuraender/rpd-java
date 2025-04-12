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

function sortTable(tableId, colIndex) {
    const table = document.getElementById(tableId);

    // Сохраняем состояние сортировки
    this.asc = !this.asc

    const getCellValue = (tr, idx) => tr.children[idx].textContent || tr.children[idx].innerText;
    const comparer = (idx, asc) => (a, b) => ((v1, v2) =>
            v1 !== '' && v2 !== '' && !isNaN(v1) && !isNaN(v2) ? v1 - v2 : v1.toString().localeCompare(v2)
    )(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

    const body = table.querySelector('tbody');
    // Сортировка строк
    Array.from(body.querySelectorAll('tr'))
        .sort(comparer(colIndex, this.asc))
        .forEach(tr => body.appendChild(tr));
    return this.asc;
}

// Инициализация обработчиков клика только для второго th в thead
document.querySelectorAll('table thead tr:nth-child(2) th').forEach(th => {
    th.addEventListener('click', function () {
        const table = this.closest('table');
        const colIndex = Array.from(this.parentNode.children).indexOf(this);

        // Удаляем классы у всех заголовков
        table.querySelectorAll('thead th').forEach(h => {
            h.classList.remove('sorted-asc', 'sorted-desc');
        });

        this.asc = sortTable(table.id, colIndex);

        // Добавляем класс текущему заголовку
        this.classList.add(this.asc ? 'sorted-asc' : 'sorted-desc');
    });
});
