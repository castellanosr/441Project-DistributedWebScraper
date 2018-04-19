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

public class DistributedWebScraper {
	public static void writeToFile(String fileName, String data, boolean append){
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
	public static String GenerateBCR(File file) {
		// System.out.println(path);
		Stack<Double> stack = new Stack<Double>();
		Queue<Double> queue = new LinkedList();
		String listing = "";
		try{
			Document doc = Jsoup.parse(file, null);
			Elements dogName = doc.select("header h1");
			Elements resultLinks = doc.select(("div[class=group-list-item]"));
			Elements check = doc.select(("span[class=star-block stars-column]"));
			Elements group = doc.select("div[class=star-by-breed clearfix default-border-bottom default-margin-bottom default-padding-bottom parent-characteristic] span[class=characteristic item-trigger-title]");

			Elements name = doc.select (("div[class=js-list-item item-expandable-content default-border-bottom default-margin-bottom default-padding-bottom star-by-breed child-characteristic] span[class=characteristic item-trigger-title]"));
			String dog = (dogName.eq(0).text());
			//System.out.println(dog);
			int count1 = 0;
			int count2 = 0;

			//String cool = name.indexOf(name.text(),3);
			for(Element g: group){
				listing += "\""+g.text()+"\" : [ \"";
				if(check.eq(count1).text().length() == 0){
					count1++;
					while(check.eq(count1).text().length() != 0){
						listing += (name.eq(count2).text()+ "\" : ");
						if(check.eq(count1+1).text().length() != 0){
							listing += (check.eq(count1).text()+",\n\"");
							stack.add(Double.valueOf(check.eq(count1).text()));
						}else{
							listing += (check.eq(count1).text()+"]\n");
							stack.add(Double.valueOf(check.eq(count1).text()));
						}
						count1++;
						count2++;
					}
				}
				int first = 0;
				stack = getAverage(stack);
				listing+="Grouping total: " + stack.pop()+"\n\n";
				// while(!stack.isEmpty()){
				//
				// 	if(first == 0){
				// 		listing += (stack.peek()+"\n");
				// 		queue.add(stack.pop());
				//
				// 		first++;
				// 	}
				// 	else{
				// 		listing += (stack.peek()+"\n");
				// 		queue.add(stack.pop());
				// 	}
				// }

				//listing += "The average of this grouping is: "+getAverage(stack)+"\n\n";
			}
			return listing;
			//System.out.println(listing);
			//System.out.println(listing);
			//System.out.println(listing);

		}catch(Exception e){
			System.exit(0);
		}
			//file.delete();

			return listing;
		}

		public static String GenerateTextualData(File file){
			Queue<String> queue = new LinkedList();
			String listing = "";
			try{
				Document doc = Jsoup.parse(file, null);
				String [] titles = {"","Highlights", "History", "Size", "Personality", "Health", "Care", "Feeding", "Coat Color And Grooming", "Children And Other Pets", "Breed Organizations"};
				Elements dogName = doc.select("header h1");
				Elements sections = doc.select("h2[class=js-section-heading description-title]");
				Elements textual = doc.select("div[class = breed-data-item-content js-breed-data-section]");
				Elements stats = doc.select("span[class = characteristic]");
				// Elements statData = doc.select();
				int count = 0;
				boolean run = true;
				String vital = "";
				String dog = (dogName.eq(0).text());
				// listing += (dog+"\n");
				// System.out.println(dog);
				for(Element dogs : dogName){
						for(Element stat: stats){
							vital += (stat.text() + " ");
							vital += (stat.nextSibling());
							count ++;
						}
						listing += (vital+"\n");
						queue.add(vital);
						count = 0;
						int count2 = 0;
				 	for(String title : titles){
						//System.out.println(section.text());
						run = true;
						for(Element section : sections){

							//System.out.println(section.text());
							if(section.text().equals(title)){
								//System.out.println(section.text());
								//System.out.println("I equaled a title");
								//System.out.println(section.text());
								String fix = textual.eq(count).text();
								fix = fix.replace('\'', '\"');
								queue.add(fix);
								listing += (fix+"\n");
								run = false;
							}else{
								//System.out.println("I am in the else statement");
							}
							//else if(section.text().equals("Highlights")){
							// 	System.out.println(textual.eq(count).text());
							// }else if(section.text().equals("History")){
							// 	System.out.println(textual.eq(count).text());
							// }
						}if(run){
							//System.out.println("I am in run");
							//count2++;
								queue.add("No information found");
								listing += ("No information found\n");
							}
							count++;
						//System.out.println(section.text());
			}
			//System.out.println(count2);
			}
			//writeToFile(dog, listing);
			return listing;
			}catch(Exception e){
				e.printStackTrace();
			}
			return listing;
		}

		public static void GenerateName(File file){
			Queue<String> queue = new LinkedList();

			try{
				Document doc = Jsoup.parse(file, null);
				Elements dogName = doc.select("header h1");
				String dog = (dogName.eq(0).text());
			}catch(Exception e){
				e.printStackTrace();
			}
		}


	public static Stack<Double> getAverage(Stack<Double> stack){
		double avg = 0;
		Stack<Double> ret = new Stack<Double>();
		int size = stack.size();
		double var = 0;
		DecimalFormat df = new DecimalFormat("###.##");
		while(!stack.isEmpty()){
			avg += stack.peek();
			var = Double.valueOf(df.format(stack.pop()));
			ret.add(var);
		}
		avg = Double.valueOf(df.format(avg/size));
		ret.add(avg);
		return ret;
	}

	public static File getHTML(String path){
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
public static void ShowAllDogBreeds(File file){
	try{
		Document doc = Jsoup.parse(file, null);
		Elements resultLinks = doc.select(("div[class=group-list-item]"));
		char[] array = new char[26];
		int index = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			array[index++] = c;
		String listOfDogs = "";
		for (int i = 0; i < array.length; i++){
				for (Element link : resultLinks){
					if (link.text().startsWith(Character.toString(array[i])))
						listOfDogs += link.text() + ",";
				}
				if (listOfDogs.indexOf(",") > 0)
					listOfDogs = listOfDogs.substring(0, listOfDogs.lastIndexOf(","));
					listOfDogs += "\n\n";
		}
		System.out.println(listOfDogs);
}catch(Exception e){
	System.out.println("There was an error in the something");
}
}
public static void getDogHTML(File file, String arg){
	System.out.println(arg);
	try{
		Document doc = Jsoup.parse(file, null);
		Elements dogLinks = doc.select(("div[class=group-list-item] h2 a[href]"));
		for(Element link : dogLinks){
			//System.out.println(link.text().charAt(0));
			int num = (int)link.text().charAt(0);
			//System.out.println(num);
			if(arg.equals("class01")&&(num >=(int)'A' && num <= (int)'F')){
				String linkDog = link.attr("href");
				File gotten = getHTML(linkDog);
				GenerateBCR(gotten);
				GenerateTextualData(gotten);
				GenerateName(gotten);
				//System.out.println(link.attr("href"));
			}
			if(arg.equals("class02")&&(num >(int)'F' && num <= (int)'N')){
				String linkDog = link.attr("href");
				File gotten = getHTML(linkDog);
				GenerateBCR(gotten);
				GenerateTextualData(gotten);
				GenerateName(gotten);
				//System.out.println(link.attr("href"));
			}
			if(arg.equals("class03")&&(num >(int)'N' && num <= (int)'T')){
				String linkDog = link.attr("href");
				File gotten = getHTML(linkDog);
				GenerateBCR(gotten);
				GenerateTextualData(gotten);
				GenerateName(gotten);
				//System.out.println(link.attr("href"));
			}
			if(arg.equals("class04")&&(num >(int)'T' && num <= (int)'Z')){
				String linkDog = link.attr("href");
				File gotten = getHTML(linkDog);
				GenerateBCR(gotten);
				GenerateTextualData(gotten);
				GenerateName(gotten);

				//System.out.println(link.attr("href"));
			}

		}
	}catch(Exception e){
		System.out.println("Error in getDogHTML");
	}
}
	public static void task1(File file){
		try{
			Document doc = Jsoup.parse(file, null);
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
	public static void task2(){
		try{
			File dogNames = new File("../dataset/t1_output01.txt");
			Scanner listing = new Scanner(dogNames);
			boolean ending = true;
			while(listing.hasNextLine()){
				String index = listing.nextLine();
				Scanner scan = new Scanner(index).useDelimiter(": ");
				String arrayNames [] = new String[2];
				arrayNames[0] = scan.next();
				arrayNames[1] = scan.next();
				File dogHtml = getHTML(arrayNames[1]);
				String BCR = GenerateBCR(dogHtml);
				String TBCR = GenerateTextualData(dogHtml);
				String writing = (arrayNames[0] + "\n" + BCR + TBCR + "\n");
				writeToFile("t2_output01.txt", writing, true);
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void task3(){
		try{
			File dogNames = new File("../dataset/t1_output02.txt");
			Scanner listing = new Scanner(dogNames);
			boolean ending = true;
			while(listing.hasNextLine()){
				String index = listing.nextLine();
				Scanner scan = new Scanner(index).useDelimiter(": ");
				String arrayNames [] = new String[2];
				arrayNames[0] = scan.next();
				arrayNames[1] = scan.next();
				File dogHtml = getHTML(arrayNames[1]);
				String BCR = GenerateBCR(dogHtml);
				String TBCR = GenerateTextualData(dogHtml);
				String writing = (arrayNames[0] + "\n" + BCR + TBCR + "\n");
				writeToFile("t3_output01.txt", writing, true);
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void task4(){
		try{
			File dogNames = new File("../dataset/t1_output03.txt");
			Scanner listing = new Scanner(dogNames);
			boolean ending = true;
			while(listing.hasNextLine()){
				String index = listing.nextLine();
				Scanner scan = new Scanner(index).useDelimiter(": ");
				String arrayNames [] = new String[2];
				arrayNames[0] = scan.next();
				arrayNames[1] = scan.next();
				File dogHtml = getHTML(arrayNames[1]);
				String BCR = GenerateBCR(dogHtml);
				String TBCR = GenerateTextualData(dogHtml);
				String writing = (arrayNames[0] + "\n" + BCR + TBCR + "\n");
				writeToFile("t4_output01.txt", writing, true);
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void task5(){
		try{
			File dogNames = new File("../dataset/t1_output04.txt");
			Scanner listing = new Scanner(dogNames);
			boolean ending = true;
			while(listing.hasNextLine()){
				String index = listing.nextLine();
				Scanner scan = new Scanner(index).useDelimiter(": ");
				String arrayNames [] = new String[2];
				arrayNames[0] = scan.next();
				arrayNames[1] = scan.next();
				File dogHtml = getHTML(arrayNames[1]);
				String BCR = GenerateBCR(dogHtml);
				String TBCR = GenerateTextualData(dogHtml);
				String writing = (arrayNames[0] + "\n" + BCR + TBCR + "\n");
				writeToFile("t5_output01.txt", writing, true);
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void task6(String token){
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
		dropbox.uploadFile("/Users/nick/Computer_Science/441/cmpsc441-toccin-castellanosr-mahoneyr/lab4/dataset/t6_output01.txt", "/441", client);
	}

	public static void deleteFiles(){
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

	public static void main(String[] args) {
			String arg = args[0];
			String token = args[1];
			System.out.println(arg);
			String path = "http://dogtime.com/dog-breeds/profiles";
			File profiles = getHTML(path);
			deleteFiles();
			task1(profiles);
			System.out.println("Task 1 has been completed");
			task2();
			System.out.println("Task 2 has been completed");
			task3();
			System.out.println("Task 3 has been completed");
			task4();
			System.out.println("Task 4 has been completed");
			task5();
			System.out.println("Task 5 has been completed");
			task6(token);
			System.out.println("The upload has been accomplished, check on dropbox.");

			// ShowAllDogBreeds(profiles);
			// getDogHTML(profiles, arg);
			//GenerateBCR(getit);

	}
}
