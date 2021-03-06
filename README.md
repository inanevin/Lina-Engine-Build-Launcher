## Lina Engine Build Launcher

![img](Docs/linalogo.png)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) 

This is a utility tool for generating project files from Lina Engine source code and building them. [Lina Engine Repository](https://github.com/inanevin/LinaEngine)
uses CMake as a build system. This tool simplifies the process of running CMake commands. You can use CMake from shell or CMake GUI to
generate project files for Lina Engine and build them, or you can use this Launcher for a simplified interface.

## Installation

-  You need to have JavaFX Runtime library installed on your computer & environment paths set for it. For more information visit [Java FX](https://openjfx.io/).
- Navigate to Releases tab and download the latest release binary as .jar file. Run the program directly within .jar file or via shell.

## Usage

![img](Docs/LauncherScreenshot.png)

- Select the source directory. This is the directory of your Lina Engine source repo/clone/download.
- Select the target directory (build directory) for the generated project files.
- Select your generator & desired build options.
- Hit Generate Project Files to generate the project files, or Generate and Build to generate and build the binaries afterwards.
