.PHONY:setup
setup:
	@echo "Setting up the Java environment..."
	@./scripts/setup.sh
	@echo "Java environment setup complete."

.PHONY:build
build:
	@echo "Building project..."
	@./scripts/build.sh
	@echo "Java project build complete."

.PHONY:compile
compile:
	@echo "Compiling project..."
	@bash ./scripts/compile.sh
	@echo "Java project check complete."

.PHONY:test
test: compile
	@echo "Running tests..."
	@./scripts/test.sh
	@echo "Java project tests complete."
