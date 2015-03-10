package solution.Models;

import com.google.gson.Gson;
import com.sun.deploy.util.StringUtils;
import solution.development.MapData;
import solution.development.PathNode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by rory on 09/03/15.
 */
public class GraphData {

    public enum DataFormat {STANDARD, CUSTOM};

    private static final String TXT = "txt";
    private static final String JSON = "json";

    private final ArrayList<CoordinateData> mNodes;

    public GraphData(final String dataFilePath, final DataFormat dataFormat){

        final String extension = dataFilePath.substring(dataFilePath.lastIndexOf(".")+1, dataFilePath.length());

        mNodes = new ArrayList<CoordinateData>();

        if(dataFormat == DataFormat.STANDARD){
            parseStandardTextFile(dataFilePath);
        }else if(dataFormat == DataFormat.CUSTOM){
            parseCustomTextFile(dataFilePath);
        }else{
            System.err.println("unknown data format: "+extension);
        }
    }

    private void parseStandardTextFile(String filePath) {

        try {

            final URL resource = getClass().getClassLoader().getResource(filePath);
            for(String line : Files.readAllLines(Paths.get(resource.toURI()))){
                String[] pieces = line.split(" ");
                if(pieces.length < 3) {
                    System.err.println("bad line: "+line);
                }else{
                    mNodes.add(new CoordinateData(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1]), Integer.parseInt(pieces[2])));

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void parseCustomTextFile(String filePath) {

        String input = null;
        try {
            final URL resource = getClass().getClassLoader().getResource(filePath);
            input = StringUtils.join(Files.readAllLines(Paths.get(resource.toURI())), "");
        } catch (IOException e) {

            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        MapData mapData = gson.fromJson(input, MapData.class);

        for(PathNode pathNode : mapData.getPathNodeList()){
            mNodes.add(new CoordinateData(pathNode.getId(), pathNode.getX(), pathNode.getY()));
        }

    }

    public ArrayList<CoordinateData> getNodes() {
        return mNodes;
    }
}