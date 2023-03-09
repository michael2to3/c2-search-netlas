# NetAtlas C2 Server Search

![Build Status](https://img.shields.io/github/actions/workflow/status/michael2to3/c2-search-netlas/build.yml?branch=main)
[![Coverage Status](https://img.shields.io/codecov/c/github/michael2to3/c2-search-netlas?style=flat-square)](https://codecov.io/gh/michael2to3/c2-search-netlas)
[![License](https://img.shields.io/github/license/michael2to3/pretty-caldav-politech-schedule?style=flat-square)](https://github.com/michael2to3/pretty-caldav-politech-schedule/blob/main/LICENSE)
[![Junit Jupiter](https://img.shields.io/badge/Junit-Jupiter-green?style=flat-square)](https://junit.org/junit5/)
[![Gradle](https://img.shields.io/badge/Gradle-blue?style=flat-square)](https://gradle.org/)

This tool is designed to search for Command and Control (C2) servers using NetAtlas, a web-based platform for mapping and visualizing internet assets. It leverages the NetAtlas API to identify potentially malicious C2 servers and provide information about them.

## Getting Started
### Prerequisites

To use this tool, you will need Java 8 or higher and Gradle installed on your system. You can download Gradle from the following link:

```bash
https://gradle.org/install/
```

### Installing

To install the tool, simply clone the GitHub repository:

```bash
git clone https://github.com/michael2to3/c2-search-netlas
```
Then navigate to the project directory and build the project using Gradle:

```bash
cd c2-search-netlas
gradle build
```

### Usage

To use the tool, run the NetAtlasC2Search.java class with the following command:

```bash
java -cp build/libs/netatlas-c2-search-java-gradle-1.0-SNAPSHOT.jar NetAtlasC2Search <your-api-key> <your-org-id>
```

Replace `<your-api-key>` and `<your-org-id>` with your NetAtlas API key and organization ID, respectively. These can be obtained from the NetAtlas web interface.

The tool will search for C2 servers within your organization's IP range and output a list of potential C2 servers, along with information about them such as the hosting provider and country.
### Contributing

Contributions are welcome! If you find a bug or would like to suggest a new feature, please open an issue or submit a pull request.
### License

This project is licensed under the License - see the LICENSE.md file for details.
### Acknowledgments

This tool was inspired by the NetAtlas platform and the need for a simple and effective way to identify potentially malicious C2 servers.
