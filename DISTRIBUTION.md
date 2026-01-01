# SoupModMaker2 Distribution Guide

## Quick Start for Users

### Running the Application

**Option 1: Double-click (GUI systems)**
Simply double-click `soupmodmaker2-0.1.0-SNAPSHOT.jar` and it will run the demo.

**Option 2: Command Line (recommended)**
```bash
# Run the demo
java -jar soupmodmaker2-0.1.0-SNAPSHOT.jar

# Generate from example workspace
java -jar soupmodmaker2-0.1.0-SNAPSHOT.jar --full-demo
```

### Requirements

- **Java 17 or higher** required
- Check your Java version: `java -version`
- [Download Java](https://adoptium.net/) if needed

### What It Does

When you run SoupModMaker2, it will:

1. **Demonstrate workspace creation** - Shows how mod projects are structured
2. **Generate clean code** - Displays generated Java code (better than MCreator!)
3. **Create a complete Forge 1.12.2 mod** - Generates a full mod project in `generated/testmod/`

### Using the Generated Mod

After running SoupModMaker2:

```bash
# Navigate to generated mod
cd generated/testmod

# Build the Forge mod
gradle build

# Your mod JAR will be in:
# build/libs/testmod-1.0.0.jar
```

You can then install this JAR in your Minecraft mods folder!

---

## For Developers

### Building from Source

```bash
# Clone repository
git clone https://github.com/GuyThatLivesAndCodes/SoupModMaker2.git
cd SoupModMaker2

# Build (creates executable JAR)
gradle build

# JAR will be in: build/libs/soupmodmaker2-0.1.0-SNAPSHOT.jar
```

### Building the Fat JAR (Recommended)

```bash
# Build fat JAR with all dependencies
gradle fatJar

# Creates: build/libs/soupmodmaker2-all-0.1.0-SNAPSHOT.jar
# This JAR is fully self-contained and can run anywhere!
```

### Distribution

The fat JAR includes:
- ✅ All application code
- ✅ FreeMarker templates
- ✅ Gson for JSON
- ✅ SLF4J logging
- ✅ All dependencies bundled
- ✅ Executable manifest

**This single JAR is all users need!**

---

## File Types Explained

### JAR Files

**What is a JAR?**
- Java ARchive - a ZIP file containing Java bytecode and resources
- Standard distribution format for Java applications
- Can be executed with `java -jar filename.jar`

**Are JAR files safe?**
- Yes! JAR files are the standard way to distribute Java software
- They're used by millions of applications (Minecraft itself is a JAR!)
- Users can inspect contents with any ZIP tool
- Open source = code is auditable

**Why won't it open when double-clicked?**

Most systems need Java configured to run JARs:

**Windows:**
1. Right-click JAR → "Open with" → "Java(TM) Platform SE binary"
2. Check "Always use this app"

**macOS/Linux:**
- May need to make executable: `chmod +x soupmodmaker2-*.jar`
- Or always use command line: `java -jar soupmodmaker2-*.jar`

**Best practice:** Provide users with a startup script!

---

## Startup Scripts

### Windows (startup.bat)

```batch
@echo off
echo Starting SoupModMaker2...
java -jar soupmodmaker2-0.1.0-SNAPSHOT.jar %*
pause
```

### macOS/Linux (startup.sh)

```bash
#!/bin/bash
echo "Starting SoupModMaker2..."
java -jar soupmodmaker2-0.1.0-SNAPSHOT.jar "$@"
```

Make executable:
```bash
chmod +x startup.sh
./startup.sh
```

---

## Troubleshooting

### "Java not found"
- Install Java 17+: https://adoptium.net/
- Verify: `java -version`

### "No main manifest attribute"
- You're using the wrong JAR
- Use the fat JAR: `gradle fatJar`
- File: `build/libs/soupmodmaker2-all-0.1.0-SNAPSHOT.jar`

### "Class not found"
- Dependencies not bundled
- Build with: `gradle fatJar` (not just `gradle jar`)

### Double-click doesn't work
- Use command line: `java -jar soupmodmaker2-*.jar`
- Or use the startup scripts above

---

## Release Checklist

When creating a release:

1. ✅ Build fat JAR: `gradle fatJar`
2. ✅ Test JAR: `java -jar build/libs/soupmodmaker2-all-*.jar`
3. ✅ Include startup scripts (Windows .bat, Unix .sh)
4. ✅ Include this README
5. ✅ Tag release with version number
6. ✅ Upload to GitHub Releases

---

## GitHub Actions Artifacts

Every successful build uploads artifacts:

- **soupmodmaker2-build** - The executable JAR
- **generated-testmod** - Sample generated mod (from workflow)

Download from the Actions tab → Latest successful run → Artifacts

---

**SoupModMaker2** - The better Minecraft mod maker!
