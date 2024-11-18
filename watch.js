const chokidar = require("chokidar");
const { exec } = require("child_process");

const APP_PATH = "src/main/java/com/example/App.java";

const watcher = chokidar.watch([
  "src/main/resources/**/*.{fxml,css}",
  "src/main/java/**/*.java"
], {
  persistent: true,
  ignored: APP_PATH
});

console.log("Watching for changes...");

watcher.on("change", (path) => {
  console.log(`File ${path} has been changed`);
  
  exec(`touch ${APP_PATH}`, (error) => {
    if (error) {
      console.error(`Error touching App.java: ${error}`);
      return;
    }
    console.log("Recompiling...");
  });
});