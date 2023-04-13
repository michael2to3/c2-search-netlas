# NetAtlas C2 Server Search

![Build Status](https://img.shields.io/github/actions/workflow/status/michael2to3/c2-search-netlas/build.yml?branch=main)
[![License](https://img.shields.io/github/license/michael2to3/pretty-caldav-politech-schedule?style=flat-square)](https://github.com/michael2to3/pretty-caldav-politech-schedule/blob/main/LICENSE)
[![Junit Jupiter](https://img.shields.io/badge/Junit-Jupiter-green?style=flat-square)](https://junit.org/junit5/)
[![Gradle](https://img.shields.io/badge/Gradle-blue?style=flat-square)](https://gradle.org/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

C2 Search Netlas is a Java utility to detect C2 servers using Netlas API. This utility provides a simple and easy-to-use CLI interface for searching C2 servers. It uses the Netlas API to fetch data and process it locally.

[![asciicast](https://asciinema.org/a/J0NLL69CVKG1PUlw9Mkyf8lZw.svg)](https://asciinema.org/a/J0NLL69CVKG1PUlw9Mkyf8lZw)

## Installation

Clone this repository to your local machine:

```bash
git clone https://github.com/michael2to3/c2-search-netlas.git
```

## Usage

To use the terminal utility, you need to have a Netlas API key. You can get the key from the [Netlas](https://netlas.io) website.

Once you have the API key, you can use the following command to search for C2 servers:

```bash
c2detect -t <TARGET_DOMAIN> -p <TARGET_PORT> -s <API_KEY> [-v]
```

Replace `<TARGET_DOMAIN>` with the IP address or domain you want to search for C2 servers on, `<TARGET_PORT>` with the port you want to scan, and `<API_KEY>` with your Netlas API key.

The `-v` option is optional and can be used to print verbose output.

Here's an example command to search for C2 servers on IP address `google.com` on port `443` using the Netlas API key 1234567890abcdef:

```bash
c2detect -t google.com -p 443 -s 1234567890abcdef
```

### Release

If a release of the utility is available, you can download it from the releases page on GitHub. Follow these steps:
- Go to the releases page of the repository.
- Download the latest release file (usually a JAR file) to your local machine.
- Create a config.properties file in the same directory as the JAR file and add the following line:
```bash
api.key=<your-netlas-api-key>
```
- Replace `<your-netlas-api-key>` with your actual Netlas API key.
- Open a terminal and navigate to the directory where the JAR file and config.properties file are located.
- Run the following command to start the utility:
```bash
java -jar c2-search-netlas-<version>.jar -t <ip-or-domain> -p <port>
```
- Replace `<version>` with the version number of the release you downloaded, `<ip-or-domain>` with the IP address or domain you want to search for C2 servers on, and `<port>` with the port you want to scan.

### Docker compose

If you have Docker Compose installed on your machine, you can build and run the utility using the following steps:
- Clone the repository to your local machine:
```bash
git clone https://github.com/michael2to3/c2-search-netlas.git
```
- Navigate to the repository directory:
```bash
cd c2-search-netlas
```
- Create a `config.properties` file in the root directory of the project and add the following line:
```bash
api.key=<your-netlas-api-key>
```
- Replace `<your-netlas-api-key>` with your actual Netlas API key.
- Open the `.env` file and replace the value of the API_KEY environment variable with your actual Netlas API key.
- Run the following command to build and start the Docker containers:
```bash
docker-compose up --build
```
- Open another terminal and run the following command to search for C2 servers:
```bash
docker-compose exec c2detect c2detect -t <ip-or-domain> -p <port>
```
- Replace `<ip-or-domain>` with the IP address or domain you want to search for C2 servers on, and `<port>` with the port you want to scan.


### Docker

To build and start the Docker container for this project, you can run the following commands:

```bash
docker build -t <image-name> .
docker run -p 8080:8080 <image-name>
```

Replace `<image-name>` with the name you want to give your Docker image. This will build the Docker image and start the container. You can then access the application by navigating to http://localhost:8080 in your web browser.

### Source
To use this utility, you need to have a Netlas API key. You can get the key from the [Netlas](https://netlas.io/) website.

Once you have the API key, create a `config.properties` file in the root directory of the project and add the following line:

```bash
api.key=<your-netlas-api-key>
```

Now you can build the project and run it using the following commands:

```bash
./gradlew build
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar --help
```

This will display the help message with available options.

To search for C2 servers, run the following command:

```bash
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar -t <ip-or-domain> -p <port>
```

This will display a list of C2 servers found in the given IP address or domain.

## Support

| Name                    | Support            |
|-------------------------|--------------------|
| Metasploit              | :white_check_mark: |
| Havoc                   | :question:         |
| Cobalt Strike           | :white_check_mark: |
| Bruteratel              | :white_check_mark: |
| Sliver                  | :white_check_mark: |
| DeimosC2                | :white_check_mark: |
| PhoenixC2               | :white_check_mark: |
| Empire                  | :x:                |
| Merlin                  | :x:                |
| Covenant                | :x:                |
| Villain                 | :x:                |
| Shad0w                  | :x:                |
| PoshC2                  | :x:                |

Legend:

- :white_check_mark: - Accept/good support
- :question: - Support unknown/unclear
- :x: - No support/poor support

## Contributing

If you want to contribute to this project, please feel free to create a pull request.

## License

This project is licensed under the License - see the [LICENSE](https://github.com/michael2to3/c2-search-netlas/blob/main/LICENSE) file for details.
