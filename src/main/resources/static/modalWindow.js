function showModalWithMessage(message, thisPage) {
    showModal(message, thisPage);
    setTimeout(() => {
        document.getElementById('successModal').remove();
        document.querySelector('.modal-backdrop').remove();
        document.body.style.overflow = 'auto'
        document.documentElement.style.overflow = 'auto'
        document.body.style.padding = '0px'
    }, 2000)
}

//модальное окно
function showModal(message, thisPage) {
    let modalHtml = `
        <div class="modal fade" id="successModal" tabindex="-1" aria-labelledby="successModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="alert alert-success" role="alert">
                            ${message}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
    // Вставить модальное окно в body
    document.body.insertAdjacentHTML('beforeend', modalHtml);

    // Показать модальное окно
    let successModal = new bootstrap.Modal(document.getElementById('successModal'), {
        keyboard: false
    });
    successModal.show();

    // Удалить модальное окно после закрытия
    document.getElementById('successModal').addEventListener('hidden.bs.modal', function (event) {
        document.getElementById('successModal').remove();
    });
}