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

public class task1{
  public void t1(){
    try{
      deleteFiles();
      File profiles = getHTML("http://dogtime.com/dog-breeds/profiles");
			Document doc = Jsoup.parse(profiles, null);
			Elements resultLinks = doc.select(("div[class=group-list-item]"));
			Elements dogLinks = doc.select("div[class=group-list-item] a[class=group-link-for-image]");
			char[] array = new char[26];
			int index = 0;
			for (char c = 'A'; c <= 'Z'; c++)
				array[index++] = c;
			String t1_output01 = "";
			String t1_output02 = "";
			String t1_output03 = "";
			String t1_output04 = "";
			int count = 0;
			for (int i = 0; i < array.length; i++){
				count = 0;
					for (Element link : resultLinks){
						if (link.text().startsWith(Character.toString(array[i])) && array[i] < 'G'){
							t1_output01 += link.text() + ": "+ dogLinks.eq(count).attr("href")+"\n";
							count++;
						}
						else if(link.text().startsWith(Character.toString(array[i])) && array[i] > 'F' && array[i] < 'M'){
							t1_output02 += link.text() + ": "+ dogLinks.eq(count).attr("href")+"\n";
							count++;
						}
						else if(link.text().startsWith(Character.toString(array[i])) && array[i] > 'M' && array[i] < 'S'){
							t1_output03 += link.text() + ": "+ dogLinks.eq(count).attr("href")+"\n";
							count++;
						}
						else if(link.text().startsWith(Character.toString(array[i])) && array[i] > 'R' && array[i] <= 'Z'){
							t1_output04 += link.text() + ": "+ dogLinks.eq(count).attr("href")+"\n";
							count++;
						}
						else{
							//System.out.println("I am in the else");
							count++;
						}
					}
			}
			writeToFile("t1_output01.txt", t1_output01, false);
			writeToFile("t1_output02.txt", t1_output02, false);
			writeToFile("t1_output03.txt", t1_output03, false);
			writeToFile("t1_output04.txt", t1_output04, false);
			//System.out.println(t1_output03);
		}catch(Exception e){
			e.printStackTrace();
		}
  }
  public void writeToFile(String fileName, String data, boolean append){
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

  public File getHTML(String path){
		URL url;
		File skip = new File("../dataset/test.html");
		try {
			// get URL content
			url = new URL(path);
			URLConnection conn = url.openConnection();
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String inputLine;
			//save to this filename
			String fileName = "../dataset/test.html";
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			//use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
			}
			bw.close();
			br.close();
			return file;
	}catch (Exception e) {
		e.printStackTrace();
	}
	return skip;
}
public void deleteFiles(){
  String array [] = {"t1_output01", "t1_output02", "t1_output03", "t1_output04", "t2_output01", "t3_output01", "t4_output01", "t5_output01", "t6_output01"};
  for(int i = 0; i<array.length;i++){
    try{
      File file = new File("../dataset/"+array[i]+".txt");
      file.delete();
    }catch(Exception e){
      System.out.println("The file has not been found");
    }
  }

}
}
