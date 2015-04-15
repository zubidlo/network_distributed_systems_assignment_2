cls
dir /s /B *.java > sources.txt
SET DIR_FOR_CLASSES="./src"
SET CLASSES="./src;"
javac -classpath %CLASSES% -d %DIR_FOR_CLASSES% @sources.txt
pause
del sources.txt
