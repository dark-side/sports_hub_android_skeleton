# Android Mobile GenAI Playground

### ⚙️ Environment
- Android Studio Ladybug Feature Drop | 2024.2.2 Canary 9
- VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
- Gradle 8.10.2 and up
- Android Gradle Plugin 8.8.0-alpha09 and up

### 📦 Project Modules
- **app**: Main sample application module

### 🪜 Getting Started
Follow these steps to set up your development environment:

1. **Install Git:** Make sure [git](https://git-scm.com/) is installed in your system.
2. **Install Android Studio:** Download and install a stable version of [Android Studio](https://developer.android.com/studio).
3. **Clone the Repository:**
    1. Preferably use [SSH key authentication](https://docs.github.com/en/authentication/connecting-to-github-with-ssh) for enhanced security.
4. **Project Setup in Android Studio:**
    1. Navigate to `File -> Open`, select the directory where the project was cloned, and click `OK`.
5. **Synchronize Gradle:** Wait for Gradle to synchronize all the project dependencies and build configurations.
6. **Device Setup for Development:**
    - For physical device development and debugging, enable device development mode and familiarize yourself with ADB by checking out [this guide](https://developer.android.com/tools/adb).
    - For virtual device development and debugging, set up a virtual device as per instructions [here](https://developer.android.com/studio/run/managing-avds). Choose a `recommended` device image and avoid `preview` images.
7. **Build and Run Application:** Follow the steps described [here](https://developer.android.com/studio/run) to build and run the application.

### 💘 Contribute
- **Branching Methodology:** Use the `git-flow` branching methodology. Detailed information is available [here](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) and [here](https://nvie.com/posts/a-successful-git-branching-model/).
- **Commit Messages:** Follow [these](https://cbea.ms/git-commit/) to craft clear and informative commit messages. If applicable, prefix your commit message with `[xxx]`, where `xxx` is the identifier (number) of the related ticket, work item, or task. For example, `[211872] Update dependencies`.
- **Feature Branch Naming:** Name your feature branches starting with `feature/xxx-yyy`, where `xxx` is the identifier (number) of the related ticket, work item, or task (if applicable), and `yyy` is the short description. Example: `feature/211872-update-dependencies`. Use hyphen `-` as a word delimiter.
- **Pull Request Naming:** Name your pull requests starting with `[xxx]`, where `xxx` is the identifier (number) of the related ticket, work item, or task (if applicable). Example: `[211872] Update dependencies`.
- **Squashing Commits:** Before merging a pull request, squash your commits to clean up the history and streamline the integration of changes.
- **Line Breaks:** Ensure every saved file ends with a line break. Navigate to `File -> Settings -> Editor -> General -> On Save` and check the `Ensure every saved file ends with a line break` option.
