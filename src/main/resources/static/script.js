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
    const input = document.getElementById('brands');
    const container = document.getElementById('brandsContainer');
    let allBrands = []; // Храним все бренды

    // Загрузка всех брендов при инициализации
    fetch('/getData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: 'brands'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(brands => {
            allBrands = brands.sort((a, b) => a.localeCompare(b, 'ru')); // Сортируем по алфавиту
        })
        .catch(error => {
            console.error("Error fetching brands:", error);
        });

    // Событие ввода текста
    input.addEventListener('input', function () {
        const query = this.value.trim();

        if (query.length > 0) {
            // Фильтруем бренды по введенному тексту
            const filteredBrands = allBrands.filter(brand =>
                brand.toLowerCase().startsWith(query.toLowerCase())
            );

            displayBrands(filteredBrands);
        } else {
            displayBrands(allBrands);
        }
    });

    // Событие клика на поле ввода
    input.addEventListener('click', function () {
        // Если поле пустое, отображаем все бренды
        if (this.value.trim().length === 0) {
            displayBrands(allBrands);
        }
    });

    // Скрытие автозаполнения при клике вне блока
    document.addEventListener('click', function (event) {
        if (!container.contains(event.target) && event.target !== input) {
            container.classList.remove('visible');
        }
    });

    // Функция отображения вариантов
    function displayBrands(brands) {
        container.innerHTML = ''; // Очищаем предыдущие предложения

        if (brands.length > 0) {
            container.classList.add('visible'); // Показываем контейнер
            brands.forEach(brand => {
                const div = document.createElement('div');
                div.classList.add('autocomplete-brand');
                div.textContent = brand;

                // Заполнение поля ввода при выборе варианта
                div.addEventListener('click', function () {
                    input.value = brand;
                    container.innerHTML = '';
                    container.classList.remove('visible'); // Скрываем контейнер
                });

                container.appendChild(div);
            });
        } else {
            container.classList.remove('visible'); // Скрываем контейнер, если пусто
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('width');
    const container = document.getElementById('widthContainer');
    let allWidths = []; // Храним все бренды

    // Загрузка всех брендов при инициализации
    fetch('/getData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: 'width'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(widths => {
            allWidths = widths.sort((a, b) => parseFloat(a) - parseFloat(b)); // Сортируем по алфавиту
        })
        .catch(error => {
            console.error("Error fetching widths:", error);
        });

    // Событие ввода текста
    input.addEventListener('input', function () {
        const query = this.value.trim();

        if (query.length > 0) {
            // Фильтруем бренды по введенному тексту
            const filteredWidths = allWidths.filter(width =>
                width.toLowerCase().startsWith(query.toLowerCase())
            );

            displayWidths(filteredWidths);
        } else {
            displayWidths(allWidths);
        }
    });

    // Событие клика на поле ввода
    input.addEventListener('click', function () {
        // Если поле пустое, отображаем все бренды
        if (this.value.trim().length === 0) {
            displayWidths(allWidths);
        }
    });

    // Скрытие автозаполнения при клике вне блока
    document.addEventListener('click', function (event) {
        if (!container.contains(event.target) && event.target !== input) {
            container.classList.remove('visible');
        }
    });

    // Функция отображения вариантов
    function displayWidths(widths) {
        container.innerHTML = ''; // Очищаем предыдущие предложения

        if (widths.length > 0) {
            container.classList.add('visible'); // Показываем контейнер
            widths.forEach(width => {
                const div = document.createElement('div');
                div.classList.add('autocomplete-brand');
                div.textContent = width;

                // Заполнение поля ввода при выборе варианта
                div.addEventListener('click', function () {
                    input.value = width;
                    container.innerHTML = '';
                    container.classList.remove('visible'); // Скрываем контейнер
                });

                container.appendChild(div);
            });
        } else {
            container.classList.remove('visible'); // Скрываем контейнер, если пусто
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('heights');
    const container = document.getElementById('heightContainer');
    let allHeights = []; // Храним все бренды

    // Загрузка всех брендов при инициализации
    fetch('/getData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: 'height'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(heights => {
            allHeights = heights.sort((a, b) => parseFloat(a) - parseFloat(b)); // Сортируем по алфавиту
        })
        .catch(error => {
            console.error("Error fetching heights:", error);
        });

    // Событие ввода текста
    input.addEventListener('input', function () {
        const query = this.value.trim();

        if (query.length > 0) {
            // Фильтруем бренды по введенному тексту
            const filteredHeights = allHeights.filter(height =>
                height.toLowerCase().startsWith(query.toLowerCase())
            );

            displayHeights(filteredHeights);
        } else {
            displayHeights(allHeights);
        }
    });

    // Событие клика на поле ввода
    input.addEventListener('click', function () {
        // Если поле пустое, отображаем все бренды
        if (this.value.trim().length === 0) {
            displayHeights(allHeights);
        }
    });

    // Скрытие автозаполнения при клике вне блока
    document.addEventListener('click', function (event) {
        if (!container.contains(event.target) && event.target !== input) {
            container.classList.remove('visible');
        }
    });

    // Функция отображения вариантов
    function displayHeights(heights) {
        container.innerHTML = ''; // Очищаем предыдущие предложения

        if (heights.length > 0) {
            container.classList.add('visible'); // Показываем контейнер
            heights.forEach(height => {
                const div = document.createElement('div');
                div.classList.add('autocomplete-brand');
                div.textContent = height;

                // Заполнение поля ввода при выборе варианта
                div.addEventListener('click', function () {
                    input.value = height;
                    container.innerHTML = '';
                    container.classList.remove('visible'); // Скрываем контейнер
                });

                container.appendChild(div);
            });
        } else {
            container.classList.remove('visible'); // Скрываем контейнер, если пусто
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const input = document.getElementById('diameters');
    const container = document.getElementById('diameterContainer');
    let allDiameters = []; // Храним все бренды

    // Загрузка всех брендов при инициализации
    fetch('/getData', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: 'diameter'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(diameters => {
            allDiameters = diameters.sort((a, b) => parseFloat(a) - parseFloat(b)); // Сортируем по алфавиту
        })
        .catch(error => {
            console.error("Error fetching diameters:", error);
        });

    // Событие ввода текста
    input.addEventListener('input', function () {
        const query = this.value.trim();

        if (query.length > 0) {
            // Фильтруем бренды по введенному тексту
            const filteredDiameters = allDiameters.filter(diameter =>
                diameter.toLowerCase().startsWith(query.toLowerCase())
            );

            displayDiameters(filteredDiameters);
        } else {
            displayDiameters(allDiameters);
        }
    });

    // Событие клика на поле ввода
    input.addEventListener('click', function () {
        // Если поле пустое, отображаем все бренды
        if (this.value.trim().length === 0) {
            displayDiameters(allDiameters);
        }
    });

    // Скрытие автозаполнения при клике вне блока
    document.addEventListener('click', function (event) {
        if (!container.contains(event.target) && event.target !== input) {
            container.classList.remove('visible');
        }
    });

    // Функция отображения вариантов
    function displayDiameters(diameters) {
        container.innerHTML = ''; // Очищаем предыдущие предложения

        if (diameters.length > 0) {
            container.classList.add('visible'); // Показываем контейнер
            diameters.forEach(diameter => {
                const div = document.createElement('div');
                div.classList.add('autocomplete-brand');
                div.textContent = diameter;

                // Заполнение поля ввода при выборе варианта
                div.addEventListener('click', function () {
                    input.value = diameter;
                    container.innerHTML = '';
                    container.classList.remove('visible'); // Скрываем контейнер
                });

                container.appendChild(div);
            });
        } else {
            container.classList.remove('visible'); // Скрываем контейнер, если пусто
        }
    }
});