const URL = './r2g'

function getTickers() {
    $.ajax({
        url: URL + '/tickers',
        method: 'GET',
        complete: function (data) {
           let tickers = JSON.parse(data.responseText).sort();
           for(i in tickers){
               $('#tickers-select').append($('<option>', {
                      value: tickers[i],
                      text : tickers[i]
                  }));
            }
        }
    })
}