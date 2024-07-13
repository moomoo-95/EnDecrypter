#!/bin/sh
JAR_PATH=target/moomoo_endecrypter-jar-with-dependencies.jar
MAIN_CLASS=com.moomoo.endecrypter.EnDecrypterMain

#Options
INPUT=sahljdhjskah
INPUT_TYPE=text
KEY=moomooProject
MODE=encrypt
#OUTPUT="result.key"
OPTIONS="-i $INPUT -it $INPUT_TYPE -k $KEY -m $MODE"
#OPTIONS="-i $INPUT -it $INPUT_TYPE -k $KEY -m $MODE -o $OUTPUT"

checkfile()
{
	if [ ! -e $1 ]; then
		echo "$1" file does not exist.
		exit 2
	fi
}

checkfile $JAR_PATH
/Library/Java/JavaVirtualMachines/jdk-11.0.11.jdk/Contents/Home/bin/java -classpath $JAR_PATH $MAIN_CLASS $OPTIONS
#/usr/bin/java -classpath $JAR_PATH $MAIN_CLASS $JAVA_OPT -i $1