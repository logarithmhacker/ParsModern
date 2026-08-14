# Quick Setup Guide

## ⚡ TL;DR (Fast Setup)

```bash
# 1. Extract files
unzip ParsModernPvP_Fixed.zip
cd ParsModernPvP_Fixed

# 2. Build
./gradlew build

# 3. Done! JAR is at: build/libs/parsmodernpvp-21.0.0.jar
```

---

## 🔍 Detailed Setup

### Step 1: Verify Java Installation
```bash
java -version
# Should show Java 25 or higher
```

If you don't have Java 25, download it:
- [AdoptOpenJDK](https://adoptopenjdk.net/)
- [Temurin](https://adoptium.net/)
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)

### Step 2: Extract & Navigate
```bash
unzip ParsModernPvP_Fixed.zip
cd ParsModernPvP_Fixed
```

### Step 3: Clean Build (Recommended First Time)
```bash
./gradlew clean build -x test
```

**On Windows:**
```bash
gradlew.bat clean build -x test
```

### Step 4: What Gradle Just Did
- Downloaded Gradle 9.4.0
- Downloaded Minecraft 1.26.1 mappings
- Downloaded Fabric Loader & API
- Compiled all Java source files
- Applied Mixin transformations
- Packaged everything into a JAR

### Step 5: Find Your JAR
```bash
build/libs/parsmodernpvp-21.0.0.jar
```

Copy this to your `.minecraft/mods` folder

---

## 🛠️ IDE Setup

### IntelliJ IDEA
```bash
# Generate IntelliJ config
./gradlew genSources idea

# Then open the project in IntelliJ
# File → Open → Select project folder
```

### Eclipse
```bash
# Generate Eclipse config
./gradlew genSources eclipse

# Then import into Eclipse
# File → Import → Existing Projects into Workspace
```

### VS Code
```bash
# Just open the folder, it should work!
code .
```

---

## ✅ Verify Build Success

After `./gradlew build` completes, you should see:

```
BUILD SUCCESSFUL in Xs

16 actionable tasks: 16 executed
```

And the JAR file exists:
```bash
ls -lh build/libs/parsmodernpvp-21.0.0.jar
```

---

## 🐛 Troubleshooting

### "Java version not correct"
```bash
# Check what Java Gradle is using
./gradlew --version

# If wrong version, set JAVA_HOME
export JAVA_HOME=/path/to/java25
# or on Windows:
set JAVA_HOME=C:\Path\To\Java25
```

### Build fails with "cannot find symbol"
```bash
# Clean and regenerate
./gradlew clean
./gradlew build --refresh-dependencies
```

### Gradle hangs or is slow
```bash
# Edit gradle.properties and ensure:
org.gradle.parallel=true
org.gradle.workers.max=4
```

### Mixin errors
```bash
# Make sure you have the right Loom version
# Check build.gradle line 2:
# id 'fabric-loom' version '1.15'
```

---

## 📦 What's Included

| File | Purpose |
|------|---------|
| `build.gradle` | Main build configuration (FIXED for Gradle 9.4.0) |
| `gradle.properties` | Project properties (Java 25, Minecraft 1.26.1) |
| `settings.gradle` | Gradle settings (plugin repos) |
| `fabric.mod.json` | Mod metadata |
| `parsmodernpvp.mixins.json` | Mixin configuration |
| `*.java` | Source code (corrected) |
| `README.md` | Full documentation |
| `CHANGELOG.md` | Detailed change log |

---

## 🚀 Next Steps

1. **Modify the code** as needed
2. **Run `./gradlew build`** to compile
3. **Test in game** with the JAR file
4. **Publish** when ready

---

## 💡 Pro Tips

### Hot Reload in IDE
```bash
# Keep this running in a terminal
./gradlew build --continuous

# Now every time you save a file, it rebuilds!
```

### Debug Mode
```bash
./gradlew runClient --debug
# Then attach debugger to port 5005
```

### Generate Docs
```bash
./gradlew javadoc
# Docs will be at: build/docs/javadoc/index.html
```

### Check Dependencies
```bash
./gradlew dependencies
# Shows all resolved dependencies
```

---

## 📞 Still Having Issues?

1. Read README.md for detailed info
2. Check CHANGELOG.md for all fixes
3. Verify all files are extracted correctly
4. Make sure you have Java 25+
5. Try a clean build: `./gradlew clean build`

---

**Ready to build? Run: `./gradlew build`** 🎮
