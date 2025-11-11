import edu.duke.*;
import org.apache.commons.csv.*;
import java.util.*;
import java.io.*;

public class FixPos {
    public ArrayList<String[]> rows(FileResource fr){
        ArrayList<String[]> rows = new ArrayList<String[]>();
        for (CSVRecord rec: fr.getCSVParser(false)){
            String[] row = new String[13];
            for(int i=0; i<13; i++) {
                if(i==4) {
                    String position = rec.get(4);
                    if(position.equals("RF") || position.equals("RM"))
                        position="RW";
                    if(position.equals("LF") || position.equals("LM"))
                        position="LW";
                    if(position.equals("CF"))
                        position="ST";
                    if(position.equals("LWB"))
                        position="LB";
                    if(position.equals("RWB"))
                        position="RB";
                    row[4] = position;
                }
                else {
                    row[i]=rec.get(i);
                }
            }
            rows.add(row);
        }
        return rows;
    }
    public void run(){
        FileResource fr = new FileResource("src/Resource/players.csv");
        ArrayList<String[]> rows = rows(fr);
        makeCSV(rows);
    }
    public void makeCSV(ArrayList<String[]> rows){
        String filePath = "src/Resource/playerFile.csv";
        try (FileWriter writer = new FileWriter(filePath)){
            for (String[] row : rows)
                writer.append(String.join(",",row)).append("\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        FixPos pos = new FixPos();
        pos.run();
    }
}
