# 🧩 DML — Descriptive Markup Language

DML is a simple, JSON-like descriptive language that supports variables, arithmetic operations, nested structures, lists, and maps. It was created using Kotlin, ANTLR, and the Java Virtual Machine.

Think of it as a strongly-typed, comment-friendly version of JSON — built for configuration, definition, and readability.

---

## 📦 Features

- ✅ Basic types: `string`, `number`, `boolean`, `list`, `map`, `url`, `file`, `char`
- ✅ Possibility to create class
- ✅ Arithmetic operations (e.g. `number age = 20 + 5`)
- ✅ Function: `now()` with pssibility to add and subtraction dates `d` - day; `h` - hour;`m` - minutes (e.g. `date tomorrow = now("+1d");`)
- ✅ Configuration files: `loadEnv(".env")` and `env("KEY")` read values from a `.env` file
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

> ℹ️ The Homebrew formula downloads the `.jar` from GitHub Releases, verifies its SHA-256 checksum and runs it with the Homebrew-managed `openjdk@17`.

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
exec /path/to/jdk-17/bin/java -jar /path/to/DML-all.jar "$@"
```

Then make it executable:

```bash
chmod +x /usr/local/bin/dml
```

Now you can run:

```bash
dml read file.dml
```

#### 4. Verify a downloaded `.jar`

A `.jar` downloaded from GitHub Releases runs with the full permissions of your user. Compare its checksum with the `sha256` pinned in the [Homebrew formula](https://github.com/tree-software-company/homebrew-dml/blob/main/dml.rb) before running it:

```bash
shasum -a 256 DML-all.jar
```

Do not run a `.jar` whose checksum differs.

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

## 🔐 Configuration Files (.env)

A DML program can read values from a `.env` file that lies in the project directory (the directory of the executed `.dml` file or below it).

`.env`:

```bash
DB_HOST=localhost
export DB_PORT="5432"
API_KEY=top-secret # inline comment
```

`app.dml`:

```bash
loadEnv(".env");

string host = env("DB_HOST");
string port = env("DB_PORT", "1111");
string region = env("REGION", "eu-west-1");
private string apiKey = env("API_KEY");
```

- `loadEnv(path)` loads a `.env` file. Calling it again merges the files, later keys win. The path is relative to the file that calls it and must stay inside the project directory; traversal, absolute paths and symlinks leading outside are rejected.
- `env("KEY")` returns the value as a `string` and fails with an error naming the key when it is not defined.
- `env("KEY", default)` returns `default` when the key is not defined; the default can be of any type.
- Supported syntax: `KEY=value`, optional `export` prefix, `"double quoted"` values with `\n`, `\t`, `\"` and `\\` escapes, `'single quoted'` literal values, `#` comments on their own line or after a space. Variables such as `${HOME}` are not expanded.
- Only values loaded with `loadEnv` are visible. Environment variables of the process are not exposed to DML programs.
- Limits: the file may not exceed 10 MiB, 1000 keys and 10000 characters per value. Error messages name the key or line, never the value.
- Values are always strings, so `number port = env("PORT")` is a type error. The CLI prints every variable that is not `private`; declare secrets as `private`.
- `loadEnv` works only when running a file (`dml -r dml file.dml`), not for inline code.

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
