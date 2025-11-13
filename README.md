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

### Packaging into fat JAR

Remember to [compile](#compilation) first. Then:

```sh
jar cfm RDRsegmenter.jar MANIFEST.MF *.class Model.RDR VnVocab
```

Explanation:

- `c` → create
- `f` → output to file
- `m` → include manifest
