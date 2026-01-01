# SoupModMaker2 - Technical Architecture

**Last Updated:** January 1, 2026
**Status:** Foundation Phase

---

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Technology Decisions](#technology-decisions)
3. [Core Components](#core-components)
4. [Package Structure](#package-structure)
5. [Code Generation Pipeline](#code-generation-pipeline)
6. [Workspace Format](#workspace-format)
7. [Build System](#build-system)
8. [Development Setup](#development-setup)

---

## Architecture Overview

### Design Principles

1. **Separation of Concerns** - Clear boundaries between components
2. **Modularity** - Independent, reusable modules
3. **Extensibility** - Plugin system for custom generators
4. **Testability** - Unit tests for all core functionality
5. **Clean Code Generation** - Primary differentiator from MCreator

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     SoupModMaker2 Application                │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  UI Layer    │  │  Core Layer  │  │  Generator   │      │
│  │              │  │              │  │  Layer       │      │
│  │  - Editors   │──│  - Workspace │──│  - Templates │      │
│  │  - Workspace │  │  - CodeGen   │  │  - Version   │      │
│  │  - Visual    │  │  - Export    │  │    Specific  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## Technology Decisions

### Core Application

**Language:** Java 17+
- Reasoning: Native to Minecraft ecosystem, proven for MCreator
- Version: Target Java 17 (LTS) for broad compatibility

**Build System:** Gradle 8+
- Reasoning: Industry standard for Minecraft mods
- Features: Multi-module support, dependency management
- Build file: Kotlin DSL for better IDE support

### Code Generation

**Template Engine:** FreeMarker 2.3+
- Reasoning: Proven in MCreator, powerful, Java-native
- Will use custom helpers for cleaner code generation
- Templates organized by Minecraft version

### Workspace Format

**Format:** JSON
- Reasoning: Human-readable, Git-friendly, easy parsing
- Schema validation for workspace integrity
- Version field for migration support

### Dependency Management

**Logging:** SLF4J + Logback
**JSON:** Gson (lightweight, familiar)
**Testing:** JUnit 5 + Mockito
**Build:** Gradle with Kotlin DSL

### UI Framework (Phase 2)

**Decision:** Start without UI (CLI/API first)
- Reasoning: Focus on core generation quality
- Future: JavaFX (modern, good tooling) or Swing (MCreator-like)
- Phase 1: Command-line tool for proof-of-concept

---

## Core Components

### 1. Workspace Module (`core/workspace`)

**Responsibility:** Manage mod project workspaces

**Key Classes:**
- `Workspace` - Main workspace container
- `WorkspaceElement` - Base class for mod elements (items, blocks, etc.)
- `WorkspaceLoader` - Load/save workspace JSON
- `WorkspaceValidator` - Validate workspace integrity

**Example:**
```java
public class Workspace {
    private String name;
    private String minecraftVersion;
    private String modLoader; // forge, neoforge, fabric
    private List<WorkspaceElement> elements;
    private WorkspaceSettings settings;
}
```

### 2. Code Generation Module (`core/codegen`)

**Responsibility:** Generate clean Java code from workspace elements

**Key Classes:**
- `CodeGenerator` - Main generation orchestrator
- `TemplateRenderer` - FreeMarker template rendering
- `CodeContext` - Context data for templates
- `CodeFormatter` - Post-processing for clean code

**Example:**
```java
public interface CodeGenerator {
    GeneratedCode generate(WorkspaceElement element, CodeContext context);
}
```

### 3. Template Module (`core/templates`)

**Responsibility:** Template storage and management

**Structure:**
```
templates/
├── common/              # Shared templates
├── forge-1.7.10/       # 1.7.10-specific
├── forge-1.12.2/       # 1.12.2-specific
├── forge-1.20.1/       # 1.20.1-specific
└── neoforge-1.21/      # 1.21-specific
```

**Key Classes:**
- `TemplateManager` - Load and cache templates
- `TemplateRegistry` - Version-specific template lookup
- `TemplateHelper` - Custom FreeMarker helpers

### 4. Export Module (`core/export`)

**Responsibility:** Export workspace to mod JAR

**Key Classes:**
- `ModExporter` - Main export orchestrator
- `GradleBuilder` - Invoke Gradle build
- `ResourceCompiler` - Compile resources
- `JarPackager` - Package final mod JAR

### 5. Generator Module (`generators`)

**Responsibility:** Version-specific code generation

**Structure:**
```java
public interface VersionGenerator {
    String getSupportedVersion();
    String getModLoader(); // forge, neoforge, fabric
    GeneratedMod generate(Workspace workspace);
}
```

**Implementations:**
- `Forge1710Generator`
- `Forge1122Generator`
- `NeoForge121Generator`
- etc.

---

## Package Structure

```
com.soupmodmaker/
├── core/
│   ├── workspace/
│   │   ├── Workspace.java
│   │   ├── WorkspaceElement.java
│   │   ├── WorkspaceLoader.java
│   │   └── elements/
│   │       ├── ItemElement.java
│   │       ├── BlockElement.java
│   │       └── EntityElement.java
│   │
│   ├── codegen/
│   │   ├── CodeGenerator.java
│   │   ├── TemplateRenderer.java
│   │   ├── CodeContext.java
│   │   └── CodeFormatter.java
│   │
│   ├── templates/
│   │   ├── TemplateManager.java
│   │   ├── TemplateRegistry.java
│   │   └── helpers/
│   │       └── JavaHelper.java
│   │
│   └── export/
│       ├── ModExporter.java
│       ├── GradleBuilder.java
│       └── JarPackager.java
│
├── generators/
│   ├── VersionGenerator.java
│   └── impl/
│       ├── Forge1710Generator.java
│       └── Forge1122Generator.java
│
├── util/
│   ├── Logger.java
│   └── FileUtils.java
│
└── cli/
    └── SoupModMakerCLI.java (Phase 1)
```

---

## Code Generation Pipeline

### Generation Flow

```
1. Load Workspace (JSON)
        ↓
2. Validate Elements
        ↓
3. Select Generator (based on version/loader)
        ↓
4. For Each Element:
   a. Build Code Context
   b. Select Template
   c. Render Template
   d. Format Generated Code
   e. Write to Output
        ↓
5. Generate build.gradle
        ↓
6. Copy Resources
        ↓
7. Export to Mod JAR (if requested)
```

### Example: Item Generation

**Input (Workspace JSON):**
```json
{
  "type": "item",
  "name": "ruby",
  "displayName": "Ruby",
  "maxStackSize": 64,
  "properties": {
    "glows": false,
    "rarity": "COMMON"
  }
}
```

**Template (FreeMarker):**
```java
package ${modPackage}.item;

import net.minecraft.item.Item;

public class ${className} extends Item {
    public ${className}() {
        super();
        this.setUnlocalizedName("${unlocalizedName}");
        this.setMaxStackSize(${maxStackSize});
        <#if glows>
        this.setFull3D();
        </#if>
    }
}
```

**Output (Generated Java):**
```java
package com.example.testmod.item;

import net.minecraft.item.Item;

public class ItemRuby extends Item {
    public ItemRuby() {
        super();
        this.setUnlocalizedName("ruby");
        this.setMaxStackSize(64);
    }
}
```

---

## Workspace Format

### Workspace JSON Schema

```json
{
  "version": "1.0.0",
  "workspace": {
    "name": "TestMod",
    "modId": "testmod",
    "minecraftVersion": "1.12.2",
    "modLoader": "forge",
    "settings": {
      "javaPackage": "com.example.testmod",
      "author": "Author Name",
      "description": "A test mod"
    }
  },
  "elements": [
    {
      "type": "item",
      "id": "ruby",
      "name": "Ruby",
      "properties": {
        "maxStackSize": 64
      }
    }
  ]
}
```

### Element Types (Phase 1 - Minimal)

1. **Item** - Basic items
2. **Block** - Basic blocks

### Element Types (Future Phases)

3. **Entity** - Mobs and custom entities
4. **GUI** - Custom GUIs
5. **Dimension** - Custom dimensions
6. **Biome** - Custom biomes
7. **Procedure** - Custom logic

---

## Build System

### Project Structure

```
SoupModMaker2/
├── build.gradle.kts          # Root build file
├── settings.gradle.kts       # Multi-module settings
├── gradle/
│   └── wrapper/              # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/             # Java source
│   │   └── resources/        # Templates, configs
│   └── test/
│       └── java/             # Unit tests
├── templates/                # Generation templates
└── docs/                     # Documentation
```

### Gradle Configuration

**build.gradle.kts:**
```kotlin
plugins {
    java
    application
}

group = "com.soupmodmaker"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // FreeMarker for templates
    implementation("org.freemarker:freemarker:2.3.32")

    // JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.soupmodmaker.cli.SoupModMakerCLI")
}
```

---

## Development Setup

### Prerequisites

- Java 17 or higher
- Gradle 8+ (or use wrapper)
- Git

### Initial Setup

```bash
# Clone repository
git clone https://github.com/GuyThatLivesAndCodes/SoupModMaker2.git
cd SoupModMaker2

# Build project
./gradlew build

# Run CLI (future)
./gradlew run --args="generate workspace.json"

# Run tests
./gradlew test
```

### Development Workflow

1. Create workspace JSON
2. Run generator
3. Verify generated code quality
4. Iterate on templates
5. Test with actual Minecraft

---

## Phase 1 Goals

### Minimal Viable Product

1. ✅ Research complete
2. ✅ Architecture defined
3. ⏳ Java project setup
4. ⏳ Basic workspace loader
5. ⏳ Simple item generator (1.12.2)
6. ⏳ FreeMarker template rendering
7. ⏳ Generate clean Java code
8. ⏳ Compile to working mod

### Success Criteria

- [ ] Generate working Forge 1.12.2 mod with one item
- [ ] Generated code is clean and readable
- [ ] Code quality better than MCreator equivalent
- [ ] Full build to JAR works

---

## Next Steps

1. Setup Gradle project structure
2. Create core package skeleton
3. Implement workspace JSON loader
4. Create first FreeMarker template
5. Implement basic item generator
6. Test with Forge 1.12.2

---

**Document Status:** ✅ Complete - Ready for implementation
