<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Pitch Detection Web App</title>
</head>

<body>
    <h1>Pitch Detection</h1>
    <button class="btn1">Start Detection</button>
    <button class="btn2">Stop Detection</button>
    <div id="noteDisplay">Note: <h1 class="txt"></h1>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.2/sockjs.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
    <script>
        const socket = new SockJS('http://localhost:8080/your-websocket-endpoint'); // Replace with your WebSocket endpoint URL
        var stompClient = Stomp.over(socket);
       
        let val = document.querySelector(".txt");
        let container = document.querySelector("#noteDisplay");
       
        stompClient.connect({}, () => {
                            console.log('WebSocket connection established.');

                            // Subscribe to the /topic/note endpoint
                            stompClient.subscribe('/topic/note', (message) => {
                                console.log(message);
                                let note = JSON.stringify(message.body);

                                // Handle the received note
                                console.log(note);


                                let noty = JSON.parse(note);
                                
                                console.log(noty);  
                                startDetection(noty);
                                

                            });
                        });
        

        let btn1 = document.querySelector(".btn1");
        btn1.addEventListener("click",startDetection);
        function startDetection(ev) {
         fetch('/startDetection', {
            method: 'POST'
        })
        .then(response => {
            if (response.status === 200) {
                console.log('Pitch detection started.');
                console.log(response);
                Handle(ev);
                
            } else {
                console.error('Failed to start pitch detection.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
        }

        let btn2 = document.querySelector(".btn2");
        btn2.addEventListener("click", stopDetection);

        function stopDetection() {
            // Make an AJAX POST request to /stopDetection endpoint
            fetch('/stopDetection', {
                method: 'POST'
            })
                .then(response => {
                    if (response.status === 200) {
                        console.log('Pitch detection stopped.');
                        // stompClient.disconnect();
                        // You can add additional code here to update the UI or take other actions
                    } else {
                        console.error('Failed to stop pitch detection.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        }
        function Handle(value){
            val.innerHTML = value;
        }
    </script>
</body>

</html>