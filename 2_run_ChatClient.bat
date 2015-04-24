cls
SET CLASSES="./src;"
java -Djava.security.manager -Djava.security.policy=chat.policy -cp %CLASSES% assignment_2.ChatClient 10.7.65.98 1099 ChatServer