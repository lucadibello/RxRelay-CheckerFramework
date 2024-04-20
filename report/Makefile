.PHONY: build
build:
	@echo "Building LaTeX document..."
	@pdflatex -interaction=nonstopmode -halt-on-error checkerframework_report.tex
	@bibtex checkerframework_report.aux
	@pdflatex -interaction=nonstopmode -halt-on-error checkerframework_report.tex
	@pdflatex -interaction=nonstopmode -halt-on-error checkerframework_report.tex
	@echo "Done building LaTeX document."
