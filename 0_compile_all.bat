cls
dir /s /B *.java > sources.txt
md bin
SET DIR_FOR_CLASSES="./bin"
SET CLASSES="./bin;"
javac -classpath %CLASSES% -d %DIR_FOR_CLASSES% @sources.txt
pause
del sources.txt
