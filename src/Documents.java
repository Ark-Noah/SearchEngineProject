/* 	1. Find all the files with txt ext.
 * 	2. Read all the contents
 * 	3. Remove Punctuations
 * 	4. Set the document map
 */
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Documents {
    public HashMap<String, List<String>> getDocuments() {

        HashMap<String, List<String>> documentMap = new HashMap<>();
        //if the program is to be run from cmd, absolute path should be provided here
        String myPath = ".\\src\\files";
        try {

            // Get all the files with ".txt" extension from the directory
            File f = new File(myPath);
            FilenameFilter fileFilter = new FilenameFilter(){
                public boolean accept(File dir, String name) {
                    String lowercaseName = name.toLowerCase();
                    if (lowercaseName.endsWith(".txt")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            String[] files = f.list(fileFilter);

            //put the file names and word lists in a hashmap
            for (int i = 0; i < files.length; i++) {
                String path = myPath + "\\" + files[i];
                String data = "";
                data = new String(Files.readAllBytes(Paths.get(path)));
                data = data.replaceAll("\\p{Punct}", "");
                documentMap.put(files[i].replace(".txt", ""), Arrays.asList(data.split(" ")));
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return documentMap;
    }
}
