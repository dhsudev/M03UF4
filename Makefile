# Define variables
SRC_DIR := src
BUILD_DIR := game
BIN_DIR := bin
JAVAC := javac
JAVA := java

# Find all Java source files in the src directory
SOURCES := $(SRC_DIR)/MazeGame.java $(SRC_DIR)/MazeChars.java $(SRC_DIR)/Validador.java

# Generate .class files from Java source files
CLASSES := $(patsubst $(SRC_DIR)/%.java,$(BUILD_DIR)/%.class,$(SOURCES))

# Define the main class
MAIN_CLASS := MazeGame

# Define the name of the output JAR file
JAR_FILE := Maze.jar

# Default target
.PHONY: all
all: $(CLASSES) | $(BIN_DIR)

# Compile Java source files to .class files
$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java | $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $<

# Create build directory if it doesn't exist
$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

# Create bin directory if it doesn't exist
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Create JAR file
#$(BIN_DIR)/$(JAR_FILE): $(CLASSES) | $(BIN_DIR)
#	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

# Clean generated files
.PHONY: clean re
clean:
	rm -rf $(BUILD_DIR) $(BIN_DIR)
re: clean all
