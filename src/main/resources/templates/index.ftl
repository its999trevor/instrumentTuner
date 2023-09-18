<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pitch Detection Web App</title>
</head>
<body>
    <h1>Pitch Detection</h1>
    <button class="btn1" onclick="startPitchDetection()">Start Detection</button>
    <button class="btn2">Stop Detection</button>
    <div id="noteDisplay">Note: <h1 class="txt"></h1></div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script>
      const socket = new SockJS('http://localhost:8080/your-websocket-endpoint'); // Replace with your WebSocket endpoint URL
      let stompClient = Stomp.over(socket);
      let val=document.querySelector(".txt");
      let container=document.querySelector("#noteDisplay");
    
        // Connect to the WebSocket
        stompClient.connect({}, () => {
            console.log('WebSocket connection established.');

            // Subscribe to the /topic/note endpoint
            stompClient.subscribe('/topic/note', (message) => {
              console.log(message);
                let note = JSON.stringify(message.body);

                // Handle the received note
                console.log(note);
                
                let noty=JSON.parse(note);
                console.log(noty);
              
              val.innerHTML=noty;
                
                // You can update your UI or perform any other actions here
                  
                });
              });

        function startPitchDetection() {
            // Send a request to start pitch detection on the server
            fetch('/startDetection', {
                method: 'POST'
            })
            .then(response => {
                if (response.status === 200) {
                    console.log('Pitch detection started.');
                } else {
                    console.error('Failed to start pitch detection.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        }

        // Rest of your JavaScript code...
    </script>
</body>
</html>
