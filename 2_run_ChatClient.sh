clear
CLASSES="src"
java -Djava.security.manager -Djava.security.policy=chat.policy -cp $CLASSES assignment_2.ChatClient rmi://127.0.1.1:1099/ChatServer
