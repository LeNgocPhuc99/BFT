# BFT

KQ:

Node with port number 8080 has been added
Node with port number 8081 has been added
Node with port number 8082 has been added
8082[8080: test2]
8081[8080: test1]
8080[8080: test0]
8081[8080: test1, 8082: 8080: test2]
8081[8080: test1, 8082: 8080: test2, 8080: 8080: test0]
8082[8080: test2, 8081: 8080: test1]
8082[8080: test2, 8081: 8080: test1, 8080: 8080: test0]


8081[8080: test1, 8082: 8080: test2]: tin nhắn "test1" được 8080 gửi cho 8081, tin nhắn "test2" được 8080 gửi cho 8082, 8082 broadcast tin nhắn "test2" cho 8081
