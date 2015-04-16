clear
DIR_FOR_CLASSES="src"
CLASSES="src"

find -type f -name "*.java" > sources.txt
javac -classpath $CLASSES -d $DIR_FOR_CLASSES @sources.txt
rm sources.txt
