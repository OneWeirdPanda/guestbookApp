var http = require('http');
var url = require('url');

http.createServer(function(request, response) {
  request.on('error', function(err) {
    console.error(err);
    response.statusCode = 400;
    response.end();
  });
  response.on('error', function(err) {
    console.error(err);
  });
  if (request.method === 'GET') {
	console.log('HTTP GET');  
    var url_parts = url.parse(request.url, true);
	var query = url_parts.query;
	console.log(query);
	response.writeHead(200, {"Content-Type": "text/plain"});
    //request.pipe(response);
		if (query.name) {
		// user told us their name in the GET request, ex: http://localhost:8000/?name=Tom
			response.end('Hello ' + query.name + '\n');
		} else {
		response.end("Hello World\n");
		}
  } else {
    response.statusCode = 404;
    response.end();
  }
  
}).listen(8081);

// Console will print the message
console.log('Server listening');