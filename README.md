# RDRSegmenter

- [RDRSegmenter](#rdrsegmenter)
  - [Original README](#original-readme)
  - [Download and Run](#download-and-run)
  - [Setup](#setup)
    - [Prerequisites](#prerequisites)
    - [Compilation](#compilation)
    - [Run](#run)
    - [Packaging into fat JAR](#packaging-into-fat-jar)

## Original README

[Here it is.](./Readme-original.md)

I just add extra functionality to makee
it more usable.

## Download and Run

Go to Releases tab and grab the JAR.
Then run it:

```sh
java -jar RDRsegmenter.jar ...
```

For command-line parameters, see the
[Run](#run) section below.

## Setup

### Prerequisites

- Java 1.8+ (they say that)

### Compilation

```sh
javac -encoding UTF-8 ./**/*.java
```

Or the following (the downside is it will not
update imported files had you changed them; but
it is okay if you compile the first time):

```sh
javac -encoding UTF-8 RDRsegmenter.java
```

### Run

```sh
java RDRsegmenter DIRECT "Xin chào các bạn yêu quý! Chúng tôi rất trân trọng tình cảm của các bạn..."
```

or:

```sh
java RDRsegmenter FILE /path/to/input/text/file
```

or:

```sh
echo -e "Xin chào các bạn yêu quý! Chúng tôi rất trân trọng tình cảm của các bạn...
Hôm nay mọi người có vui không ạ?
Hãy cho phép tôi được giới thiệu bản thân nhé!
\nANOTHER PARAGRAPH ISN'T IT???" \
| java RDRsegmenter STDIN
```

The result (segmented text) will be
printed to `stdout`.

Or:

```sh
java RDRsegmenter TXT-RPC 8024
```

Then you would be able to POST the
HTTP/1.1 endpoint `/v1/segment`
with the request body containing
the text to segment (in plain text)
and get back the result. **Please**
**remember to always check the returned**
**HTTP status code for errors.**

```sh
curl http://localhost:8024/v1/segment --http1.1 -X POST -H 'Content-Type: text/plain' -v -d 'Xin chào các bạn yêu quý! Chúng tôi rất trân trọng tình cảm của các bạn...
Hôm nay mọi người có vui không ạ?
Hãy cho phép tôi được giới thiệu bản thân nhé!'
```

Also note that the HTTP server implementation
is utterly simple and will not stand cyber
attacks. Protect it with WAFs or use it
internally only.

### Packaging into fat JAR

Remember to [compile](#compilation) first. Then:

```sh
jar cfm RDRsegmenter.jar MANIFEST.MF *.class ./**/*.class Model.RDR VnVocab
```

Explanation:

- `c` → create
- `f` → output to file
- `m` → include manifest

Then run it just as instructed in the
[Download and Run](#download-and-run)
section.
