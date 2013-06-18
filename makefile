# super simple makefile
# call it using 'make NAME=name_of_code_file_without_extension'
# (assumes a .java extension)
NAME = "Draw"

# HACK: myShape.java and vecmath are compiled regardless if needed
all:
	@echo "Compiling..."
	javac -cp vecmath-1.5.1.jar $(NAME).java MyShape.java Origin.java Shape.java

# HACK: adding vecmath to classpath regardless if needed
# Windows users: you may have to change the colon to a semi-colon in the 
# class path param below. Make sure to change it back, so your submission
# will work on the VM
run: all
	@echo "Running..."
	java -cp "vecmath-1.5.1.jar:." $(NAME)
