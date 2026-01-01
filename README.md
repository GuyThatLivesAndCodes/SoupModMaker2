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

**Current Phase:** Research & Planning ✅

We have completed comprehensive research on MCreator and defined the vision for SoupModMaker2.

---

## Documentation

### Research
- **[MCreator Research](docs/MCREATOR_RESEARCH.md)** - Comprehensive analysis of MCreator's architecture, features, and limitations

### Planning
- **[SoupModMaker2 Vision](docs/SOUPMODMAKER2_VISION.md)** - Project vision, goals, and design philosophy

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
├── docs/                    # Documentation
│   ├── MCREATOR_RESEARCH.md
│   └── SOUPMODMAKER2_VISION.md
└── README.md               # This file
```

More structure will be added as features are implemented (following our "no premature folder creation" principle).

---

## Roadmap

### Phase 1: Foundation ✅
- [x] Research MCreator
- [x] Define vision and goals
- [ ] Define detailed architecture
- [ ] Setup Java project structure

### Phase 2: Core Functionality
- [ ] Workspace management
- [ ] Basic mod element types
- [ ] Code generation engine
- [ ] Resource management

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
