----
```java


public class Main {  
    /**  
     * JDK 1.4 버전 이하에서는 exec() 메소드를 이용하여 외부 프로그램을 실행 시킬 수 있고  
     * JDK 1.5 버전 이상에서는 ProcessBuilder 객체를 이용하여 외부 프로그램을 실행 시킬 수 있다.  
     */    public static String executeCmd(String[] cmd) {  
        try {  
            // create a process builder to execute the cmd instruction  
            /**  
             * OS Process를 수행시키고 흐름을 제어할 수 있다  
             * Process prs = ProcessBuilder.start();  
             * - prs.waitFor();             * - prs.destroy();             * > builder.command("cmd.exe", "/c", ".\\test.bat"); // Windows OS             */            // 프로세스 빌더를 통하여 외부 프로그램 실행  
            Process process = new ProcessBuilder(cmd).start();  
  
            // read the output of the process  
            // 외부 프로그램의 표준출력 상태 버퍼에 저장  
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            StringBuilder builder = new StringBuilder();  
            String line;  
            System.out.println("#@@@@");  
            while ((line = reader.readLine()) != null) {  
                builder.append(line);  
                System.out.println("***" + line);  
                builder.append(System.getProperty("line.separator"));  
            }  
  
            // wait for the process to finish and get the exit code  
            int exitCode = process.waitFor();  
  
            // return the output of the process as a string  
            return builder.toString().trim();  
        } catch (IOException | InterruptedException e) {  
            e.printStackTrace();  
            return null;        }  
    }  
  
    public static String createDirectories(String[] filePath, String[] cmd) {  
        String finalPath = "";  
        for (String path : filePath) {  
            Path dir = Paths.get(path);  
            if (!Files.exists(dir)) {  
                try {  
                    Files.createDirectories(dir);  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            finalPath = dir.toAbsolutePath().toString();  
        }  
        String[] cdCmd = { "cmd", "/c", cmd[0], cmd[1] };  
        executeCmd(cdCmd);  
        finalPath = Paths.get(cmd[1]).toAbsolutePath().toString();  
        return finalPath;  
    }  
  
    public static void main(String[] args) {  
        String[] filePath = {"C://main/folder1", "C://main/folder1/file1.txt", "C://main/folder2"};  
        String[] cmd = {"cd", "C://main/folder1"};  
        String finalPath = createDirectories(filePath, cmd);  
        System.out.println(finalPath);  
        System.out.println(createDirectories(filePath, new String[]{"cd", "C://mai"}));  
    }  
}

```

```java
import java.io.IOException;  
import java.nio.file.Files;  
import java.nio.file.Path;  
import java.nio.file.Paths;  
  
public class FolderCmd {  
  
    public static void main(String[] args) throws IOException, InterruptedException {  
        String[] filePath = {"C://main/folder1", "C://main/folder1/file1.txt"};  
        String[] cmd = {"cmd", "/c", "cd", "C://main/folder1"};  
  
        // Create directories specified in filePath array  
        for (int i = 0; i < filePath.length; i++) {  
            String[] currPath = filePath[i].split("/");  
            String path = "";  
            String file = "";  
            for (int j = 0; j < currPath.length; j++) {  
                if (currPath.length - 1 == j && currPath[i].contains(".")) {  
                    file += currPath[j];  
                } else {  
                    path += currPath[j] + "/";  
                }  
            }  
            Files.createDirectories(Paths.get(path));  
  
            // Create executable file at last path in filePath array  
            String homeDir = System.getProperty("user.home");  
            Path exeFile = Paths.get(homeDir, file);  
            // Path exeFile = Paths.get();  
            Files.createFile(exeFile);  
            exeFile.toFile().setExecutable(true);  
        }  
  
  
        // Execute cmd in the terminal  
        ProcessBuilder pb = new ProcessBuilder(cmd);  
        pb.redirectErrorStream(true);  
        Process p = pb.start();  
  
        // Wait for cmd process to complete  
        p.waitFor();  
  
        // Check if the directory specified in cmd exists  
        Path cmdPath = Paths.get(cmd[3]);  
        if (Files.exists(cmdPath) && Files.isDirectory(cmdPath)) {  
            System.out.println(cmdPath.toString());  
        } else {  
            System.out.println("");  
        }  
    }  
}
```


> instruction 

i'm trying to generate a code using java. the main method takes two parameters 1. Array of String that has list of file paths to generate (ex. String[] filePath = {"C://main/folder1", "C://main/folder1/file1.txt", "C://main/folder2"} 2. Array of String that represents cmd command and execution instruction (ex. String[] Cmd = {"CD", "C://main/folder1"}. String[] filePath actually makes file within the system using Files.createDirectories(), and String[] cmd executes the given in the terminal. so with the given example, you will execute cd C://main/folder1 in the terminal and get results. basically the code will return C://main/folder1 in this example as filePath[] created the folder. but if String[] cmd = {"cd", "C://main/fol"} is given, it will return empty string as there is no existing folder with path C://main/fol.