package olegivanov.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    @SerializedName("ADDRESS")
    private String address;
    @SerializedName("PORT")
    private int port;

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setADDRESS(String address) {
        this.address = address;
    }

    public void setPORT(int port) {
        this.port = port;
    }

    public Config() {
        Config config = loadFile("config.json");
        if (config == null) {
            this.address = "127.0.0.1";
            this.port = 8185;
        } else {
            this.address = config.address;
            this.port = config.port;
        }

    }

    public static Config loadFile(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //System.out.println("DIR:  - " + System.getProperty("user.dir"));
        try (FileReader reader = new FileReader(file)) {
            //JsonElement json = gson.fromJson(reader, JsonElement.class);
            System.out.println(gson.fromJson(reader, Config.class).address);
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            System.out.println("Config File - NotFound - Error" + e.getMessage());
            return null;
        } catch (IOException e2) {
            System.out.println("IO - Error" + e2.getMessage() );
            return null;
        }  catch (Exception e3) {
        System.out.println("All other Errors" + e3.getMessage());
        return null;
        }
    }
        public void toFile(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonConfig);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

