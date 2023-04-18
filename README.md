# NetAtlas C2 Server Search

![Build Status](https://img.shields.io/github/actions/workflow/status/michael2to3/c2-search-netlas/build.yml?branch=main)
[![License](https://img.shields.io/github/license/michael2to3/pretty-caldav-politech-schedule?style=flat-square)](https://github.com/michael2to3/pretty-caldav-politech-schedule/blob/main/LICENSE)
[![Junit Jupiter](https://img.shields.io/badge/Junit-Jupiter-green?style=flat-square)](https://junit.org/junit5/)
[![Gradle](https://img.shields.io/badge/Gradle-blue?style=flat-square)](https://gradle.org/)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/6d3c2810e4f844fa989a987f84565b7d)](https://app.codacy.com/gh/michael2to3/c2-search-netlas/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

C2 Search Netlas is a Java utility designed to detect Command and Control (C2) servers using the Netlas API. It provides a straightforward and user-friendly CLI interface for searching C2 servers, leveraging the Netlas API to gather data and process it locally.

[![asciicast](https://asciinema.org/a/Q0g0ego8SK97elJvTHN5IXLzs.svg)](https://asciinema.org/a/Q0g0ego8SK97elJvTHN5IXLzs)

## Usage

To utilize this terminal utility, you'll need a Netlas API key. Obtain your key from the [Netlas](https://netlas.io) website.

After acquiring your API key, execute the following command to search servers:

```bash
c2detect -t <TARGET_DOMAIN> -p <TARGET_PORT> -s <API_KEY> [-v]
```
Replace `<TARGET_DOMAIN>` with the desired IP address or domain, `<TARGET_PORT>` with the port you wish to scan, and `<API_KEY>` with your Netlas API key.
Use the optional `-v` flag for verbose output.
For example, to search at the `google.com` IP address on port `443` using the Netlas API key `1234567890abcdef`, enter:
```bash
c2detect -t google.com -p 443 -s 1234567890abcdef
```

## Release
To download a release of the utility, follow these steps:

- Visit the repository's releases page on GitHub.
- Download the latest release file (typically a JAR file) to your local machine.
- In a terminal, navigate to the directory containing the JAR file.
- Execute the following command to initiate the utility:
```bash
java -jar c2-search-netlas-<version>.jar -t <ip-or-domain> -p <port> -s <your-netlas-api-key>
```

## Docker
To build and start the Docker container for this project, run the following commands:
```bash
docker build -t c2detect .
docker run -it --rm \
  c2detect \
  -s "your_api_key" \
  -t "your_target_domain" \
  -p "your_target_port" \
  -v
```

## Source

To use this utility, you need to have a Netlas API key. You can get the key from the Netlas website.
Now you can build the project and run it using the following commands:
```bash
./gradlew build
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar --help
```
This will display the help message with available options.
To search for C2 servers, run the following command:
```bash
java -jar app/build/libs/c2-search-netlas-1.0-SNAPSHOT.jar -t <ip-or-domain> -p <port> -s <your-netlas-api-key>
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
| Villain                 | :white_check_mark: |
| Shad0w                  | :x:                |
| PoshC2                  | :x:                |

Legend:

- :white_check_mark: - Accept/good support
- :question: - Support unknown/unclear
- :x: - No support/poor support

## Contributing

If you'd like to contribute to this project, please feel free to create a pull request.

## License

This project is licensed under the License - see the [LICENSE](https://github.com/michael2to3/c2-search-netlas/blob/main/LICENSE) file for details.


