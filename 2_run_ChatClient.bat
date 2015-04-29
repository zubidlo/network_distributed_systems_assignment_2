cls
SET CLASSES="./src;"
java -Djava.security.manager -Djava.security.policy=chat.policy -cp %CLASSES% assignment_2.ChatClient rmi://192.168.192.17:1099/ChatServer