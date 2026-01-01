# MCreator Research Document

**Last Updated:** January 1, 2026
**Purpose:** Comprehensive research on MCreator to inform SoupModMaker2 development

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture & Technology Stack](#architecture--technology-stack)
3. [Core Features](#core-features)
4. [Mob AI System](#mob-ai-system)
5. [Event & Procedure System](#event--procedure-system)
6. [Workspace Structure](#workspace-structure)
7. [Code Editor & IDE Features](#code-editor--ide-features)
8. [Version Support](#version-support)
9. [Export & Compilation](#export--compilation)
10. [Limitations & Criticisms](#limitations--criticisms)
11. [Plugin System](#plugin-system)

---

## Overview

**MCreator** is an open-source software used to make:
- Minecraft Java Edition mods
- Minecraft Bedrock Edition Add-Ons
- Resource packs
- Data packs

It provides both a visual block-based programming interface (Blockly) and an integrated code editor for direct Java programming.

**Official Resources:**
- Website: https://mcreator.net/
- GitHub: https://github.com/MCreator/MCreator
- License: GPL-3.0 (with exceptions per section 7)

---

## Architecture & Technology Stack

### Core Application
- **Primary Language:** Java (requires Java 21)
- **Build System:** Gradle
- **IDE:** Standalone application (can integrate with IntelliJ IDEA)

### Code Generation
- **FreeMarker Templates:** Used to generate all Java & JSON code
- **Blockly:** Visual programming interface (Google's block-based framework)
- **JavaScript:** Used for Blockly customization in plugins

### Output Languages
- **Java:** For mod logic and code
- **JSON:** For Minecraft data structures, resources, and configurations

### Development Approach
- Users can clone with: `git clone --recursive https://github.com/MCreator/MCreator.git`
- Gradle task `runMCreator` for testing during development
- IntelliJ IDEA recommended for MCreator development

### Third-Party Dependencies
MCreator uses several third-party libraries (license files in `license` subdirectory)

---

## Core Features

### Latest Version: MCreator 2025.3 (as of January 2026)

#### Supported Platforms (2025.3)
- NeoForge 1.21.8/1.21.1
- Data Packs 1.21.8/1.21.1
- Resource Packs 1.21.8/1.21.1
- Bedrock Edition 1.21.x

### Animation Systems
- **Entity Animations:** Custom animations for entities with conditional triggers
- **Block Animations:** Animations for custom blocks conditionally executed by procedures
- **Item Animations:** Items with custom Java models can have custom conditionally triggered animations

### Resource Pack Creation
- Can create resource packs for core Minecraft game
- **Mod Resource Packs:** Drag and drop a mod file to create resource packs for imported mods
- Makes MCreator a comprehensive Minecraft resource pack maker

### Block Features
- Custom block creation with extensive parameters:
  - Is ignited by lava
  - Note block instrument
  - Block set type for block bases
  - Custom models and textures
  - Redstone behavior
  - Light emission
  - Collision and hitboxes

### Item Features
- Custom items with parameters:
  - Banner patterns this item provides
  - Maximum stack size up to 99
  - Custom models and animations
  - Tool properties
  - Food properties
  - Durability

### Tag System
- **Structure Tags:** Tag structures with properties
  - Define what structures are located with eye of ender
  - Control where mobs spawn (e.g., cat spawning in structures)
- Support for vanilla tag types

### Attribute Modifiers
- Add, check, and remove attribute modifiers from entities
- Applies to custom and vanilla entities including the player
- Enables custom dynamics and entity behavior

### User Interface Improvements
- Vectorized icons across the interface (especially beneficial for high-DPI and Retina displays)
- Auto-colored icons for custom themes
- Modern, pleasant UI design

### Testing & Debugging
- Built-in testing environment
- Fully-fledged Java debugger for Minecraft client and server
- Live testing within MCreator

---

## Mob AI System

### AI Task Selection
MCreator allows reusing every single AI property from vanilla Minecraft. Users can:
- Select from presets that mimic vanilla mob behavior
- Mix and match AI tasks
- Create custom AI using procedures and state machines

### Available AI Tasks & Goals
- **Door Breaking:** Breaking down doors like zombies
- **Door Interaction:** Opening and closing doors like villagers
- **Shelter Seeking:** Finding shelter when possible (like villagers at night)
- **Buoyancy:** Floating towards water surface
- **Following Same Type:** Following entities of the same type (like fish)
- **Following Player:** Following when player holds specific items (like wheat for cows)

### Custom Entity Features
- Select from built-in models or import custom models
- **Mob Type:** Choose between "Mob" (can attack) or "Animal" (cannot attack)
- Model import from custom formats

### Advanced Custom AI
**Limitations:** For complex, unique behaviors:
- Must create custom state machines
- Requires extensive use of entity triggers and variables
- Needs "a ton of procedures" (as noted in documentation)
- More complex than visual tools can handle easily

---

## Event & Procedure System

### Evolution
- **Old System:** Events (deprecated in versions before 1.7.9)
- **Current System:** Procedures (replaced events in MCreator 1.7.9+)

### Procedure System
Based on **Blockly** (similar to Scratch editor):
- Visual block-based programming
- More condition options than old event system
- Better multiplayer compatibility
- More flexibility with event results

### How Triggers Work
1. Create a mod element (e.g., block, item, entity)
2. Attach procedure to a trigger (e.g., "on block right-clicked")
3. Each procedure has dependencies shown in the creator window

### Global Triggers
- Can create global triggers that fire on game-wide events
- Documentation: https://mcreator.net/wiki/creating-global-triggers

### Event Conditions
- Conditional logic within procedures
- Access to entity, world, and event data
- Custom variables and logic

### Code Generation from Procedures
- Procedures generate Java code using FreeMarker templates
- **Recent Improvements (2025):** Large chunks of repetitive code now extracted into methods for better organization and readability

---

## Workspace Structure

### Root Directory
- **@WORKSPACEROOT** - Path variable to current workspace

### Folder Structure
- **Code Files Directory:** Java files and packages
- **Resource Files Directory:** Assets and data files
- **Structures Directory:** Structure files (defined by `structures_dir` parameter)

### Workspace Sections (Left Panel)
1. **Mod Elements:** All custom mod elements
2. **Resources:** Textures, sounds, models
3. **Global Variables:** Workspace-wide variables
4. **Localization:** Language files
5. **Remote Workspace:** Version control integration

### Action Buttons
- Add new mod element
- Edit selected mod element
- Duplicate/copy selected mod element
- Delete selected mod element
- Edit code of selected element
- Lock/Unlock element's code
- Edit mod element IDs and registry names

### File Organization
- **Source Folder:** All packages and `.java` files
- **Resources Folder:** Asset and data files (textures, structures, JSON files)

---

## Code Editor & IDE Features

### Intelligent Features
- **Jump to Declaration:** Navigate to code definitions
- **Autocomplete:** Tailored for Minecraft development
  - Minecraft core functions
  - Custom mod elements
- **Code Assist:** Automatic code suggestions
- **Syntax Highlighting:** Colorful, readable code presentation

### Code Navigation
- **Code Tree:** Structured view of project files
- **File Browser:** Access to all workspace files
- **Package Explorer:** Java package structure

### Integration
- Can work on MCreator workspaces from **IntelliJ IDEA**
- Decompiled code insight
- Library code searching
- Search through entire codebase

### Accessibility
- No prior programming knowledge required
- Can use visual tools or direct code editing
- Gradual learning curve from blocks to code

---

## Version Support

### Current Official Support (2025.3)
- **NeoForge:** 1.21.8, 1.21.1
- **Data Packs:** 1.21.8, 1.21.1
- **Resource Packs:** 1.21.8, 1.21.1
- **Bedrock Edition:** 1.21.x

### Legacy Version Status

#### Minecraft 1.20.1
- Last version with Forge support
- MCreator no longer supports new Forge versions after 1.20.1
- Transitioning to NeoForge for newer versions

#### Minecraft 1.14.4
- Marked as LTS (Long Term Support)

#### Minecraft 1.12.2
- Legacy version
- Use MCreator 2021.1 with 1.12.2 plugin
- Oldest "safe" version to use
- New features from MCreator 2020.4+ not compatible

#### Minecraft 1.7.10
- No longer supported by Minecraft, Forge, or MCreator
- Stopped receiving updates

### Accessing Old Versions
- Older MCreator versions deleted from Pylo servers
- Community archives available
- Can create custom generator plugins for specific versions

### Version Download Archive
- All downloadable versions: https://mcreator.net/download/all
- Legacy installation procedures in MCreator Wiki

---

## Export & Compilation

### Export Process
1. Click export button in top toolbar
2. Enter mod details
3. Click Export
4. MCreator builds all resources and sources
5. Saves mod to file

### Gradle Build System
- Manages building and exporting
- Handles dependencies
- Compiles Java code
- Packages resources

### Forge/NeoForge Conversion
**To switch between mod loaders:**
1. Press workspace settings in top bar
2. Find current Minecraft Forge version
3. Press pencil icon next to it
4. Switch to NeoForge project (or vice versa)

**Note:** Switching to 1.20.1 allows mod to work on Forge. Newer versions require NeoForge.

### Common Compilation Issues
- **Gradle compilation errors:** Task `:compileJava` failures
- **Missing symbols:** Classes or procedures not properly generated
- **Gradle cache problems:** Solved by deleting gradle directory and reopening
- **Syntax errors in custom code:** Shown during build

### Official Mappings
- Some code generators use official Minecraft mappings
- Usage covered under Microsoft license

---

## Limitations & Criticisms

### Recent Issues (2025)

#### MCreator 2025.1+ Complexity
- Some users find newer versions "unusable"
- Reports of procedure corruption when switching between procedures
- Longtime users feeling "completely lost" with updates
- Increasing complexity making it harder for non-programmers

### Technical Limitations

#### Missing Features
Cannot create:
- GUI progress bars
- Chest-like animations (TERs - Tile Entity Renderers)
- Custom particles
- Custom damage sources
- Advanced ranged item support (limited)

#### Abstraction Constraints
- Heavily limited by abstraction layer
- Only features with buttons/text fields available
- Cannot add features MCreator doesn't explicitly support
- Must wait for MCreator updates to get new capabilities

### Code Quality Issues

#### Generated Code Problems
- **Messy Code:** "Dirty and inefficient at best"
- **Procedure Complexity:** Code becomes harder to read after adding procedures
- **Performance:** Generated code potentially slower
- **Bugs:** Messier code possibly more buggy
- **Maintainability:** Difficult to modify generated code manually

#### Hand-Holding Consequences
- Like any abstraction tool, limits what's possible
- Generated code not representative of best practices
- Hard to transition from MCreator to pure Java modding

### Reputation Issues

#### Community Perception
- Modding community doesn't hate MCreator itself
- **Real Issue:** Low-effort, unoriginal mods made in minutes
- Easy accessibility leads to flooding of poor-quality mods
- Stigma against "MCreator mods" in general

#### Learning Limitations
- May not teach proper Java modding techniques
- Can create dependency on tool rather than learning fundamentals
- Abstraction hides important Minecraft modding concepts

### Workflow Challenges
- Complex projects may become unwieldy
- Mixing visual and code editing can be confusing
- Debugging generated code difficult
- Version updates may break existing workspaces

---

## Plugin System

### Purpose
MCreator plugins extend functionality with:
- New generator types
- New procedure blocks
- Custom AI tasks
- Support for APIs
- Additional Minecraft version support

### Plugin Requirements
Every plugin must include **plugin.json** with:
- Author information
- Version
- Supported MCreator version
- Plugin weight (loading priority)
- Plugin ID

### Popular Plugins

#### Fabric Generator
- Adds Fabric mod loader support
- Alternative to Forge/NeoForge
- Available on GitHub

#### [Re]Structure
- Version 3.1.0
- Mod Element for Structure Generation
- Available for NeoForge 1.21.4

#### Version-Specific Generators
- Community-created plugins for specific Minecraft versions
- Example: Generator-1.19.2 for 2023.3/2023.4/2024.x/2025.x

### Plugin Development

#### Technical Setup
- Uses MCreator's plugin API
- Can register custom Blockly JavaScript files
- Uses FreeMarker for code generation
- Can define new workspace types

#### Documentation
- Plugin development wiki: https://mcreator.net/wiki/developing-mcreator-plugins
- Creating generators: https://mcreator.net/wiki/create-new-mcreator-generators
- Blockly usage: https://mcreator.net/wiki/how-use-blockly-plugins

#### Generator Examples
- Check Fabric Generator GitHub
- Example plugins in MCreator folder/plugins directory

### Plugin Marketplace
- Explore plugins: https://mcreator.net/plugins
- Community-submitted plugins
- Rated and reviewed by users

---

## Key Takeaways for SoupModMaker2

### What MCreator Does Well
1. **Accessibility:** Low barrier to entry for non-programmers
2. **Visual Programming:** Blockly interface for beginners
3. **Integrated IDE:** All-in-one workspace
4. **Multi-Platform:** Supports multiple Minecraft editions
5. **Plugin System:** Extensible architecture
6. **Open Source:** Community can contribute

### What MCreator Struggles With
1. **Code Quality:** Generated code is messy and inefficient
2. **Version Support:** Limited old version support
3. **Abstraction Limits:** Can't do things outside predefined features
4. **Complexity Creep:** Becoming too complex in recent versions
5. **Advanced AI:** Requires extensive procedures for custom behavior
6. **Event System:** Procedures system can be limiting for complex logic

### Opportunities for SoupModMaker2
1. **Better Code Generation:** Clean, maintainable, efficient code
2. **Old Version Support:** First-class support for legacy Minecraft versions
3. **Advanced AI Builder:** Intuitive, powerful mob AI creation
4. **Flexible Event System:** More natural event/trigger system
5. **Java-Aligned:** More closely mirror actual Java modding patterns
6. **Balanced Abstraction:** Helpful abstractions without over-simplification

---

## Sources

Research compiled from the following sources:

- [MCreator Official Website](https://mcreator.net/)
- [MCreator GitHub Repository](https://github.com/MCreator/MCreator)
- [About MCreator](https://mcreator.net/about)
- [MCreator Wiki](https://mcreator.net/wiki)
- [1st 2025.2 Snapshot](https://mcreator.net/news/115375/1st-20252-snapshot-packed-features)
- [MCreator 2025.3 Release](https://mcreator.net/news/120031/mcreator-20253-new-features-and-new-minecraft-version)
- [MCreator 2025.1 Release](https://mcreator.net/news/114121/mcreator-20251-minecraft-1214-support-many-new-features-and-fixes)
- [Procedure System Wiki](https://mcreator.net/wiki/procedure-system)
- [Mob AI Wiki](https://mcreator.net/wiki/mob-ai)
- [Creating Global Triggers](https://mcreator.net/wiki/creating-global-triggers)
- [Workspace Types](https://mcreator.net/wiki/workspace-types)
- [MCreator's Code Editor](https://mcreator.net/wiki/mcreators-code-editor)
- [Interface Overview](https://mcreator.net/wiki/interface-overview)
- [Developing MCreator Plugins](https://mcreator.net/wiki/developing-mcreator-plugins)
- [Why MCreator Sucks (Community Critique)](https://gist.github.com/thebrightspark/6d2bc3ca824fe2198b8979b070c4c943)
- [Is there a disadvantage for using Mcreator?](https://mcreator.net/forum/65465/there-disadvantage-using-mcreator)
- [Why is MCreator generally disliked?](https://www.planetminecraft.com/forums/minecraft/modding/why-is-mcreator-generally-dislik-585459/)

---

**Document Status:** ✅ Complete and ready for reference during SoupModMaker2 development
