# 🧩 DML — Descriptive Markup Language

DML is a simple, JSON-like descriptive language that supports variables, arithmetic operations, nested structures, lists, and maps. It was created using Kotlin, ANTLR, and the Java Virtual Machine.

Think of it as a strongly-typed, comment-friendly version of JSON — built for configuration, definition, and readability.

---

## 📦 Features

- ✅ Basic types: `string`, `number`, `boolean`, `list`, `map`, `url`, `file`, `char`
- ✅ Possibility to create class
- ✅ Arithmetic operations (e.g. `number age = 20 + 5`)
- ✅ Function: `now()` with pssibility to add and subtraction dates `d` - day; `h` - hour;`m` - minutes (e.g. `date tomorrow = now("+1d");`)
- ✅ Nested structures (e.g. `event.user.name`)
- ✅ List & Map literals
- ✅ Type checking with error reporting
- ✅ Simple CLI (`dml read file.dml`)
- ✅ Written in Kotlin + ANTLR

---

## 🚀 Installation

### 🔧 Option 1: Install via Homebrew (Recommended)

> Coming soon: Homebrew Tap to install globally

```bash
brew tap tree-software-company/dml
brew install dml
```

Then you can use it globally like:

```bash
dml -r dml test.dml
```

> ℹ️ The Homebrew formula will download the `.jar` from GitHub Releases automatically.

---

### 💻 Option 2: Manual Installation (Local Development)

#### 1. Clone the repo and build the `.jar`

```bash
git clone https://github.com/tree-software-company/dml.git
cd dml
gradle build
```

After build, you'll find your CLI JAR at:

```bash
build/libs/DML-all.jar
```

#### 2. Run the CLI

```bash
java -jar build/libs/DML-all.jar -r dml test.dml
```

#### 3. (Optional) Make it global

Create a wrapper script:

```bash
sudo nano /usr/local/bin/dml
```

Paste:

```bash
#!/bin/bash
java -jar /path/to/DML-all.jar "$@"
```

Then make it executable:

```bash
chmod +x /usr/local/bin/dml
```

Now you can run:

```bash
dml read file.dml
```

## 📄 Example DML Code

```bash
string title = "Hello";
number age = 30 + 2;
boolean isActive = true;

list numbers = [1, 2, 3, 4];
map user = { "name": "Szymon", "email": "example@example.com" };

string message = "Welcome, " + user.name;
```

Output:

```bash
Variables: {
  title=Hello,
  age=32,
  isActive=true,
  numbers=[1, 2, 3, 4],
  user={name=Szymon, email=example@example.com},
  message=Welcome, Szymon
}
```

## 📚 CLI Commands

| Command                     | Description                            |
| --------------------------- | -------------------------------------- |
| `dml -r dml FILE`           | Executes and interprets a .dml file    |
| `dml -r json FILE`          | Convert .json file for .dml file       |
| `dml -r yaml FILE`          | Convert .yaml file for .dml file       |
| `dml -r xml FILE`           | Convert .xml file for .dml file        |
| `dml -r plist FILE`         | Convert .plist file for .dml file      |
| `dml -r properties FILE`    | Convert .properties file for .dml file |
| `dml -w json FILE`          | Convert .dml file for .json file       |
| `dml -w yaml FILE`          | Convert .dml file for .yaml file       |
| `dml -w xml FILE`           | Convert .dml file for .xml file        |
| `dml -w properties FILE`    | Convert .dml file for .properties file |
| `dml -w plist FILE`         | Convert .dml file for .plist file      |
| `dml -l FILE`               | Check if .dml file will validate       |
| `dml -f FILE`               | Make .dml code more beauty             |
| `dml -e 'EXTENSION' FILE`   | Create code from terminal              |
| `dml -n FILE`               | Create a new DML file with a template  |
| `dml -h` or `dml --help`    | Shows help menu                        |
| `dml -v` or `dml --version` | Shows lang version                     |
| `dml -u` or `dml --update`  | Update DML via Homebrew                |

---

## 🛠️ Development

This project uses:

- Kotlin 1.9
- Java 17
- ANTLR 4.13
- Gradle + Shadow plugin

---

## 📜 License

This project is licensed under the [Apache 2.0 License](LICENSE).

---

## 👤 Author

Developed by [Tree](https://github.com/tree-software-company)
