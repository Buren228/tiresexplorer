document.getElementById('searchButton').addEventListener('click', function () {
    const width = document.getElementById('width').value;
    const height = document.getElementById('heights').value;
    const diameter = document.getElementById('diameters').value;
    const season = document.getElementById('season').value;
    const spikes = document.getElementById('spikes').value;
    const runflat = document.getElementById('runflat').checked;
    const lightweight = document.getElementById('lightweight').checked;
    const mt = document.getElementById('mt').checked;
    const at = document.getElementById('at').checked;
    const brand = document.getElementById('brands').value;

    const filters = {
        width,
        height,
        diameter,
        season,
        spikes,
        runflat,
        lightweight,
        mt,
        at,
        brand
    };

    fetch('/fetchData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(filters)
    })
        .then(response => response.text())
        .then(data => {
            document.getElementById('response').innerText = data;
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});

document.addEventListener('DOMContentLoaded', function () {
    setupAutocomplete('brands', 'brandsContainer', 'brands', true);
    setupAutocomplete('width', 'widthContainer', 'width', false);
    setupAutocomplete('heights', 'heightContainer', 'height', false);
    setupAutocomplete('diameters', 'diameterContainer', 'diameter', false);

    function setupAutocomplete(inputId, containerId, requestType, isTextType) {
        const input = document.getElementById(inputId);
        const container = document.getElementById(containerId);
        let allValues = [];
        let isDataLoaded = false;

        // Функция для выполнения запроса
        function fetchData() {
            fetch('/getData', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: requestType
            })
                .then(response => {
                    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                    return response.json();
                })
                .then(values => {
                    allValues = isTextType
                        ? values.sort((a, b) => a.localeCompare(b, 'ru'))
                        : values.sort((a, b) => parseFloat(a) - parseFloat(b));

                    isDataLoaded = true;
                    displayValues(allValues);
                })
                .catch(error => console.error(`Ошибка загрузки ${requestType}:`, error));
        }

        // Запрашиваем данные при клике и раз в 30 минут
        input.addEventListener('click', function () {
            if (!isDataLoaded) {
                fetchData();
            } else {
                displayValues(allValues);
            }
        });

        input.addEventListener('input', function () {
            const query = this.value.trim().toLowerCase();
            displayValues(query.length > 0
                ? allValues.filter(val => val.toLowerCase().startsWith(query))
                : allValues);
        });

        // Скрытие автозаполнения при клике вне блока
        document.addEventListener('click', function (event) {
            if (!container.contains(event.target) && event.target !== input) {
                container.classList.remove('visible');
            }
        });

        // Функция для отображения значений
        function displayValues(values) {
            container.innerHTML = '';
            if (values.length > 0) {
                container.classList.add('visible');
                values.forEach(value => {
                    const div = document.createElement('div');
                    div.classList.add('autocomplete-brand');
                    div.textContent = value;
                    div.addEventListener('click', function () {
                        input.value = value;
                        container.innerHTML = '';
                        container.classList.remove('visible');
                    });
                    container.appendChild(div);
                });
            } else {
                container.classList.remove('visible');
            }
        }

        // Перезапрос данных каждые 30 минут (1800000 миллисекунд)
        setInterval(fetchData, 1800000);
    }
});