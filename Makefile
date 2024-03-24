# Define the compiler
JAVAC = javac

# Define flags for the compiler
JAVACFLAGS = -d bin -sourcepath src

# Define the source and output directories
SRC_DIR = src
BIN_DIR = bin

# Define the list of Java source files
JAVA_FILES = $(wildcard $(SRC_DIR)/*.java)

# Define the list of class files to be generated
CLASS_FILES = $(patsubst $(SRC_DIR)/%.java,$(BIN_DIR)/%.class,$(JAVA_FILES))

# Default target to compile all Java source files
all: $(CLASS_FILES)

# Rule to compile Java source files into class files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java Makefile
	@mkdir -p $(BIN_DIR)
	@$(JAVAC) $(JAVACFLAGS) $<
	@echo "🛠  Compilant $< ..."

# Define the run target to execute the Java program with user-defined arguments
run: all
	@echo "✅ Iniciant joc..."
	@java -cp $(BIN_DIR) MazeGame $(ARGS)

# Clean up generated class files
clean:
	rm -rf $(BIN_DIR)
# Clean and recompile all 
re: clean all

.PHONY: clean re run