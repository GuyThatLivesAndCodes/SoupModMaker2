# SoupModMaker2 - Vision & Design Document

**Created:** January 1, 2026
**Status:** Planning Phase
**Goal:** Build a Minecraft mod maker that surpasses MCreator with better code generation, old version support, and Java-aligned workflows

---

## Table of Contents

1. [Project Vision](#project-vision)
2. [Core Principles](#core-principles)
3. [Key Improvements Over MCreator](#key-improvements-over-mcreator)
4. [Planned Features](#planned-features)
5. [Technology Stack](#technology-stack)
6. [Development Philosophy](#development-philosophy)
7. [Next Steps](#next-steps)

---

## Project Vision

**SoupModMaker2** aims to be the ultimate Minecraft mod creation tool that bridges the gap between accessibility and professional modding practices.

### Primary Goals
1. **Better than MCreator** - Superior code quality, features, and user experience
2. **Old Version Support** - First-class support for legacy Minecraft versions
3. **Advanced Mob AI** - Intuitive, powerful AI creation without excessive procedures
4. **Better Event System** - Natural, flexible event handling
5. **Java-Aligned** - Code generation that mirrors real Java modding patterns
6. **Clean Architecture** - Organized, maintainable project structure

### Target Users
- **Beginners:** Visual tools for learning mod development
- **Intermediate:** Hybrid visual/code workflow
- **Advanced:** Full Java modding with helpful abstractions
- **Educators:** Teaching tool for Minecraft modding and Java

---

## Core Principles

### 1. Clean Code Generation
**Problem:** MCreator generates "dirty and inefficient" code that's hard to read and maintain.

**Solution:**
- Generate clean, idiomatic Java code
- Follow Minecraft modding best practices
- Readable code structure with proper organization
- Efficient implementations
- Well-commented generated code
- Code that experienced Java modders would write

### 2. Balanced Abstraction
**Problem:** MCreator is either too simplified or unnecessarily complex.

**Solution:**
- Helpful abstractions that don't hide important concepts
- Progressive complexity (simple for basics, powerful for advanced)
- Always allow dropping down to code level
- Visual tools that teach Java concepts, not obscure them
- Clear mapping between visual elements and generated code

### 3. Java-Native Approach
**Problem:** MCreator abstractions don't align with how Java mods are actually written.

**Solution:**
- Use Java as primary language (like MCreator)
- Generate code that looks like hand-written mods
- Support standard Forge/NeoForge/Fabric patterns
- Enable seamless transition from tool to pure Java
- Gradle-based build system
- Native IDE integration (IntelliJ, Eclipse, VS Code)

### 4. Comprehensive Version Support
**Problem:** MCreator has limited support for old Minecraft versions.

**Solution:**
- First-class support for legacy versions (1.7.10, 1.8.9, 1.12.2, etc.)
- Modern versions (latest Forge, NeoForge, Fabric)
- Plugin system for community version support
- Version migration tools
- Side-by-side multi-version development

### 5. No Premature Folder Creation
**Problem:** Creating folders without implementing features leads to confusion.

**Solution:**
- Only create folders when implementing features
- Keep project structure aligned with current functionality
- Clear documentation of what exists vs. what's planned
- Incremental, tested development

---

## Key Improvements Over MCreator

### Code Quality
| MCreator | SoupModMaker2 |
|----------|---------------|
| Messy, inefficient generated code | Clean, optimized code generation |
| Hard to read procedures | Readable, maintainable logic |
| Difficult to modify manually | Easy to customize generated code |
| Generic templates | Context-aware generation |

### Mob AI System
| MCreator | SoupModMaker2 |
|----------|---------------|
| Limited preset AI tasks | Advanced AI builder |
| "Ton of procedures" for custom AI | Visual state machine editor |
| Abstracted, simplified | True-to-Java AI implementation |
| Hard to create complex behaviors | Intuitive complex AI creation |

**Planned SoupModMaker2 AI Features:**
- Visual state machine editor
- Behavior tree builder
- Goal/task priority system
- Animation integration
- Path-finding customization
- Target selection logic
- Sensor/detector system
- Easy AI debugging tools

### Event System
| MCreator | SoupModMaker2 |
|----------|---------------|
| Blockly procedure blocks | Hybrid visual/code events |
| Limited event types | Comprehensive event coverage |
| Hard to debug | Built-in event debugger |
| Abstraction limits flexibility | Flexible, extensible system |

**Planned SoupModMaker2 Event Features:**
- Event pipeline visualization
- Custom event creation
- Event priority/ordering
- Conditional event firing
- Event data inspection
- Cross-mod event compatibility

### Version Support
| MCreator | SoupModMaker2 |
|----------|---------------|
| Limited old version support | First-class legacy support |
| 1.12.2 requires old MCreator version | Modern tool, old version export |
| Transitioning to NeoForge only | Forge, NeoForge, Fabric support |
| Version changes break workspaces | Version-agnostic workspace format |

### User Interface
| MCreator | SoupModMaker2 |
|----------|---------------|
| Becoming too complex (2025 feedback) | Intuitive, scalable complexity |
| All-in-one interface | Modular, customizable workspace |
| Limited customization | Theme system, layouts, extensions |
| Standalone only | IDE integration + standalone |

---

## Planned Features

### Phase 1: Foundation (Current)
- [ ] Research MCreator (✅ COMPLETE)
- [ ] Define architecture
- [ ] Setup Java project structure
- [ ] Basic Gradle build system
- [ ] Simple code generator prototype
- [ ] Basic GUI framework

### Phase 2: Core Functionality
- [ ] Workspace management
- [ ] Basic mod element types (items, blocks)
- [ ] Code generation engine
- [ ] Template system
- [ ] Resource management
- [ ] Export to mod file

### Phase 3: Advanced Features
- [ ] Advanced mob AI builder
- [ ] Event system
- [ ] GUI designer
- [ ] Dimension creator
- [ ] Biome builder
- [ ] Structure generator

### Phase 4: Multi-Version Support
- [ ] Version abstraction layer
- [ ] Legacy version generators (1.7.10, 1.8.9, 1.12.2)
- [ ] Modern version generators (1.20+)
- [ ] Forge/NeoForge/Fabric support
- [ ] Version migration tools

### Phase 5: Polish & Extension
- [ ] Plugin API
- [ ] Theme system
- [ ] IDE integrations
- [ ] Community marketplace
- [ ] Documentation & tutorials
- [ ] Example projects

---

## Technology Stack

### Core Application (Like MCreator)
**Language:** Java
- Native to Minecraft mod ecosystem
- Easy mod compilation and export
- Rich library ecosystem
- Cross-platform

**Build System:** Gradle
- Industry standard for Minecraft mods
- Powerful dependency management
- Plugin ecosystem
- Multi-project support

### UI Framework (To Be Decided)
**Options:**
1. **JavaFX** - Modern, feature-rich
2. **Swing** - Mature, stable (MCreator uses this)
3. **Electron + Java Backend** - Web technologies for UI

### Code Generation
**Template Engine:** FreeMarker (like MCreator)
- Proven in MCreator
- Powerful templating
- Java-native
- Good documentation

**Alternative:** Custom template system
- More control
- Better optimization
- Tailored to our needs

### Visual Programming (If Used)
**Options:**
1. **Blockly** - Google's block editor (MCreator uses this)
2. **Custom visual system** - More control, better Java alignment
3. **Hybrid approach** - Drag-and-drop Java snippets

### Version Control
**Workspace Format:** JSON/YAML
- Human-readable
- Version control friendly
- Easy to parse
- Extensible

---

## Development Philosophy

### Incremental Development
1. **Start Small:** Basic functionality first
2. **Test Early:** Each feature thoroughly tested
3. **Iterate:** Continuous improvement based on feedback
4. **No Premature Optimization:** Get it working, then make it fast

### User-Centric Design
1. **Listen to Feedback:** Learn from MCreator's criticisms
2. **Progressive Disclosure:** Show complexity as needed
3. **Multiple Workflows:** Support different user skill levels
4. **Clear Documentation:** Every feature well-documented

### Code Quality Standards
1. **Clean Code:** Readable, maintainable
2. **Design Patterns:** Use appropriate patterns
3. **Testing:** Unit tests for critical components
4. **Code Review:** Peer review for major changes

### Open Development
1. **Open Source:** Community contributions welcome
2. **Transparent:** Public roadmap and development
3. **Community-Driven:** Listen to user needs
4. **Extensible:** Plugin API for community extensions

---

## Architecture Overview (Planned)

### High-Level Components

```
SoupModMaker2/
├── core/                  # Core application logic
│   ├── workspace/         # Workspace management
│   ├── codegen/          # Code generation engine
│   ├── templates/        # Generation templates
│   └── export/           # Mod export functionality
├── ui/                   # User interface
│   ├── editors/          # Element editors (items, blocks, etc.)
│   ├── visual/           # Visual programming tools
│   └── workspace/        # Workspace UI
├── generators/           # Version-specific generators
│   ├── forge-1.7.10/
│   ├── forge-1.12.2/
│   ├── forge-1.20.1/
│   ├── neoforge-1.21/
│   └── fabric-1.20/
├── plugins/              # Plugin system
│   └── api/             # Plugin API
└── utils/               # Utilities and helpers
```

### Key Design Decisions

#### Workspace Format
- **JSON-based** workspace definition
- **Modular** mod elements (items, blocks, entities)
- **Version-agnostic** core format
- **Generator-specific** extensions

#### Code Generation Pipeline
```
Workspace Element → Template Selection → Context Building →
Code Generation → Post-Processing → Output
```

#### Plugin System
- **Hook-based** architecture
- **Event-driven** communication
- **Isolated** plugin loading
- **API versioning**

---

## Key Differentiators

### What Makes SoupModMaker2 Better

1. **Code Quality**
   - Professional-grade generated code
   - Easy to understand and modify
   - Follows Minecraft modding best practices

2. **Old Version Support**
   - 1.7.10, 1.8.9, 1.12.2 full support
   - Modern tool for legacy versions
   - Version migration assistance

3. **Advanced Mob AI**
   - Visual state machine editor
   - Behavior tree builder
   - No "ton of procedures" needed
   - True Java AI implementation

4. **Better Events**
   - More natural event system
   - Easier debugging
   - More flexible and powerful
   - Clear event pipeline

5. **Java-Aligned**
   - Code looks like real mods
   - Teaches proper patterns
   - Easy transition to pure Java
   - IDE integration

6. **Organized Development**
   - No premature folder creation
   - Clear project structure
   - Feature-aligned organization
   - Everything working together

---

## Success Metrics

### Technical Goals
- [ ] Generate code 80%+ as clean as hand-written
- [ ] Support 10+ Minecraft versions simultaneously
- [ ] 90%+ feature parity with MCreator
- [ ] 50%+ faster mod compilation
- [ ] <5% bug rate in generated code

### User Experience Goals
- [ ] 70%+ user satisfaction rating
- [ ] 30%+ faster mod creation vs MCreator
- [ ] 90%+ of features discoverable without docs
- [ ] 50%+ of users transition to pure Java
- [ ] Active community plugin ecosystem

---

## Next Steps

### Immediate Actions
1. ✅ Research MCreator (COMPLETE)
2. ⏳ Define detailed architecture
3. ⏳ Setup Java project structure
4. ⏳ Create basic proof-of-concept
5. ⏳ Implement simple code generator

### Short-Term Goals (Next 2-4 Weeks)
- [ ] Basic workspace creation
- [ ] Simple item generator
- [ ] Test mod compilation
- [ ] Prove core concept works

### Medium-Term Goals (Next 2-3 Months)
- [ ] Multiple mod element types
- [ ] Basic UI
- [ ] One version generator complete
- [ ] Export working mods

### Long-Term Goals (6+ Months)
- [ ] Feature parity with MCreator
- [ ] Multiple version support
- [ ] Advanced features (AI, events)
- [ ] Public beta release

---

## Open Questions

### Technical Decisions
- [ ] UI framework choice (JavaFX vs Swing vs Electron)
- [ ] Visual programming approach (Blockly vs custom)
- [ ] Template engine (FreeMarker vs custom)
- [ ] IDE integration strategy

### Feature Priorities
- [ ] Which Minecraft versions to support first?
- [ ] Which mod loaders to prioritize? (Forge vs NeoForge vs Fabric)
- [ ] Visual vs code-first approach?
- [ ] Plugin system early or late?

### Architecture
- [ ] Monolithic vs modular architecture?
- [ ] How to handle version differences?
- [ ] Workspace format details
- [ ] Code generation pipeline specifics

---

## References

See `MCREATOR_RESEARCH.md` for comprehensive MCreator analysis.

### Key Learning from MCreator
- Start with solid architecture
- Don't over-abstract too early
- Keep code generation clean from day one
- Plan for extensibility
- Listen to community feedback
- Test each feature thoroughly

---

**Document Status:** ✅ Complete - Ready to guide development

**Next Document:** ARCHITECTURE.md (detailed technical architecture)
