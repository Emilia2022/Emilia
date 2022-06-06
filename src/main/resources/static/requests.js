const BASE_URL = './r2g'

function getTickers() {
    $.ajax({
        url: BASE_URL + '/tickers',
        method: 'GET',
        complete: function(data) {
            let tickers = JSON.parse(data.responseText).sort();
            for (i in tickers) {
                $('#tickers-select').append($('<option>', {
                    value: tickers[i],
                    text: tickers[i]
                }));
            }
            $('select').formSelect();
        }
    })
}

let cachedGifs = [];
let offset = 0;

function getGif(ticker) {
    $.ajax({
        url: BASE_URL + '/gif?ticker=' + ticker + '&offset=' + offset,
        method: 'GET',
        complete: function(data) {
            let json = JSON.parse(data.responseText);
            offset = json.pagination.offset + 1;
            let gifs = json.data;
            for (i in gifs) {
                let obj = {};
                let gif = gifs[i];
                obj.url = gif.images.original.url;
                obj.title = gif.title;
                if (cachedGifs.indexOf(obj) == -1) {
                    cachedGifs.push(obj);
                }
            }
            let random = cachedGifs[Math.floor(Math.random() * cachedGifs.length)];
            $('#gif').attr("src", random.url);
            $('#title').text(random.title);
            $('#info').text('Search query: ' + '"' + json.query + '"');
        }
    })
}