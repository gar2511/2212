const chokidar = require("chokidar");
const { exec } = require("child_process");

// Watch all FXML and CSS files in the resources directory
const watcher = chokidar.watch("src/main/resources/**/*.{fxml,css}", {
  persistent: true
});

console.log("Watching for FXML and CSS changes...");

watcher.on("change", (path) => {
  console.log(`File ${path} has been changed`);
  
  // Touch the App.java file to trigger recompilation
  exec("touch src/main/java/com/example/App.java", (error) => {
    if (error) {
      console.error(`Error touching App.java: ${error}`);
      return;
    }
    console.log("Triggered recompilation");
  });
});