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

let urls = [];
var offset = 0;

function getGif(ticker) {
    $.ajax({
        url: BASE_URL + '/gif?ticker=' + ticker + '&offset=' + offset,
        method: 'GET',
        complete: function(data) {
            let json = JSON.parse(data.responseText);
            offset = json.pagination.offset + 1;
            console.log(json);
            let gifs = json.data;
            for (i in gifs) {
                let url = gifs[i].images.original.url;
                if (urls.indexOf(url) == -1) {
                    urls.push(url);
                }
            }
            let url = urls[Math.floor(Math.random() * urls.length)];
            $('#gif').attr("src", url);
        }
    })
}