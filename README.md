# PET HAVEN

A virtual pet game built with JavaFX where you can adopt and care for digital pets. Features include feeding, playing, exercising, and monitoring your pet's vital stats. The game includes parental controls for managing playtime and content access.

## System Requirements
#### Required
- Java Development Kit (JDK) 23
- JavaFX SDK 23.0.1
- Apache Maven 3.9.9

#### Development Mode (Optional for hot reloading of FXML and CSS files)
- Node.js
- pnpm (highly recommended over npm)

## Installation Instructions

### Windows Setup

1. **Install Java Development Kit (JDK) 23**
   - Download and install from Oracle's website

2. **Install JavaFX SDK**
   - Visit: https://www.tutorialspoint.com/javafx/javafx_environment.htm
   - Download from: https://gluonhq.com/products/javafx/
   - Select: Windows 23.0.1 x64 SDK
   - Create folder: `C:\Program Files\JavaFX`
   - Extract downloaded folder to: `C:\Program Files\JavaFX\javafx-sdk-23.0.1`
   - Set up environment variable:
     1. Search "Edit the system environment variables" in Windows search
     2. Click "Environment Variables..." (bottom left)
     3. Under "System variables" (lower rectangle), click "New..."
     4. Enter:
        - Variable name: `PATH_TO_FX`
        - Variable value: `C:\Program Files\JavaFX\javafx-sdk-23.0.1\lib`
     5. Click "OK" on all windows to exit

3. **Install Maven**
   - Visit: https://phoenixnap.com/kb/install-maven-windows
   - Download from: https://maven.apache.org/download.cgi
   - Select: Binary zip archive (apache-maven-3.9.9-bin.zip)
   - Create folder: `C:\Program Files\Maven`
   - Extract downloaded folder to: `C:\Program Files\Maven\apache-maven-3.9.9`
   - Add to system PATH:
     1. Search "Edit the system environment variables" in Windows search
     2. Click "Environment Variables..." (bottom left)
     3. Under "System variables", find and select "Path"
     4. Click "Edit..."
     5. Click "New" (top right)
     6. Enter: `C:\Program Files\Maven\apache-maven-3.9.9\bin`
     7. Click "OK" on all windows to exit

### macOS Setup
Paste and run in macOS Terminal
```bash
# Install Homebrew
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install JDK 23
brew install openjdk@23

# Add Java to PATH
export PATH="/opt/homebrew/opt/openjdk@23/bin:$PATH"

# Install Maven
brew install maven
```

## Running the Game

### Standard Mode
Open Terminal/Command Prompt in the project directory and run:

```bash
mvn clean javafx:run
```

### Development Mode (Optional)

#### Windows Setup

1. Download Node.js from https://nodejs.org/
2. Install pnpm (recommended)
    ```bash
    npm install -g pnpm
    ```
3. Install development dependencies:
   ```bash
   pnpm install
   ```
4. Start development mode:
    ```bash
    dev.sh
    ```

#### macOS Setup
```bash
# Install Node.js and pnpm (recommended)
brew install node
brew install pnpm

# Install development dependencies
pnpm install

# Start development mode
./dev.sh
```

## User Guide

### Basic Controls
- Use mouse clicks to interact with buttons and menus
- Volume can be adjusted in the settings menu
- Save progress regularly

### Pet Care
- Feed your pet to maintain hunger levels
- Play with your pet to increase happiness
- Exercise to maintain health
- Take to vet when health is low
- Give gifts to increase affection
- Your pet will die if health reaches zero

### Inventory System
- Purchase items using earned points
- Access inventory through the game menu
- Items can be used to care for your pet

## Parental Controls

### Setting Up Parental Controls
1. Access Settings from the main menu
2. Click "CREATE PROFILE" under Parental Controls
3. Create a 6-digit PIN
4. Confirm the PIN

### Features
- Time limit settings
- Play time restrictions
- Pet revival options
- Inventory management

Default Parent PIN: None (must be created on first use)

## Additional Information

- Save files are stored in the `saves` directory
- Game settings are saved in `preferences.json`
- The game uses autosave when exiting properly
- Development mode enables hot reloading of FXML and CSS files for faster development

## Troubleshooting

If you encounter "JavaFX runtime components are missing" error:
1. Verify JavaFX SDK installation
2. Check environment variables
3. Ensure Maven is properly configured

For any other issues, verify all environment variables are set correctly and required dependencies are installed.
