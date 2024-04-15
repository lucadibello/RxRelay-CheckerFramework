setup:
	@echo "Setting up the Java environment"
	@./scripts/setup.sh
	@echo "Java environment setup complete."

build:
	@echo "Building the Java project"
	@./scripts/build.h
	@echo "Java project build complete."

compile:
	@echo "Checking the Java project"
	@bash ./scripts/compile.sh
	@echo "Java project check complete."
