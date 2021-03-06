Android Audit Toolbox
===============

The Android Audit Toolbox is a collection of analyzers for auditing Android applications.

# Setup

0) Download and extract [Eclipse Luna IDE for Java Developers](https://eclipse.org/downloads/packages/release/Luna/SR2) for your Operating System.

1) Install Atlas Standard or Professional (not Lite).  When installing Atlas make sure to include the Atlas experimental features.  See [http://www.ensoftcorp.com/atlas](http://www.ensoftcorp.com/atlas/).

2) Install dependencies.  
 - [Eclipse Plugin Development Tools](http://download.eclipse.org/eclipse/updates/4.4)
 - [https://ensoftcorp.github.io/toolbox-commons](https://ensoftcorp.github.io/toolbox-commons)
 - [https://ensoftcorp.github.io/android-essentials-toolbox](https://ensoftcorp.github.io/android-essentials-toolbox)
 - [Eclipse Android Development Tools](https://developer.android.com/sdk/installing/installing-adt.html), you may need to download the [Android SDK](https://developer.android.com/sdk). Make sure to install the `SDK Platform` and `Google APIs` for each API version. 

3) Clone the Toobox repository.

`git clone https://github.com/questionablecode/android-audit-toolbox.git`

4) Import the `toolbox.audit.analysis` and `toolbox.audit.shell` projects into your Eclipse workspace.

# Using the Toolbox

## Shell Project

To use the analysis toolbox interactively make sure you have both the `toolbox.audit.analysis` and `toolbox.audit.shell` projects imported into the Eclipse workspace.  Then navigate to `Window`->`Show View`->`Other`->`Atlas`->`Atlas Shell`.  Select the `toolbox.audit.shell` project from the shell project list and press `OK`.

From the Atlas Shell you can run any Java analysis code in the `toolbox.audit.analysis` project.  To automatically import packages or classes on the Shell edit the `atlasInit.scala` file.

To open an interactive Smart View right click on the `toolbox.audit.shell` project and navigate to `Atlas`->`Open Atlas Smart View`.  Drag the Smart View window to your preferred location in the Eclipse IDE.  In the Smart View window click on the down arror and navigate to `Script` and then select the Smart View you'd like to display.

## Headless Mode

The `toolbox.audit.analysis` project is also an Eclipse plugin that can be installed and run in a headless mode.  To install the Eclipse plugin from the workspace right click on the project and navigate to `Export`->`Plug-in Development`->`Deployable plug-ins and fragments`.  Select `Next` and make sure only the `toolbox.audit.analysis` project is selected.  Then select the `Install into host.` radio and click `Finish`.  You will need to restart Eclipse.

To run the analysis toolbox project in a headless mode invoke Eclipse from the command line with arguments similiar to the following:

    ./eclipse -application toolbox.audit.analysis.Headless 
              -nosplash 
              -consoleLog  
              -data <workspace path>/headless-workspace/ 
              -import <project path>/{MyProject | MyApp.apk}
              -output <output path>/output.xml
              -remove-imported-projects-after-analysis
              -vmargs -Dsdtcore.headless=true
              
### Eclipse Arguments Explained

| **Argument**                                              |                                                **Explanation**                                               |
|-----------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------:|
| -application edu.iastate.binary.toolbox.analysis.Headless | The identifier of the Eclipse application to run. This specifies the headless toolbox entry point.            |
| -nosplash                                                 | Disables the Eclipse splash screen                                                                            |
| -consoleLog                                               | Redirects any log output sent to Java's System.out (typically back to the command shell if any)               |
| -data &lt;workspace path&gt;                                    | Set the Eclipse workspace to use                                                                              |
| -vmargs -Dsdtcore.headless=true                           | Sets a VM argument to run the Scala plugin in a headless mode.  Without this argument the toolbox will crash. |

### Headless Toolbox Arguments Explained

| **Argument**                                    |                            **Explanation**                            |
|-------------------------------------------------|:----------------------------------------------------------------------:|
| -import &lt;project path&gt;/{MyProject or MyApp.apk} | Imports a Eclipse project or Android binary into the workspace         |
| -output &lt;output file path&gt;                      | Sets the output file path                                              |
| -close-imported-projects-after-analysis         | Closes the imported project after the analysis is complete             |
| -remove-imported-projects-after-analysis        | Closes and removes the imported project after the analysis is complete |

For additional Eclipse runtime arguments see [help.eclipse.org](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fmisc%2Fruntime-options.html).

# Extending the Toolbox

## Adding an analysis script
To add an analysis script extend `toolbox.analysis.Script` and implement the `evaluateEnvelope` method.  Alternatively create your own Java classes for analysis and invoke them directly from the Headless entry point or on the Atlas Shell.
