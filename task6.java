import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.*;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.DownloadErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.FileMetadata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class task6{
  public  void t6(String token){
    System.out.println(token);
    String array [] = {"t2_output01", "t3_output01", "t4_output01", "t5_output01"};
		try{
			for(int i = 0;i<array.length;i++){
				String writing = "";
				File file = new File("../dataset/"+array[i]+".txt");
				Scanner scan = new Scanner(file);
				while(scan.hasNextLine()){
					writing += scan.nextLine()+"\n";
				}
				writeToFile("t6_output01.txt", writing, true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		DropboxDriver dropbox = new DropboxDriver();
		DbxRequestConfig config = new DbxRequestConfig("en_US");
		DbxClientV2 client = new DbxClientV2(config, token);
		dropbox.uploadFile("/Users/nick/Computer_Science/441/cmpsc441-toccin-castellanosr-mahoneyr/lab4/dataset/t6_output01.txt", "/441/t6_output01.txt", client);
  }
  public  void writeToFile(String fileName, String data, boolean append){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			File file = new File("../dataset/"+fileName);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), append);
			bw = new BufferedWriter(fw);
			bw.write(data);
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
