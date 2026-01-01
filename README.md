# SoupModMaker2

**A better Minecraft mod maker** - Clean code generation, old version support, and Java-aligned workflows.

---

## Overview

SoupModMaker2 is an open-source Minecraft mod creation tool designed to surpass MCreator with:

- **Clean Code Generation** - Professional-grade, readable Java code
- **Old Version Support** - First-class support for legacy Minecraft versions (1.7.10, 1.8.9, 1.12.2, etc.)
- **Advanced Mob AI** - Intuitive visual state machine and behavior tree editor
- **Better Event System** - Natural, flexible event handling
- **Java-Aligned** - Code that mirrors real Java modding patterns

---

## Status

**Current Phase:** Phase 2 Complete - Working Forge 1.12.2 Generator! ✅

SoupModMaker2 can now generate complete, buildable Forge 1.12.2 mods with clean code!

---

## Quick Start

### For Users (Using Pre-built JAR)

```bash
# Download the JAR from GitHub Releases, then:

# Run the demo
java -jar soupmodmaker2-0.1.0-SNAPSHOT.jar

# Or use the startup scripts
./startup.sh        # macOS/Linux
startup.bat         # Windows
```

See **[DISTRIBUTION.md](DISTRIBUTION.md)** for full usage guide.

### For Developers (Building from Source)

```bash
# Build the project
gradle build

# Build executable JAR (recommended)
gradle shadowJar

# Run the demo (generates a test mod)
gradle run

# Generate from example workspace
gradle run --args="--full-demo"

# Check generated mod
cd generated/testmod
gradle build  # Build the generated Forge mod!
```

## Documentation

### User Guides
- **[Distribution Guide](DISTRIBUTION.md)** - How to run and distribute SoupModMaker2

### Developer Documentation
- **[MCreator Research](docs/MCREATOR_RESEARCH.md)** - Comprehensive analysis of MCreator's architecture, features, and limitations
- **[SoupModMaker2 Vision](docs/SOUPMODMAKER2_VISION.md)** - Project vision, goals, and design philosophy
- **[Architecture](docs/ARCHITECTURE.md)** - Technical architecture and design decisions

---

## Key Features (Planned)

### Code Quality
- Clean, optimized code generation
- Readable, maintainable logic
- Easy to customize generated code
- Context-aware generation

### Mob AI System
- Visual state machine editor
- Behavior tree builder
- Goal/task priority system
- Animation integration
- Easy AI debugging tools

### Event System
- Hybrid visual/code events
- Comprehensive event coverage
- Built-in event debugger
- Flexible, extensible system

### Version Support
- First-class legacy support (1.7.10+)
- Modern tool for old versions
- Forge, NeoForge, Fabric support
- Version-agnostic workspace format

---

## Technology Stack

- **Language:** Java (native to Minecraft ecosystem)
- **Build System:** Gradle
- **Template Engine:** FreeMarker (or custom)
- **UI Framework:** TBD (JavaFX/Swing/Electron)

---

## Development Principles

1. **Clean Code Generation** - Generate code that experienced Java modders would write
2. **Balanced Abstraction** - Helpful without hiding important concepts
3. **Java-Native Approach** - Generate code that looks like hand-written mods
4. **Comprehensive Version Support** - First-class support for all Minecraft versions
5. **No Premature Folder Creation** - Only create structure when implementing features

---

## Project Structure

```
SoupModMaker2/
├── docs/                          # Documentation
│   ├── MCREATOR_RESEARCH.md      # Comprehensive MCreator analysis
│   ├── SOUPMODMAKER2_VISION.md   # Project vision and goals
│   └── ARCHITECTURE.md            # Technical architecture
├── src/
│   ├── main/
│   │   ├── java/com/soupmodmaker/
│   │   │   ├── core/
│   │   │   │   ├── workspace/         # Workspace management ✅
│   │   │   │   │   └── elements/      # Mod elements (Items ✅)
│   │   │   │   ├── codegen/           # Code generation engine ✅
│   │   │   │   ├── templates/         # Template management ✅
│   │   │   │   └── export/            # Mod export ✅
│   │   │   ├── generators/            # Version-specific generators ✅
│   │   │   │   └── impl/              # Forge 1.12.2 ✅
│   │   │   ├── util/                  # Utilities
│   │   │   └── cli/                   # Command-line interface ✅
│   │   └── resources/
│   │       └── templates/
│   │           └── forge-1.12.2/      # FreeMarker templates ✅
│   └── test/java/                     # Unit tests (future)
├── .github/workflows/
│   ├── build.yml                      # CI/CD build workflow ✅
│   └── generate-mod.yml               # Test mod generation ✅
├── examples/
│   └── example-workspace.json         # Example workspace file ✅
├── build.gradle.kts                   # Gradle build configuration ✅
├── settings.gradle.kts                # Gradle settings ✅
└── README.md                          # This file
```

---

## Roadmap

### Phase 1: Foundation ✅ COMPLETE
- [x] Research MCreator
- [x] Define vision and goals
- [x] Define detailed architecture
- [x] Setup Java project structure
- [x] Create core workspace classes
- [x] Implement JSON workspace loading
- [x] Create proof-of-concept code generator
- [x] Demonstrate clean code generation

### Phase 2: Core Functionality ✅ COMPLETE
- [x] Workspace management (JSON loading/saving)
- [x] Basic mod element types (Items)
- [x] Code generation engine (FreeMarker templates)
- [x] Forge 1.12.2 generator (complete mod generation)
- [x] Resource management (templates, mcmod.info)
- [x] Build system integration

### Phase 3: Advanced Features
- [ ] Advanced mob AI builder
- [ ] Event system
- [ ] GUI designer

### Phase 4: Multi-Version Support
- [ ] Legacy version generators
- [ ] Modern version generators
- [ ] Version migration tools

### Phase 5: Polish & Extension
- [ ] Plugin API
- [ ] IDE integrations
- [ ] Documentation & tutorials

---

## How SoupModMaker2 Improves on MCreator

| Aspect | MCreator | SoupModMaker2 |
|--------|----------|---------------|
| **Code Quality** | Messy, inefficient | Clean, optimized |
| **Old Versions** | Limited support | First-class support |
| **Mob AI** | "Ton of procedures" | Visual state machine |
| **Event System** | Limited Blockly | Flexible hybrid system |
| **Learning Curve** | Too simple or too complex | Progressive complexity |

---

## Contributing

This project is in early development. Contributions will be welcome once the core architecture is established.

---

## License

TBD - Will be open source

---

## Acknowledgments

- **MCreator** - Research and learning from the leading Minecraft mod maker
- Research based on MCreator's open-source codebase and documentation

---

**Built with the goal of creating the best Minecraft mod maker ever.**
