
import java.sql.*;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
public class MySQLDriver{
	private static String dbURL = "jdbc:mysql://18.217.75.143:3306/";
	private static String engineRepoDBname = "dogbreeds";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static String login = "root";
	private static String password = "441db";
	private static Connection conn = null;

	public static void storeStarData(Queue<Double> stack, String name) throws SQLException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL + engineRepoDBname, login, password);
			String insertion = "";
			insertion+="\'"+name+"\',";
			while(!stack.isEmpty()){
					if(stack.size()==1){
						insertion += "\'"+stack.remove()+"\'";
					}else{
					insertion += "\'"+stack.remove()+"\',";
				}
			}

			Statement st = conn.createStatement();
			String q = "INSERT INTO dogs_bcr " + " (dogbreedid, Adaptability, Adapts_Well_to_Apartment_Living, Good_For_Novice_Owners, Sensitivity_Level, Tolerates_Being_Alone, Tolerates_Cold_Weather, Tolerates_Hot_Weather, All_Around_Friendliness, Affectionate_With_Family, Incredibly_Kid_Friendly_Dogs, Dog_Friendly, Friendly_Towards_Strangers, Health_Grooming, Amount_of_Shedding, Drooling_Potential, Easy_To_Groom, General_Health, Potential_For_Weight_Gain, Size, Trainabilty, Easy_To_Train, Intelligence, Potential_For_Mouthiness, Prey_Drive, Tendency_To_Bark_Or_Howel, Wanderlust_Potential, Exercise_Needs, Energy_Level, Intensity, Exercise_Need, Potential_For_Playfullness) VALUES ("+insertion+");";

			System.out.println("Query: " + q);
			// System.out.println("-----------------------------------");
			st.executeUpdate(q);
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
	}

	public static void storeTextual(Queue<String> stack, String name) throws SQLException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL + engineRepoDBname, login, password);
			String insertion = "";
			insertion+="\'"+name+"\',";
			int count = 0;
			while(!stack.isEmpty()){
					if(stack.size()==1){
						insertion += "\'"+stack.remove()+"\'";
						count++;
					}else{
					insertion += "\'"+stack.remove()+"\',";
					//System.out.println(insertion);
					count++;
				}
			}
			System.out.println(count);

			Statement st = conn.createStatement();
			String q = "INSERT INTO dogs_texual " + " (dogbreedid, Vital_Stats, Description, Highlights, Histroy, Size, Personality, Health, Care, Feeding, Coat_Color_and_Grooming, Children_and_Other_Pets, Breed_Organization) VALUES ("+insertion+");";

			//System.out.println("Query: " + q);
			// System.out.println("-----------------------------------");
			st.executeUpdate(q);
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
	}

	public static void storeName(String name) throws SQLException {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL + engineRepoDBname, login, password);
			String insertion = "";
			insertion+="\'"+name+"\',";
			insertion+="\'"+name+"\'";

			Statement st = conn.createStatement();
			String q = "INSERT INTO dogs_breedlist " + " (dogbreedid, dogbreedname) VALUES ("+insertion+");";

			//System.out.println("Query: " + q);
			// System.out.println("-----------------------------------");
			st.executeUpdate(q);
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		}
	}

}
