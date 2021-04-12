package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
	private static Connection connection;
	private static Statement statement;


	public static void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:main.db");
			statement = connection.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static int addUser(String login, String pass, String nickname) {
		try {
			String query = "INSERT INTO users (login, password, nickname) VALUES (?, ?, ?);";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, login);
			ps.setInt(2, pass.hashCode());
			ps.setString(3, nickname);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int addHistory(String nickname, String message) {
		try {
			String query = "INSERT INTO history (nickname, message) VALUES (?, ?);";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, nickname);
			ps.setString(2, message);
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void addToBlacklist(String nickname) {
		try {
			String query = "INSERT INTO blacklist (nickname) VALUES (?);";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, nickname);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkBlacklist(String nickname) {
		List<String> bl = new ArrayList<>();
		String query = "select * from blacklist";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String rs = resultSet.getString(1);
				bl.add(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} return (bl.contains(nickname));
	}

	public static String getNicknameByLoginAndPass(String login, String pass) {
		String query = String.format("select nickname, password from users where login='%s'", login);
		try {
			ResultSet rs = statement.executeQuery(query); // возвращает выборку через select
			int myHash = pass.hashCode();
			// кеш числа 12345
			// изменим пароли в ДБ на хеш от строки pass1

			if (rs.next()) {
				String nick = rs.getString(1);
				int dbHash = rs.getInt(2);
				if (myHash == dbHash) {
					return nick;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
