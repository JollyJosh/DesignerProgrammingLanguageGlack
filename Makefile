main : all

run :
	java -jar bin/Environment.jar

clean :
	rm -rf bin/*
	rm -f evaluator
	rm -f compile

bin :
	mkdir -p bin

compile :
	javac -cp bin -d bin src/me/jjcollins2/glack/*.java
	touch compile

evaluator : compile
	cd bin && jar cmvf ../manifests/evaluator.manifest Evaluator.jar *
	touch evaluator

error1 :
	cat testFiles/error1.glk

error1x : evaluator
	./dpl testFiles/error1.glk

error2 :
	cat testFiles/error2.glk

error2x : evaluator
	./dpl testFiles/error2.glk

error3 :
	cat testFiles/error3.glk

error3x : evaluator
	./dpl testFiles/error3.glk

error4 :
	cat testFiles/error4.glk

error4x : evaluator
	./dpl testFiles/error4.glk

error5 :
	cat testFiles/error5.glk

error5x : evaluator 
	./dpl testFiles/error5.glk

arrays :
	cat testFiles/arrays.glk

arraysx : evaluator
	./dpl testFiles/arrays.glk

conditionals :
	cat testFiles/conditionals.glk

conditionalsx : evaluator
	./dpl testFiles/conditionals.glk

recursion :
	cat testFiles/recursion.glk

recursionx : evaluator
	./dpl testFiles/recursion.glk

iteration :
	cat testFiles/iteration.glk

iterationx :
	./dpl testFiles/iteration.glk

functions :
	cat testFiles/functions.glk

functionsx : evaluator
	./dpl testFiles/functions.glk

lambda :
	cat testFiles/lambda.glk

lambdax : evaluator
	./dpl testFiles/lambda.glk

dictionary :
	cat testFiles/dictionary.glk

dictionaryx : evaluator
	./dpl testFiles/dictionary.glk

problem :
	cat testFiles/sieve.glk

problemx : evaluator
	./dpl testFiles/sieve.glk

extra :
	cat testFiles/extra.glk

extrax : evaluator
	./dpl testFiles/extra.glk

all : evaluator

