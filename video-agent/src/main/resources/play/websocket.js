var fs = require('fs'),
	http = require('http'),
	https = require('https'),
	WebSocket = require('ws');

if (process.argv.length < 3) {
	console.log('输入正确参数');
	process.exit();
}

var stream_secret = process.argv[2] || "supersecret";
var stream_port = process.argv[3] || 8081;
var websocket_port = process.argv[4] || 8082;
var record_stream = false;
var totalSize = 0;



function initWebSocket(websocket_port) {
	var clientMap = new Map();
	var sslConfig = JSON.parse(fs.readFileSync("src/main/resources/ssl.json"));
	var socketServer = null;
	
	if(sslConfig["ws.ssl"] == true){
		const server = https.createServer({
			cert: fs.readFileSync(sslConfig["ws.ssl.cert"]),
			key: fs.readFileSync(sslConfig["ws.ssl.key"])
		}, function(req, res) {
			res.end("Video play Service!\n");
		}).listen(websocket_port);
		
		socketServer = new WebSocket.Server({
			server
		});
	}else{
		socketServer = new WebSocket.Server({
			port : websocket_port,
			perMessageDeflate : false
		});
	}
	
	socketServer.on('connection', function(socket, upgradeReq) {

		var url = upgradeReq.socket.remoteAddress + upgradeReq.url;
		var key = url.substr(1).split('/')[1];
		var clients = clientMap.get(key);
		if (!clients) {
			clients = new Set();
			clientMap.set(key, clients);
		}
		clients.add(socket);
		totalSize++;
		socket.on('close', function(code, message) {
			var clientSet = clientMap.get(key);
			if (clientSet) {
				clientSet.delete(socket);
				totalSize--;
				if (clientSet.size == 0) {
					clientMap.delete(key);
					console.log("delete:" + key);
				}
			}
		});
	});

	socketServer.broadcast = function(data, theme) {
		var clients = clientMap.get(theme);
		if (!clients) {
			return;
		}
		clients.forEach(function(client, set) {
			if (client.readyState === WebSocket.OPEN) {
				client.send(data);
			}
		});
	};

	return socketServer;
}

function initHttp(stream_port, stream_secret, record_stream, socketServer) {
	var streamServer = http.createServer(
		function(request, response) {
			var params = request.url.substr(1).split('/');
			if (params.length != 2) {
				console.log("[ERROR]:Incorrect parameters, enter password and push theme");
				response.end();
			}
			if (params[0] !== stream_secret) {
				console.log("[ERROR]:Password error: " + request.socket.remoteAddress + ":" + request.socket.remotePort);
				response.end();
			}
			response.connection.setTimeout(0);
			request.on('data', function(data) {
				socketServer.broadcast(data, params[1]);
				if (request.socket.recording) {
					request.socket.recording.write(data);
				}
			});
			request.on('end', function() {
				console.log("[INFO]:close request");
				if (request.socket.recording) {
					request.socket.recording.close();
				}
			});
			if (record_stream) {
				var path = 'recordings/' + Date.now() + '.ts';
				request.socket.recording = fs.createWriteStream(path);
			}
		}).listen(stream_port);
	console.log('Started Video Service :secret is [%s],http Port is [%s], ws port is [%s].', stream_secret,
		stream_port, websocket_port);
}

initHttp(stream_port, stream_secret, record_stream, initWebSocket(websocket_port));
