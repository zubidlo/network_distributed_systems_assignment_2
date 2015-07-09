clear
find -name "*.java" > sources.txt
DIR_FOR_CLASSES="src"
CLASSES="src"
javac -classpath $CLASSES -d $DIR_FOR_CLASSES @sources.txt
rm ./sources.txt
